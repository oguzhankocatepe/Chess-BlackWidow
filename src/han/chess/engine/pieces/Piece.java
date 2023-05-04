package han.chess.engine.pieces;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.BoardUtils;
import han.chess.engine.board.Move;

import java.awt.Point;
import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final Point piecePosition;
    protected final Alliance pieceAlliance;
    protected boolean isFirstMove;
    private final int cachedHashCode;


    Piece(final PieceType pieceType,final Point position, final Alliance alliance){
        this.pieceType = pieceType;
        this.piecePosition = position;
        this.pieceAlliance = alliance;
        this.isFirstMove = true;
        this.cachedHashCode = computeHashCode();
    }

    @Override
    public boolean equals(final Object other){
        if (this == other)
            return true;
        if (!(other instanceof Piece))
            return false;
        final Piece otherPiece = (Piece) other;
        return pieceAlliance.equals(otherPiece.pieceAlliance)
            && piecePosition.equals(otherPiece.piecePosition)
            && pieceType.equals(otherPiece.pieceType);
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition.x + piecePosition.y * BoardUtils.TILESIZE;
        return result;
    }
    @Override
    public int hashCode(){
        return cachedHashCode;
    }
    public Alliance getPieceAlliance(){
        return pieceAlliance;
    }
    public Point getPiecePosition() { return piecePosition;}

    public boolean isFirstMove(){
        return isFirstMove;
    }

    public void setFirstMove(boolean m){
        isFirstMove = m;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public int getPieceValue() {
            return this.pieceType.getPieceValue();
    }

    public enum PieceType{

        PAWN("P",100),KNIGHT("N",290),BISHOP("B",300),ROOK("R",500),QUEEN("Q",900),KING("K",10000);

        private final String pieceName;
        private final int pieceValue;

        PieceType(final String s,final int v){
            pieceName = s;
            pieceValue = v;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public int getPieceValue() {
            return this.pieceValue;
        }
    }
}
