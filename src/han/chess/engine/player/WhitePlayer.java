package han.chess.engine.player;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.Move;
import han.chess.engine.pieces.Piece;

import java.util.Collection;

public class WhitePlayer extends Player{
    public WhitePlayer(final Board board,final Collection<Move> whiteMoves, final Collection<Move> blackMoves) {
        super(board,whiteMoves,blackMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }
}
