package han.chess.engine.player;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.Move;
import han.chess.engine.pieces.Piece;

import java.util.Collection;

public class BlackPlayer extends Player{
    public BlackPlayer(final Board board, final Collection<Move> whiteMoves, final Collection<Move> blackMoves) {
        super(board,blackMoves,whiteMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }
}
