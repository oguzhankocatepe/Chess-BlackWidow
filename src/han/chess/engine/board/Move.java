package han.chess.engine.board;

import han.chess.engine.pieces.Piece;

import java.awt.*;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final Point destination;

    private Move(final Board board, final Piece piece, final Point point){
        this.board = board;
        this.movedPiece = piece;
        this.destination = point;
    }

    public Point getDestination() {
        return destination;
    }

    public abstract Board execute();

    public static final class MajorMove extends Move{

        public MajorMove(final Board board,final Piece piece,final Point point) {
            super(board, piece, point);
        }

        @Override
        public Board execute() {
            return null;
        }
    }

    public static final class AttackMove extends Move{

        final Piece attackedPiece;

        public AttackMove(final Board board,final Piece piece,final Point point,final Piece apiece) {
            super(board, piece, point);
            this.attackedPiece = apiece;
        }
        @Override
        public Board execute() {
            return null;
        }
    }

    public static final class PawnMove extends Move {
        public PawnMove(Board board, Piece piece, Point point) {
            super(board, piece, point);
        }
        @Override
        public Board execute() {
            return null;
        }
    }
}
