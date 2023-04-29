package han.chess.engine.board;

import han.chess.engine.pieces.Piece;
import org.carrot2.shaded.guava.common.collect.ImmutableMap;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile {

    static final int TILESIZE = 8;
    protected static Point tileCoordinate;

    private static Map<Point,EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();

    private static Map<Point, EmptyTile> createAllPossibleEmptyTiles() {

        final Map<Point,EmptyTile> emptyTileMap = new HashMap<>();

        for (int i=0 ;i < TILESIZE ; i++)
            for (int j=0 ;i < TILESIZE ; j++) {
                Point point = new Point(i, j);
                emptyTileMap.put(point, new EmptyTile(point));
            }

        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile(final Point point, final Piece piece){
        return piece != null ? new OccupiedTile(point,piece) : EMPTY_TILES.get(tileCoordinate);
    }

    Tile(Point point){
        tileCoordinate = point;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile{

        EmptyTile(final Point point ){
            super(point);
        }

        @Override
        public boolean isTileOccupied(){
            return false;
        }

        @Override
        public Piece getPiece(){
            return null;
        }
    }

    public static final class OccupiedTile extends Tile{

        private final Piece pieceOnTile;

        OccupiedTile(final Point point,final Piece piece){
            super(point);
            pieceOnTile = piece;
        }

        @Override
        public boolean isTileOccupied(){
            return true;
        }

        @Override
        public Piece getPiece(){
            return pieceOnTile;
        }
    }
}
