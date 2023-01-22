package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import edu.princeton.cs.introcs.StdDraw;

/*
 * This class creates world objects.
 * Each world object has attributes like the tilerenderer, seed, max no. of rooms,
 * max dimension of rooms, the random object, width and height of the world
 */
public class WorldGen {
    public static TETile[][] maingrid;
    private TETile[][] testgrid;
    private int seed;
    private static final int MAXNOROOMS = 10;
    private int norooms;
    private Random r;
    private int width;
    private int height;
    private static ArrayList<Room> roomColl;
    private RandomUtils randomtools;
    public Den redDen; //redDen closest to origin
    public Den blueDen; //blueDen farthest away from origin
    private Room closesttoOrigin;
    private Room farfromOrigin;
    public static ArrayList<TETile> tilecoll; //A collection of all non-den tiles that are used in this world.
    public ArrayList<Coordinate> redzone;
    public ArrayList<Coordinate> bluezone;

    public WorldGen(TERenderer ter, String input, int width, int height) {
        this.width = width;
        this.height = height;
        ter.initialize(width, height);
        maingrid = new TETile[width][height];
        testgrid = new TETile[width][height];
        seed = input.hashCode();
        r = new Random(seed);
        norooms = randomtools.uniform(r, 5, MAXNOROOMS);
        redzone = new ArrayList<>();
        bluezone = new ArrayList<>();
        roomColl = new ArrayList<>();
        tilecoll = new ArrayList<>(); //A collection of all tiles that are not walls
        tilecoll.add(Tileset.GRASS);
        tilecoll.add(Tileset.BLUE);
        tilecoll.add(Tileset.RED);
        tilecoll.add(Tileset.WATER);
        tilecoll.add(Tileset.REDDEN);
        tilecoll.add(Tileset.BLUEDEN);
        tilecoll.add(Tileset.REDFLAG);
        tilecoll.add(Tileset.BLUEFLAG);

        //Initializing the grids
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                maingrid[x][y] = Tileset.NOTHING;
                testgrid[x][y] = Tileset.NOTHING;
            }
        }
    }

    private boolean collisionDetection(Room newRoom) {
        for (Room curr : roomColl) {
            if (newRoom.x1 < curr.x2 + 2 && newRoom.x2 > curr.x1 - 2 &&
                    newRoom.y1 < curr.y2 + 2 && newRoom.y2 > curr.y1 - 2)
                return true;
        }
        return false; //Added by VS - if arrayList is empty, then no collision.
    }

    public void buildallrooms() {
        int i = 1; //Signifies the number of rooms built so far. We need to build norooms number of rooms.
        while (i <= norooms) {
            Room currRoom = new Room(randomtools.uniform(r, 0, width),
                    randomtools.uniform(r, 0, height), r);
            if (!collisionDetection(currRoom) && currRoom.roombounds(maingrid)) {
                roomColl.add(currRoom);
                currRoom.roombuilder(maingrid);
                if (i == 1) {
                    closesttoOrigin = currRoom;
                    farfromOrigin = currRoom;
                } else {
                    if (currRoom.roomId.normDist() < closesttoOrigin.roomId.normDist()) {
                        closesttoOrigin = currRoom;
                    }
                    if (currRoom.roomId.normDist() > farfromOrigin.roomId.normDist()) {
                        farfromOrigin = currRoom;
                    }
                }
                i++;
            }
            else {
                Room.room2coor.remove(currRoom);
            }
        }
    }

    public void denbuilder() {
        //Building redDen
        while (true) {
            Den currDen = new Den(randomtools.uniform(r, 0, width / 2),
                    randomtools.uniform(r, 0, height), r, false, this);
            if (!collisionDetection(currDen) && currDen.roombounds(maingrid) &&
                    currDen.roomId.normDist() < closesttoOrigin.roomId.normDist()) {
                redDen = currDen;
                currDen.roombuilder(maingrid);
                break;
            }
            else {
                Room.room2coor.remove(currDen);
            }
        }
        //Building blueDen
        while (true) {
            Den currDen = new Den(randomtools.uniform(r, width / 2, width),
                    randomtools.uniform(r, 0, height), r, true, this);
            if (!collisionDetection(currDen) && currDen.roombounds(maingrid) &&
                    currDen.roomId.normDist() > farfromOrigin.roomId.normDist()) {
                blueDen = currDen;
                currDen.roombuilder(maingrid);
                break;
            }
            else {
                Room.room2coor.remove(currDen);
            }
        }
        roomColl.add(blueDen);
        roomColl.add(redDen);
        Collections.swap(roomColl, roomColl.size() - 2, 0);
    }


    public void connectallrooms() {
        for (int i = 0; i < roomColl.size() - 1; i++) {
            connect(roomColl.get(i), roomColl.get(i + 1));
        }
    }

    public static TETile[][] returnWorld() {
        return maingrid;
    }

    /*
     * Takes two rooms and connects them rectilinearly.
     */
    public void connect(Room r1, Room r2) {
        /*
         * Picking a random co-ordinate from the walls
         * of room 1 and room 2
         */
        int horsize = r1.perimeter_vertical.size();
        int versize = r2.perimeter_horizontal.size();
        Coordinate startpt = r1.perimeter_vertical.get(randomtools.uniform(r, 0, horsize - 1));
        Coordinate endpt = r2.perimeter_horizontal.get(randomtools.uniform(r, 0, versize - 1));
        connect_helper(startpt.x1, startpt.y1, endpt.x1, startpt.y1, 'x',
                (Integer) startpt.norm_dirn(endpt).get(0));
        connect_helper(endpt.x1, startpt.y1, endpt.x1, endpt.y1, 'y',
                (Integer) startpt.norm_dirn(endpt).get(1));
    }

    private void connect_helper(int xstart, int ystart, int xend, int yend, char ch, int dir) {
        if (ch == 'x') {
            if (xstart - dir >= 0 && xstart - dir <= width &&
                    maingrid[xstart - dir][ystart] == Tileset.NOTHING) {
                maingrid[xstart][ystart] = Tileset.WALL;
                xstart = xstart + dir;
            }
            while (xstart != (xend + dir)) {
                if (tilecoll.contains(maingrid[xstart][ystart])) {
                    if (maingrid[xstart][ystart + 1] == Tileset.NOTHING) {
                        maingrid[xstart][ystart + 1] = Tileset.WALL;
                    }
                    if (maingrid[xstart][ystart - 1] == Tileset.NOTHING) {
                        maingrid[xstart][ystart - 1] = Tileset.WALL;
                    }
                } else if (maingrid[xstart][ystart + 1] == Tileset.WALL &&
                        (tilecoll.contains(maingrid[xstart][ystart + 2]))) {
                    maingrid[xstart][ystart] = maingrid[xstart][ystart + 2];
                    maingrid[xstart][ystart + 1] = maingrid[xstart][ystart + 2];
                    maingrid[xstart][ystart - 1] = Tileset.WALL;
                } else if (maingrid[xstart][ystart - 1] == Tileset.WALL &&
                        (tilecoll.contains(maingrid[xstart][ystart - 2]))) {
                    maingrid[xstart][ystart] = maingrid[xstart][ystart - 2];
                    maingrid[xstart][ystart - 1] = maingrid[xstart][ystart - 2];
                    maingrid[xstart][ystart + 1] = Tileset.WALL;
                } else if (maingrid[xstart][ystart] == Tileset.WALL &&
                        (tilecoll.contains(maingrid[xstart][ystart + 1]))) {
                    maingrid[xstart][ystart] = maingrid[xstart][ystart + 1];
                    maingrid[xstart][ystart - 1] = Tileset.WALL;
                } else if (maingrid[xstart][ystart] == Tileset.WALL &&
                        (tilecoll.contains(maingrid[xstart][ystart - 1]))) {
                    maingrid[xstart][ystart] = maingrid[xstart][ystart - 1];
                    maingrid[xstart][ystart + 1] = Tileset.WALL;
                } else {
                    maingrid[xstart][ystart + 1] = Tileset.WALL;
                    maingrid[xstart][ystart] = Tileset.GRASS;
                    maingrid[xstart][ystart - 1] = Tileset.WALL;
                }
                xstart = xstart + dir;
            }
        }
        if (ch == 'y') {
            while (ystart != (yend + dir)) {
                if (tilecoll.contains(maingrid[xstart][ystart])) {
                    if (maingrid[xstart + 1][ystart] == Tileset.NOTHING) {
                        maingrid[xstart + 1][ystart] = Tileset.WALL;
                    }
                    if (maingrid[xstart - 1][ystart] == Tileset.NOTHING) {
                        maingrid[xstart - 1][ystart] = Tileset.WALL;
                    }
                } else if (maingrid[xstart + 1][ystart] == Tileset.WALL &&
                        (tilecoll.contains(maingrid[xstart + 2][ystart]))) {
                    maingrid[xstart][ystart] = maingrid[xstart + 2][ystart];
                    maingrid[xstart + 1][ystart] = maingrid[xstart + 2][ystart];
                    maingrid[xstart - 1][ystart] = Tileset.WALL;
                } else if (maingrid[xstart - 1][ystart] == Tileset.WALL &&
                        (tilecoll.contains(maingrid[xstart - 2][ystart]))) {
                    maingrid[xstart][ystart] = maingrid[xstart - 2][ystart];
                    maingrid[xstart - 1][ystart] = maingrid[xstart - 2][ystart];
                    maingrid[xstart + 1][ystart] = Tileset.WALL;
                } else if (maingrid[xstart][ystart] == Tileset.WALL &&
                        (tilecoll.contains(maingrid[xstart + 1][ystart]))) {
                    maingrid[xstart][ystart] = maingrid[xstart + 1][ystart];
                    maingrid[xstart - 1][ystart] = Tileset.WALL;
                } else if (maingrid[xstart][ystart] == Tileset.WALL &&
                        (tilecoll.contains(maingrid[xstart - 1][ystart]))) {
                    maingrid[xstart][ystart] = maingrid[xstart - 1][ystart];
                    maingrid[xstart + 1][ystart] = Tileset.WALL;
                } else {
                    maingrid[xstart + 1][ystart] = Tileset.WALL;
                    maingrid[xstart][ystart] = Tileset.GRASS;
                    maingrid[xstart - 1][ystart] = Tileset.WALL;
                }
                ystart = ystart + dir;
                if (maingrid[xstart][ystart] == Tileset.NOTHING) {
                    maingrid[xstart][yend] = Tileset.WALL;
                }
            }
        }
    }

    public void zonebuilder() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Coordinate currCon = new Coordinate(x, y);
                if (currCon.normDist(redDen.roomId) < currCon.normDist(blueDen.roomId) &&
                        tilecoll.contains(maingrid[x][y])) {

                    if (maingrid[x][y] == Tileset.GRASS) {
                        maingrid[x][y] = Tileset.RED;
                    }
                    redzone.add(currCon);
                }
                if (currCon.normDist(redDen.roomId) >= currCon.normDist(blueDen.roomId) &&
                        tilecoll.contains(maingrid[x][y])) {
                    if (maingrid[x][y] == Tileset.GRASS) {
                        maingrid[x][y] = Tileset.BLUE;
                    }
                    bluezone.add(currCon);
                }
            }
        }
    }


    /*
    * Built solely for the purposes of testing.
    */
    public static void main(String[] args){
        String input = "bondo!";
        TERenderer ter = new TERenderer();
        WorldGen world = new WorldGen(ter, input, 70, 40);
        world.buildallrooms();
        world.denbuilder();
        world.connectallrooms();
        world.zonebuilder();
        TETile[][] finalWorldFrame = world.returnWorld();
        ter.renderFrame(finalWorldFrame);
    }
}
