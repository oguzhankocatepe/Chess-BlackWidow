package han.chess.engine.player;

import han.chess.engine.board.Board;
import han.chess.engine.board.Move;

public class MoveTransition {

    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board board, final Move move, final MoveStatus moveStatus){
        this.transitionBoard = board;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return moveStatus;
    }

    public Board getBoard() {
        return transitionBoard;
    }
}
