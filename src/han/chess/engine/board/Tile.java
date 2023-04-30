package han.chess.engine.board;

import han.chess.engine.Alliance;
import han.chess.engine.pieces.Piece;
import org.carrot2.shaded.guava.common.collect.ImmutableMap;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile {

    protected static Point tileCoordinate;

    private static Map<Point,EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Point, EmptyTile> createAllPossibleEmptyTiles() {

        final Map<Point,EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.TILESIZE ; i++)
            for (int j = 0; i < BoardUtils.TILESIZE ; j++) {
                Point point = new Point(i, j);
                emptyTileMap.put(point, new EmptyTile(point));
            }

        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile(final Point point, final Piece piece){
        return piece != null ? new OccupiedTile(point,piece) : EMPTY_TILES_CACHE.get(point);
    }

    Tile(Point point){
        this.tileCoordinate = point;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile{

        private EmptyTile(final Point point ){
            super(point);
        }

        @Override
        public String toString(){
            return "-";
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

        private OccupiedTile(final Point point,final Piece piece){
            super(point);
            pieceOnTile = piece;
        }
        @Override
        public String toString(){
            String backWhite = "\u001B[47m";
            String colorBlack = "\u001B[30m";
            String reset = "\u001B[0m";
            return this.getPiece().getPieceAlliance()== Alliance.WHITE ? this.getPiece().toString():
                    backWhite+colorBlack+this.getPiece().toString()+reset;
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
