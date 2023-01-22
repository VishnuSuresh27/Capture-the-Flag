package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;


public class Coordinate {

    public int x1;
    public int y1;

    public Coordinate(int x, int y) {
        x1 = x;
        y1 = y;
    }

    public boolean equals(Coordinate c) {
        // Returns a boolean on whether a coordinate equals c
        if (c.x1 == this.x1 && c.y1 == this.y1){
            return true;
        }
        return false;
    }
    public ArrayList norm(Coordinate c) {
        // returns the necessary x and y traversal from a coordinate to another
        // return format [xDiff, yDiff]
        ArrayList<Integer> traversal = new ArrayList<>();
        traversal.add(c.x1-this.x1);
        traversal.add(c.y1-this.y1);
        return traversal;
    }

    /*
     * Returns euclidean distance between two coordinates.
     */
    public double normDist(Coordinate c) {
        ArrayList al = this.norm(c);
        return Math.sqrt((int)al.get(0)*(int)al.get(0) + (int)al.get(1)*(int)al.get(1));
    }

    /*
     * Returns euclidean distances between two coordinates where one is (0,0)
     */
    public double normDist() {
        return normDist(new Coordinate(0, 0));
    }

    public ArrayList norm_dirn(Coordinate c) {
        // returns the x and y direction [+1 or -1] from a coordinate to another
        // return format [xDiff, yDiff]
        ArrayList<Integer> direction = new ArrayList<>();
        if(c.x1-this.x1 == 0) {direction.add(0);}
        else {direction.add((c.x1-this.x1)/Math.abs(c.x1-this.x1));}
        if(c.y1-this.y1 == 0) {direction.add(0);}
        else {direction.add((c.y1-this.y1)/Math.abs(c.y1-this.y1));}
        return direction;
    }
    public void moveHelper(char key) {
        TETile[][] CWorld = WorldGen.returnWorld();
        if(key == 'w' || key == 'i') {
            if(!CWorld[this.x1][this.y1 + 1].equals(Tileset.WALL)) {
                this.y1 += 1;
            }
            System.out.println(this);
        }
        if(key == 'a' || key == 'j') {
            if(!CWorld[this.x1 -1 ][this.y1].equals(Tileset.WALL)) {
                this.x1 -=1;
            }
            System.out.println(this);

        }
        if(key == 's' || key == 'k') {
            if(!CWorld[this.x1][this.y1 -1].equals(Tileset.WALL)) {
                this.y1 -=1;
            }
            System.out.println(this);

        }
        if(key == 'd' || key == 'l') {
            if(!CWorld[this.x1 + 1][this.y1].equals(Tileset.WALL)) {
                this.x1 +=1;
            }
            System.out.println(this);

        }
    }
    public boolean inside(Coordinate[] corners){
        Coordinate leftBottom = corners[0];
        Coordinate rightTop = corners[1];
        if(this.x1 > leftBottom.x1 && this.x1 < rightTop.x1){
            if(this.y1 > leftBottom.y1 && this.y1 < rightTop.y1){
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString(){
        return(this.x1 + ","+this.y1);
    }
}
