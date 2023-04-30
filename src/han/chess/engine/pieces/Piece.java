package han.chess.engine.pieces;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.Move;

import java.awt.Point;
import java.util.Collection;

public abstract class Piece {
    protected final Point piecePosition;
    protected final Alliance pieceAlliance;
    //protected boolean isFirstMove;

    Piece(final Point position, final Alliance alliance){
        this.piecePosition = position;
        this.pieceAlliance = alliance;
        //this.isFirstMove = true;
    }

    public Alliance getPieceAlliance(){
        return pieceAlliance;
    }
    public Point getPiecePosition() { return piecePosition;}
/*
    public boolean isFirstMove(){
        return isFirstMove;
    }

    public void setFirstMove(boolean m){
        isFirstMove = m;
    }
    */
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public enum PieceType{

        PAWN("P"),KNIGHT("N"),BISHOP("B"),ROOK("R"),QUEEN("Q"),KING("K");

        private String pieceName;

        PieceType(final String s){
            pieceName = s;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }
    }
}
