package han.chess.engine.board;

import han.chess.engine.Alliance;
import han.chess.engine.pieces.*;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.awt.Point;
import java.util.*;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private Board (Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard,Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard,Alliance.BLACK);

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
    }

    @Override
    public String toString(){
        final StringBuilder sbuilder = new StringBuilder();
        for (int i=0; i < BoardUtils.TILESIZE * BoardUtils.TILESIZE ; i++){
            final String tileText =this.gameBoard.get(i).toString();
            sbuilder.append(String.format("%3s",tileText));
            if ((i+1) % BoardUtils.TILESIZE == 0)
                sbuilder.append("\n");
        }
        return sbuilder.toString();
    }


    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {

        final List<Move> legalMoves = new ArrayList<Move>();
        for (final Piece piece: pieces)
            legalMoves.addAll(piece.calculateLegalMoves(this));

        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, Alliance alliance) {
        List<Piece> activePieces = new ArrayList<Piece>();
        for (final Tile tile: gameBoard) {
            if (tile.isTileOccupied()){
                if(tile.getPiece().getPieceAlliance() == alliance)
                    activePieces.add(tile.getPiece());
            }
        }
        return  ImmutableList.copyOf(activePieces);
    }

    public Tile getTile(final Point destination) {
        return gameBoard.get(destination.y * BoardUtils.TILESIZE + destination.x);
    }

    private List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.TILESIZE * BoardUtils.TILESIZE];
        for (int i = 0 ;i < BoardUtils.TILESIZE; i++)
            for (int j = 0 ;j < BoardUtils.TILESIZE; j++) {
                Point p = new Point(i,j);
                tiles[j * BoardUtils.TILESIZE + i] = Tile.createTile(p,builder.boardConfig.get(p));
            }
            return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        // WHITE
        builder.setPiece(new Rook(new Point(0,0), Alliance.WHITE));
        builder.setPiece(new Knight(new Point(1,0), Alliance.WHITE));
        builder.setPiece(new Bishop(new Point(2,0), Alliance.WHITE));
        builder.setPiece(new King(new Point(3,0), Alliance.WHITE));
        builder.setPiece(new Queen(new Point(4,0), Alliance.WHITE));
        builder.setPiece(new Bishop(new Point(5,0), Alliance.WHITE));
        builder.setPiece(new Knight(new Point(6,0), Alliance.WHITE));
        builder.setPiece(new Rook(new Point(7,0), Alliance.WHITE));
        builder.setPiece(new Pawn(new Point(0,1), Alliance.WHITE));
        builder.setPiece(new Pawn(new Point(1,1), Alliance.WHITE));
        builder.setPiece(new Pawn(new Point(2,1), Alliance.WHITE));
        builder.setPiece(new Pawn(new Point(3,1), Alliance.WHITE));
        builder.setPiece(new Pawn(new Point(4,1), Alliance.WHITE));
        builder.setPiece(new Pawn(new Point(5,1), Alliance.WHITE));
        builder.setPiece(new Pawn(new Point(6,1), Alliance.WHITE));
        builder.setPiece(new Pawn(new Point(7,1), Alliance.WHITE));
        // BLACK
        builder.setPiece(new Rook(new Point(0,7), Alliance.BLACK));
        builder.setPiece(new Knight(new Point(1,7), Alliance.BLACK));
        builder.setPiece(new Bishop(new Point(2,7), Alliance.BLACK));
        builder.setPiece(new King(new Point(3,7), Alliance.BLACK));
        builder.setPiece(new Queen(new Point(4,7), Alliance.BLACK));
        builder.setPiece(new Bishop(new Point(5,7), Alliance.BLACK));
        builder.setPiece(new Knight(new Point(6,7), Alliance.BLACK));
        builder.setPiece(new Rook(new Point(7,7), Alliance.BLACK));
        builder.setPiece(new Pawn(new Point(0,6), Alliance.BLACK));
        builder.setPiece(new Pawn(new Point(1,6), Alliance.BLACK));
        builder.setPiece(new Pawn(new Point(2,6), Alliance.BLACK));
        builder.setPiece(new Pawn(new Point(3,6), Alliance.BLACK));
        builder.setPiece(new Pawn(new Point(4,6), Alliance.BLACK));
        builder.setPiece(new Pawn(new Point(5,6), Alliance.BLACK));
        builder.setPiece(new Pawn(new Point(6,6), Alliance.BLACK));
        builder.setPiece(new Pawn(new Point(7,6), Alliance.BLACK));

        builder.setMoveMaker(Alliance.WHITE);
        return builder.build();
    }

    public static class Builder{

        Map<Point,Piece> boardConfig;
        Alliance nextMoveMaker;

        public Builder(){
            boardConfig = new HashMap<Point,Piece>();
        }

        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition() , piece);
            return this;
        }

        public Builder setMoveMaker(final Alliance next){
            this.nextMoveMaker = next;
            return this;
        }

        public Board build(){
            return new Board(this);
        }
    }
}
