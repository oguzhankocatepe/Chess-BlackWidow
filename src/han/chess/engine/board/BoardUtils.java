package han.chess.engine.board;

import java.awt.*;

public class BoardUtils {

    public static final int TILESIZE = 8;
    public static final String [] ALGEBRAIC_NOTATION = {"a","b","c","d","e","f","g","h"};

    private BoardUtils(){
        throw new RuntimeException("Private Constructor");
    }
    public static boolean checkValid(final Point p) {
        return (p.x >= 0 & p.y >=0 & p.x < TILESIZE & p.y < TILESIZE) ;
    }

    public static String getAlgebraicNotation(final Point p) {
        return ALGEBRAIC_NOTATION[p.x]+p.y;
    }
}
