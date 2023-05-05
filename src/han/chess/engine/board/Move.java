package han.chess.engine.board;

import han.chess.engine.pieces.Pawn;
import han.chess.engine.pieces.Rook;
import han.chess.engine.pieces.Piece;

import java.awt.Point;

import static han.chess.engine.board.Board.*;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final Point destination;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece piece, final Point point){
        this.board = board;
        this.movedPiece = piece;
        this.destination = point;
    }
    @Override
    public int hashCode(){
        int result = 31 + this.movedPiece.getPiecePosition().hashCode();
        result = 31 * result + this.getDestination().hashCode();
        return result;
    }
    @Override
    public boolean equals(final Object other){
        if (this==other)
            return true;
        if (!(other instanceof Move))
                return false;
        final Move otherMove = (Move) other;
        return getCurrent() == otherMove.getCurrent() &&
                getDestination() == otherMove.getDestination() &&
                getMovedPiece().equals(((Move) other).getMovedPiece());
    }

    private Point getCurrent() {
        return movedPiece.getPiecePosition();
    }
    public Point getDestination() {
        return destination;
    }
    public Piece getMovedPiece() {
        return movedPiece;
    }
    public boolean isAttack(){ return false;}
    public boolean isCastlingMove(){ return false;}
    public Piece getAttackedPiece(){ return null;}


    public Board execute() {
        final Builder builder = new Builder();
        for (final Piece piece: this.board.getCurrentPlayer().getActivePieces())
            if (!this.movedPiece.equals(piece))
                builder.setPiece(piece);
        for (final Piece piece: this.board.getCurrentPlayer().getOpponent().getActivePieces())
            builder.setPiece(piece);
        builder.setPiece(movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public static final class MajorMove extends Move{
        public MajorMove(final Board board,final Piece piece,final Point point) {
            super(board, piece, point);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceType().toString()+BoardUtils.getAlgebraicNotation(getDestination());
        }
    }

    public static class AttackMove extends Move{
        final Piece attackedPiece;

        public AttackMove(final Board board,final Piece piece,final Point point,final Piece apiece) {
            super(board, piece, point);
            this.attackedPiece = apiece;
        }
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(final Object other){
            if (this == other)
                return true;
            if (!(other instanceof AttackMove))
                return false;
            return super.equals(other) && getAttackedPiece().equals(((AttackMove) other).getAttackedPiece());
        }
        @Override
        public boolean isAttack(){ return  true;}
        @Override
        public Piece getAttackedPiece(){ return this.attackedPiece;}
        @Override
        public String toString(){
            return movedPiece.getPieceType().toString()+"x"+BoardUtils.getAlgebraicNotation(getDestination());
        }
    }

    public final static class PawnMove extends Move {
        public PawnMove(final Board board,final Piece piece,final Point point) {
            super(board, piece, point);
        }
        @Override
        public String toString(){
            return BoardUtils.getAlgebraicNotation(getDestination());
        }
    }

    public final static class PawnJump extends Move {
        public PawnJump(final Board board,final Piece piece,final Point point) {
            super(board, piece, point);
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces())
                if (!this.movedPiece.equals(piece))
                    builder.setPiece(piece);
            for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces())
                builder.setPiece(piece);
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
        @Override
        public String toString(){
            return BoardUtils.getAlgebraicNotation(getDestination());
        }
    }

    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board, final Piece piece,final Point point,final Piece apiece) {
            super(board, piece, point, apiece);
        }

        @Override
        public String toString(){
            return BoardUtils.getAlgebraicNotation(getMovedPiece().getPiecePosition()).charAt(0)+"x"+BoardUtils.getAlgebraicNotation(getDestination());
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(final Board board, final Piece piece,final Point point,final Piece apiece) {
            super(board, piece, point, apiece);
        }
        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.getCurrentPlayer().getActivePieces())
                if (!this.movedPiece.equals(piece))
                    builder.setPiece(piece);
            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces())
                if (!this.attackedPiece.equals(piece))
                    builder.setPiece(piece);
            builder.setPiece(movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move{
        protected  final Rook castleRook;
        protected  final Point castleRookStart;
        protected  final Point castleRookEnd;
        public CastleMove(final Board board,final Piece piece,final Point point ,final Rook rook, final Point start,final Point end) {
            super(board, piece, point);
            this.castleRook = rook;
            this.castleRookStart = start;
            this.castleRookEnd = end;
        }
        public Rook getCastleRook() {
            return castleRook;
        }
        @Override
        public boolean isCastlingMove(){
            return  true;
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces())
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece))
                    builder.setPiece(piece);
            for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces())
                builder.setPiece(piece);
            movedPiece.setFirstMove(false);
            builder.setPiece(this.movedPiece.movePiece(this));
            Rook rook = new Rook(castleRookEnd,castleRook.getPieceAlliance());
            rook.setFirstMove(false);
            builder.setPiece(rook);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
        @Override
        public int hashCode(){
            int result = super.hashCode();
            result = 31* result +castleRook.hashCode();
            result = 31* result + castleRookEnd.hashCode();
            return result;
        }
    }

    public static final class KingSideCastleMove extends CastleMove{
        public KingSideCastleMove(final Board board,final Piece piece,final Point point,final Rook rook, final Point start,final Point end) {
            super(board, piece, point,rook,start,end);
        }
        @Override
        public String toString(){
            return "O-O";
        }
    }
    public static final class QueenSideCastleMove extends CastleMove{
        public QueenSideCastleMove(final Board board,final Piece piece,final Point point,final Rook rook, final Point start,final Point end) {
            super(board, piece, point,rook,start,end);
        }
        @Override
        public String toString(){
            return "O-O-O";
        }
    }

    public static final class NullMove extends Move{
        public NullMove() {
            super(null, null, null);
        }
        @Override
        public Board execute(){
            throw new RuntimeException("NULL MOVE");
        }
    }

    public static class MoveFactory{
        private MoveFactory(){
            throw new RuntimeException("No Instance");
        }
        public static  Move createMove(final Board board, final Point current, final Point destination){
            for (final Move move: board.getAllLegalMoves())
                if (move.getCurrent().equals(current) && move.getDestination().equals(destination))
                    return move;
            return NULL_MOVE;
        }
    }

}
