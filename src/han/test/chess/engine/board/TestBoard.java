package han.test.chess.engine.board;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.pieces.King;
import han.chess.engine.pieces.Pawn;
import han.chess.engine.player.MoveTransition;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static han.chess.engine.board.Board.*;
import static han.chess.engine.board.Move.*;
import static org.junit.jupiter.api.Assertions.*;

class TestBoard {
    @Test
    public void initialBoard() {

        final Board board = createStandardBoard();
        assertEquals(board.getCurrentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.getCurrentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().isCastled());
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isCastled());
    }

    @Test
    public void testPlainKingMove() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(new Point(4,7),Alliance.BLACK));
        builder.setPiece(new Pawn(new Point(4,6),Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(new Point(4,1),Alliance.WHITE));
        builder.setPiece(new King(new Point(4,0),Alliance.WHITE));
        builder.setMoveMaker(Alliance.WHITE);
        // Set the current player
        final Board board = builder.build();
        System.out.println(board);

        assertEquals(board.getWhitePlayer().getLegalMoves().size(), 6);
        assertEquals(board.getBlackPlayer().getLegalMoves().size(), 6);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());

    }

    @Test
    public void testBoardConsistency() {
        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());

        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, new Point(4,1),new Point(4,3)));

        final MoveTransition t2 = t1.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getBoard(), new Point(4,6),new Point(4,4)));

        final MoveTransition t3 = t2.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getBoard(),  new Point(6,0),new Point(5,2)));
        final MoveTransition t4 = t3.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getBoard(),  new Point(3,6),new Point(3,4)));

        final MoveTransition t5 = t4.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.getBoard(),  new Point(4,3),new Point(3,4)));
        final MoveTransition t6 = t5.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.getBoard(),  new Point(3,7),new Point(3,4)));

        final MoveTransition t7 = t6.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.getBoard(),  new Point(5,2),new Point(6,4)));
        final MoveTransition t8 = t7.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.getBoard(),  new Point(5,6),new Point(5,5)));

        final MoveTransition t9 = t8.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.getBoard(),  new Point(3,0),new Point(7,4)));
        final MoveTransition t10 = t9.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.getBoard(),  new Point(6,6),new Point(6,5)));

        final MoveTransition t11 = t10.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.getBoard(),  new Point(7,4),new Point(7,3)));
        final MoveTransition t12 = t11.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.getBoard(),  new Point(5,5),new Point(6,4)));

        final MoveTransition t13 = t12.getBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t12.getBoard(),  new Point(7,3),new Point(6,4)));

        System.out.println(t13.getBoard());
        assertTrue(t13.getBoard().getWhitePlayer().getActivePieces().size() == 14);
        assertTrue(t13.getBoard().getBlackPlayer().getActivePieces().size() == 14);
    }
}