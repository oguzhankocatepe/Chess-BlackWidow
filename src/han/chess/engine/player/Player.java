package han.chess.engine.player;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.Move;
import han.chess.engine.pieces.King;
import han.chess.engine.pieces.Piece;
import org.carrot2.shaded.guava.common.collect.ImmutableList;
import org.carrot2.shaded.guava.common.collect.Iterables;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves,opponentMoves;
    private final boolean isCastled = false;

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,Collection<Move> opponentLegals);

    Player(final Board board, final Collection<Move> legalMoves,final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        this.opponentMoves = opponentMoves;
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,calculateKingCastles(legalMoves,opponentMoves)));
    }

    protected static Collection<Move> calculateAttacksOnTile(final Point p,final Collection<Move> moves) {
        final List<Move> attacks = new ArrayList<Move>();
        for (final Move move:moves)
            if(move.getDestination().equals(p))
                attacks.add(move);
        return ImmutableList.copyOf(attacks);
    }

    public King getPlayerKing() {
        return playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return legalMoves;
    }

    private King establishKing() {
        for (Piece piece: getActivePieces()) {
            if (piece.getPieceType() == Piece.PieceType.KING)
                return (King)piece;
        }
        throw new RuntimeException("No King!");
    }

    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }

    protected boolean hasEscapeMoves() {
        for (final Move move: legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus()==MoveStatus.DONE)
                return true;
        }
        return false;
    }

    public boolean isInCheck(){
        return !Player.calculateAttacksOnTile(playerKing.getPiecePosition(),opponentMoves).isEmpty();
    }

    public boolean isInCheckMate(){
        return isInCheck() && !hasEscapeMoves();
    }

    public boolean isInStaleMate(){
        return !isInCheck() && !hasEscapeMoves();
    }

    public boolean isCastled(){
        return isCastled;
    }

    public MoveTransition makeMove(final Move move){
        if (!isMoveLegal(move))
            return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVE);

        final Board transitionBoard = move.execute();
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.getCurrentPlayer().getLegalMoves());

        if(!kingAttacks.isEmpty())
            return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        if (move instanceof Move.KingSideCastleMove){
            if(!Player.calculateAttacksOnTile(new Point(move.getDestination().x-1,move.getDestination().y), transitionBoard.getCurrentPlayer().getLegalMoves()).isEmpty() ||
               !Player.calculateAttacksOnTile(new Point(move.getDestination().x-2,move.getDestination().y), transitionBoard.getCurrentPlayer().getLegalMoves()).isEmpty())
                return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        if (move instanceof Move.QueenSideCastleMove){
            if(!Player.calculateAttacksOnTile(new Point(move.getDestination().x+1,move.getDestination().y), transitionBoard.getCurrentPlayer().getLegalMoves()).isEmpty() ||
               !Player.calculateAttacksOnTile(new Point(move.getDestination().x+2,move.getDestination().y), transitionBoard.getCurrentPlayer().getLegalMoves()).isEmpty() ||
               !Player.calculateAttacksOnTile(new Point(move.getDestination().x-1,move.getDestination().y), transitionBoard.getCurrentPlayer().getLegalMoves()).isEmpty())
                return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard,move, MoveStatus.DONE);
    }
}
