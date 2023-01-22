package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;

import java.awt.*;
import java.util.ArrayList;

public class DaMain {
    private int width;
    private int height;
    public static String seed;

    private In in;

    private String[] res;

    public DaMain(int width, int height) {
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public static TETile[][] TwoDCopy(TETile[][]a) {
        TETile[][] b = new TETile[a.length][];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i].clone();
        }
        return b;
    }

    public static void main(String[] args) throws FileNotFoundException {
        DaMain game = new DaMain(70, 40);
        game.startGameDisplay();
        game.startGame(seed, 'n');
    }

    public void startGameDisplay() throws FileNotFoundException {
        drawFrame("Press n to start, l to load and q to quit");
        Input();
        drawFrame("Enter seed, press S when done.");
        seed = Input('S');
    }

    public void drawFrame(String s) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height / 2, s);
        StdDraw.line(0.0, 38.0, 70.0, 38.0);
        StdDraw.show();
    }

    public String Input(char endchar) {
        String result = "";
        while(true) {
            if(StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if(c != endchar) {
                    result = result + c;
                    System.out.println(result);
                    drawFrame(result);
                }
                else {
                    break;
                }
            }
        }
        return result;
    }

    public String Input() throws FileNotFoundException {
        String result = "";
        ArrayList<Character> ch = new ArrayList<>();
        ch.add('n');
        ch.add('l');
        ch.add('q');
        while(true) {
            if(StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if(!ch.contains(c)) {
                    result = result + c;
                    System.out.println(result);
                    drawFrame(result);
                }
                else if(c == 'q') {
                    System.exit(0);
                }
                else if(c == 'n') {
                    break;
                }
                else { // c == 'l'
                    in = new In("SaveandLoad.txt");
                    String arrinput = in.readLine();
                    res = arrinput.split(",");
                    startGame(res[0], 'l');
                }
            }
        }
        return result;
    }

    public void quit() throws FileNotFoundException{
        File f = new File("SaveandLoad.txt");
        f.delete();
        File g = new File("SaveandLoad.txt");
        Out out = new Out("SaveandLoad.txt");
        out.print(this.seed+",");
        out.print(Player.bluePlayer.location.toString()+",");
        out.print(Player.redPlayer.location.toString()+",");
        out.print(Player.bluePlayer.flagPoss+",");
        out.print(Player.redPlayer.flagPoss);
        System.exit(0);
    }

    public void startGame(String seed, char ch) throws FileNotFoundException {
        //String input = "VishnuMurali!";
        TERenderer ter = new TERenderer();
        WorldGen world = new WorldGen(ter, seed, 70, 40);
        world.buildallrooms();
        world.denbuilder();
        world.connectallrooms();
        world.zonebuilder();
        TETile[][] finalWorldFrame = world.returnWorld();
        TETile[][] staticWorld = TwoDCopy(world.maingrid);
        TETile[][] renderWorld = new TETile[70][40];

        ter.renderFrame(finalWorldFrame);
        Game daGame = new Game(world);

        if(ch == 'l') {
            renderWorld[Player.bluePlayer.location.x1][Player.bluePlayer.location.y1] = Tileset.BLUEDEN;
            renderWorld[Player.redPlayer.location.x1][Player.redPlayer.location.y1] = Tileset.REDDEN;
            Player.bluePlayer.location = new Coordinate(Integer.parseInt(res[1]), Integer.parseInt(res[2]));
            Player.redPlayer.location = new Coordinate(Integer.parseInt(res[3]), Integer.parseInt(res[4]));
            renderWorld[Player.bluePlayer.location.x1][Player.bluePlayer.location.y1] = Tileset.AVATARBLUE;
            renderWorld[Player.redPlayer.location.x1][Player.redPlayer.location.y1] = Tileset.AVATARRED;
            if(res[5].equals("1")) {Player.bluePlayer.flagPoss = true;}
            else {Player.bluePlayer.flagPoss = false;}
            if(res[6].equals("1")) {Player.redPlayer.flagPoss = true;}
            else {Player.redPlayer.flagPoss = false;}
        }

        while(!daGame.isGameOver()){
            char key = 'x'; //Setting it something random.
            if(ter.hasKey()){
                key = ter.nextKey();
                if (key != 't') {
                    Player.move(key, staticWorld);
                }
                if(key == 'q') {
                    quit();
                }
                System.out.println(key);
            }
            Game.flagCheck();
            Game.possCheck();

            for(int x = 0; x < finalWorldFrame.length; x++){
                for(int y = 0; y < finalWorldFrame[0].length; y++){
                    Coordinate curr = new Coordinate(x,y);
                    if(curr.inside(Player.redPlayer.illuminationZone()) || curr.inside(Player.bluePlayer.illuminationZone())){
                        renderWorld[x][y] = finalWorldFrame[x][y];
                    }else{
                        renderWorld[x][y] = Tileset.NOTHING;
                    }
                }
            }
            String blueflagstate = daGame.blueflagposs();
            String redflagstate = daGame.redflagposs();
            for(int x = 0; x < blueflagstate.length(); x++) {
                char current = blueflagstate.charAt(x);
                renderWorld[x][39] = new TETile(current, Color.white, Color.black, "");
            }
            for(int x = 0; x < redflagstate.length(); x++) {
                char current = redflagstate.charAt(x);
                renderWorld[x][38] = new TETile(current, Color.white, Color.black, "");
            }
            if(key == 't') { //This is to momentarily show the entire world
                ter.renderFrame(finalWorldFrame);
                StdDraw.pause(2500);
            }
            ter.renderFrame(renderWorld);
        }
        if(daGame.bluewin) {
            drawFrame("Player blue wins!");
        }
        if(daGame.redwin) {
            drawFrame("Player red wins!");
        }
    }
}
