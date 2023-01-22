package byow.Core;

import byow.TileEngine.Tileset;
import byow.Core.Player;
/*
 * Objects of this class need to handle game functionality.
 */
public class Game {
    public boolean gameOver;
    public Player blue;
    public Player red;
    public boolean redwin;
    public boolean bluewin;

    public Game(WorldGen world) {
        gameOver = false;
        this.redwin = false;
        this.bluewin = false;
        this.blue = new Player(true, world, Tileset.AVATARBLUE);
        this.red = new Player(false, world, Tileset.AVATARRED);
    }

    /*
     * Game over when player of same color as den reaches
     * the den with the opposite flag.
     * This is a static method to keep track either player winning the game.
     */
    public boolean isGameOver() {
        if(Player.bluePlayer.flagPoss == true
           && Player.bluePlayer.location.equals(Player.bluePlayer.den.flaglocation)) {
            gameOver = true;
            bluewin = true;
            return true;
        }
        if(Player.redPlayer.flagPoss == true
                && Player.redPlayer.location.equals(Player.redPlayer.den.flaglocation)) {
            gameOver = true;
            redwin = true;
            return true;
        }
        return false;
    }
    public static void flagCheck(){
        if(Player.bluePlayer.location.inside(Player.redPlayer.illuminationZone())){
            Player.bluePlayer.flagPoss = false;
            //Coordinate flagLoc = Player.redPlayer.den.flaglocation;

        }
        if(Player.redPlayer.location.inside(Player.bluePlayer.illuminationZone())){
            Player.redPlayer.flagPoss = false;
        }
    }
    public static void possCheck(){
        if(Player.bluePlayer.location.equals(Player.redPlayer.den.flaglocation)){
            Player.bluePlayer.flagPoss = true;
            System.out.println(true);
        }
        if(Player.redPlayer.location.equals(Player.bluePlayer.den.flaglocation)){
            Player.redPlayer.flagPoss = true;
            System.out.println(true);
        }
    }

    public String blueflagposs() {
        if(Player.bluePlayer.flagPoss == true) {
            return "Blue: Captured Flag";
        }
        return "Blue: Not Captured Flag";
    }

    public String redflagposs() {
        if(Player.redPlayer.flagPoss == true) {
            return "Red: Captured Flag";
        }
        return "Red: Not Captured Flag";
    }
}
