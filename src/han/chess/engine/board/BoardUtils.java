package han.chess.engine.board;

import java.awt.*;

public class BoardUtils {

    public static final int TILESIZE = 8;

    private BoardUtils(){
        throw new RuntimeException("Private Constructor");
    }
    public static boolean checkValid(final Point p) {
        return (p.x >= 0 & p.y >=0 & p.x < TILESIZE & p.y < TILESIZE) ? true : false;
    }
}
