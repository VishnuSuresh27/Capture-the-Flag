package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATARBLUE = new TETile('@', Color.blue, Color.white, "you");

    public static final TETile AVATARRED = new TETile('@', Color.red, Color.white, "you");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile BLUE = new TETile('"', Color.blue, Color.black, "blue");
    public static final TETile RED = new TETile('"', Color.red, Color.black, "red");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile BLUEDEN = new TETile('≈', Color.blue, Color.orange, "blueden");
    public static final TETile REDDEN = new TETile('≈', Color.red, Color.orange, "redden");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile REDFLAG = new TETile('█', Color.red, Color.red,
            "red flag");
    public static final TETile BLUEFLAG = new TETile('█', Color.blue, Color.blue,
            "blue flag");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile REDPLAYER = new TETile('♠', Color.red, Color.gray, "red player");
    public static final TETile BLUEPLAYER = new TETile('♠', Color.blue, Color.gray, "blue player");
}


