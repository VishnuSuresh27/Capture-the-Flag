package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Player {
    public boolean color;
    public Coordinate location;
    public boolean flagPoss;
    public TETile avatar;
    public WorldGen world;


    public static Player bluePlayer;
    public static Player redPlayer;

    public Den den; //The den to which this player belongs to.
    public Player(boolean colorT, WorldGen world, TETile avatar) { //avatar is the Tileset icon to be used for this color.
        color = colorT; //True = Blue False = Red
        if(colorT){
            bluePlayer = this;
        }else{
            redPlayer = this;
        }
        flagPoss = false; //Initially the color doesn't possess flag
        this.avatar = avatar;
        this.world = world;
        if(color) {//blue
            location = Room.room2coor.get(world.blueDen).get(0); //start location
            world.maingrid[location.x1][location.y1] = avatar;
            den = world.blueDen;
        }
        else {//red
            location = Room.room2coor.get(world.redDen).get(0); //start location
            world.maingrid[location.x1][location.y1] = avatar;
            den = world.redDen;
        }
    }
    public Coordinate[] illuminationZone(){
        Coordinate leftBottom = new Coordinate(this.location.x1 - 5, this.location.y1 - 5);
        Coordinate rightTop = new Coordinate(this.location.x1 + 5, this.location.y1 + 5);
        Coordinate[] reti = new Coordinate[2];
        reti[0] = leftBottom;
        reti[1] = rightTop;
        return reti;
    }
    public static void move(char key, TETile[][] staticWorld){
        if(key == 'w' || key == 'a' || key == 's' || key == 'd') {
            Coordinate oldLoc = new Coordinate(bluePlayer.location.x1,bluePlayer.location.y1);
            bluePlayer.location.moveHelper(key);
            if(!oldLoc.equals(bluePlayer.location)) {
                bluePlayer.replace(oldLoc, staticWorld);
            }
        }else if(key == 'i' || key == 'j' || key == 'k' || key == 'l'){
            Coordinate oldLoc = new Coordinate(redPlayer.location.x1,redPlayer.location.y1);
            redPlayer.location.moveHelper(key);
            if(!oldLoc.equals(redPlayer.location)) {
                redPlayer.replace(oldLoc, staticWorld);
            }

        }
    }
    private void replace(Coordinate coor, TETile[][]staticWorld) {
        world.maingrid[this.location.x1][this.location.y1] = this.avatar;
        world.maingrid[coor.x1][coor.y1] = staticWorld[coor.x1][coor.y1];
        //System.out.println(world.maingrid == staticWorld.maingrid);
    }
    /*
    public void move(char key) {
        if(color &&
                (key == 'w' || key == 'a' || key == 's' || key == 'd')) {
            throw new IllegalArgumentException();
        }
        if(!color &&
                (key == 'i' || key == 'j' || key == 'k' || key == 'j')) {
            throw new IllegalArgumentException();
        }
        replace(location, key);
        location.moveHelper(key);
        // Remember. When building the Game UI, you will need to render the frame after every call to move.
    }
    */


    /*

    private void replace(Coordinate currPos, char keystroke, WorldGen staticWorld) {
        TETile temp;
        if((keystroke == 'w' || keystroke == 'i') && world.maingrid[currPos.x1][currPos.y1 + 1] != Tileset.WALL) {
            temp = world.maingrid[currPos.x1][currPos.y1 + 1];
            if((temp == Tileset.BLUEFLAG && color == false) || (temp == Tileset.REDFLAG && color == true)) {
                flagPoss = true;
                temp = (temp == Tileset.BLUEFLAG)?(Tileset.BLUEDEN):(Tileset.REDDEN);
            }
            if(keystroke == 'w') {
                world.maingrid[currPos.x1][currPos.y1 + 1] = bluePlayer.avatar;
            }else{
                world.maingrid[currPos.x1][currPos.y1 + 1] = redPlayer.avatar;
            }
            world.maingrid[currPos.x1][currPos.y1-1] = staticWorld.maingrid[currPos.x1][currPos.y1];
            //currPos.y1 = currPos.y1 + 1;
        }
        if((keystroke == 's' || keystroke == 'k') && world.maingrid[currPos.x1][currPos.y1 - 1] != Tileset.WALL) {
            temp = world.maingrid[currPos.x1][currPos.y1 - 1];
            if((temp == Tileset.BLUEFLAG && color == false) || (temp == Tileset.REDFLAG && color == true)) {
                flagPoss = true;
                temp = (temp == Tileset.BLUEFLAG)?(Tileset.BLUEDEN):(Tileset.REDDEN);
            }
            if(keystroke == 's'){
                world.maingrid[currPos.x1][currPos.y1 - 1] = bluePlayer.avatar;
            }else{
                world.maingrid[currPos.x1][currPos.y1 - 1] = redPlayer.avatar;
            }
            //world.maingrid[currPos.x1][currPos.y1 - 1] = avatar;
            world.maingrid[currPos.x1][currPos.y1] = temp;
            //currPos.y1 = currPos.y1 - 1;
        }
        if((keystroke == 'd' || keystroke == 'l') && world.maingrid[currPos.x1 + 1][currPos.y1] != Tileset.WALL) {
            temp = world.maingrid[currPos.x1 + 1][currPos.y1];
            if((temp == Tileset.BLUEFLAG && color == false) || (temp == Tileset.REDFLAG && color == true)) {
                flagPoss = true;
                temp = (temp == Tileset.BLUEFLAG)?(Tileset.BLUEDEN):(Tileset.REDDEN);
            }
            if(keystroke == 'd'){
                world.maingrid[currPos.x1 + 1][currPos.y1] = bluePlayer.avatar;
            }else{
                world.maingrid[currPos.x1 + 1][currPos.y1] = redPlayer.avatar;
            }
            //world.maingrid[currPos.x1 + 1][currPos.y1] = avatar;
            world.maingrid[currPos.x1][currPos.y1] = temp;
            //currPos.x1 = currPos.x1 + 1;
        }
        if((keystroke == 'a' || keystroke == 'j') && world.maingrid[currPos.x1 - 1][currPos.y1] != Tileset.WALL) {
            temp = world.maingrid[currPos.x1 - 1][currPos.y1];
            if((temp == Tileset.BLUEFLAG && color == false) || (temp == Tileset.REDFLAG && color == true)) {
                flagPoss = true;
                temp = (temp == Tileset.BLUEFLAG)?(Tileset.BLUEDEN):(Tileset.REDDEN);
            }
            if(keystroke == 'a'){
                world.maingrid[currPos.x1 - 1][currPos.y1] = bluePlayer.avatar;
            }else{
                world.maingrid[currPos.x1 - 1][currPos.y1] = redPlayer.avatar;
            }
            //world.maingrid[currPos.x1 - 1][currPos.y1] = avatar;
            world.maingrid[currPos.x1][currPos.y1] = temp;
            //currPos.x1 = currPos.x1 - 1;
        }
    }

     */


}

