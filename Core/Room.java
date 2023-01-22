package byow.Core;

import byow.TileEngine.Tileset;
import byow.TileEngine.TETile;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Room {
    public int x1; //1 bottom left, 2, top right -  
    public int y1;
    public int x2;
    public int y2;

    public Coordinate roomId; //Bottom-left corner coordinates
    private static final int MAXROOMSIZE = 10; //Setting this to be the maximum length/breadth of any hall we build

    private Random r;
    private RandomUtils randomtools;
    private int width; //If you think you need these, you can
    private int length; // change access to public

    //public HashSet<Coordinate> perimeter = new HashSet<>(); // Defines the outer perimeter of a room for collision detection
    //public static HashMap<Coordinate, Room> coor2Room = new HashMap<>();
    public static HashMap<Room, ArrayList<Coordinate>> room2coor = new HashMap<>(); //Every room (including dens) are mapped to all its coordinates.
    public ArrayList<Coordinate> perimeter_vertical = new ArrayList<>();
    public ArrayList<Coordinate> perimeter_horizontal = new ArrayList<>();


    /* Builds the room object
     * Please pass the same random object created in worldgen.
     * To build a room, you only need to mention the bottomleft coordinate.
     * Room is not built yet
     */
    public Room(int x1, int y1, Random r) { //Please pass the private instance variable r from the worldgen class here.
        this.x1 = x1;
        this.y1 = y1;
        width = randomtools.uniform(r, 4, MAXROOMSIZE);
        length = randomtools.uniform(r, 4, MAXROOMSIZE);
        this.x2 = this.x1 + width;
        this.y2 = this.y1 + length;
        this.roomId = new Coordinate(this.x1, this.y1);
        room2coor.put(this, new ArrayList<>());
        for(int x = this.x1; x <= this.x2; x++ ){
            //perimeter.add(new Coordinate(x, this.y1));
            //coor2Room.put(new Coordinate(x, this.y1), this);
            if(x!=this.x1 && x!=this.x2) {
                perimeter_horizontal.add(new Coordinate(x, this.y1));
                perimeter_horizontal.add(new Coordinate(x, this.y2));
            }
            //perimeter.add(new Coordinate(x, this.y2));
            //coor2Room.put(new Coordinate(x, this.y2), this);
            //room2coor.get(this).add(new Coordinate(x, this.y2));
        }
        for(int y = this.y1; y <= this.y2; y++ ){
            //perimeter.add(new Coordinate(this.x1, y));
            //coor2Room.put(new Coordinate(this.x1, y), this);
            if(y!=this.y1 && y!= this.y2) {
                perimeter_vertical.add(new Coordinate(this.x1, y));
                perimeter_vertical.add(new Coordinate(this.x2, y));
            }
            //perimeter.add(new Coordinate(this.x2, y));
            //coor2Room.put(new Coordinate(this.x2, y), this);
            //room2coor.get(this).add(new Coordinate(this.x2, y));
        }
        for(int x = x1; x <= x2; x++) {
            for(int y = y1; y <= y2; y++) {
                if(!(x == x1 || y == y1 || x == x2 || y == y2)) {
                   room2coor.get(this).add(new Coordinate(x, y));
                }
            }
        }
    }

    /*
     * This returns a boolean to signify whether this room can be built or not.
     * Doesn't consider collisions.
     */
    public boolean roombounds(TETile[][] world) {
        if((x1 < 2 || y1 < 2 ||
                x1 + width >= world.length - 2) || y1 + length >= world[0].length - 2) {
            //System.out.println("This room cannot be built because it would go beyond the grid");
            return false;
        }
        return true;
    }

    /*
     * This is where the room will actually be built.
     * To be used as follows: roomobj.roombuilder(world)
     */
    public void roombuilder(TETile[][] world) {
        for(int x = x1; x <= x2; x++) {
            for(int y = y1; y <= y2; y++) {
                if(x == x1 || y == y1 || x == x2 || y == y2) {
                    world[x][y] = Tileset.WALL;
                }
                else {
                    world[x][y] = Tileset.GRASS;
                }
            }
        }
    }
}