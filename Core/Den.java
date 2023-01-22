package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Den extends Room {

    public boolean color;
    public boolean gameOver;
    public Coordinate flaglocation;
    public WorldGen world;

    public Den(int x1, int y1, Random r, boolean color, WorldGen world) { //true means blue, false means red
        super(x1, y1, r);
        this.color = color;
        this.gameOver = false;
        flaglocation = room2coor.get(this).get(1);
    }

    @Override
    /*
     * Building the den in a different color then the regular rooms.
     */
    public void roombuilder(TETile[][] world) {
        for(int x = x1; x <= x2; x++) {
            for(int y = y1; y <= y2; y++) {
                if(x == x1 || y == y1 || x == x2 || y == y2) {
                    world[x][y] = Tileset.WALL;
                }
                else {
                    if(color == true) {world[x][y] = Tileset.BLUEDEN;}
                    if(color == false) {world[x][y] = Tileset.REDDEN;}
                }
            }
        }
        if (color == true) {//blue
            world[flaglocation.x1][flaglocation.y1] = Tileset.BLUEFLAG;
        }
        if (color == false) {//red
            world[flaglocation.x1][flaglocation.y1] = Tileset.REDFLAG;
        }
    }

}
