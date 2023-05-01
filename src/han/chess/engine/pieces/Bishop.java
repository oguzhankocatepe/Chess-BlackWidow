package han.chess.engine.pieces;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.BoardUtils;
import han.chess.engine.board.Move;
import han.chess.engine.board.Tile;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.awt.*;
import java.util.*;

import static han.chess.engine.board.Move.*;

public class Bishop extends Piece {

    private final static Point[] candidates = { new Point (1,1), new Point(-1,1), new Point(-1,-1) , new Point(1,-1)};

    public Bishop(final Point position, final Alliance alliance) {
        super(PieceType.BISHOP,position, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        java.util.List<Move> legalMoves = new ArrayList<Move>();
        for (final Point p: candidates) {
            final Point destination = piecePosition;
            while (BoardUtils.checkValid(destination)){
                destination.x += p.x;
                destination.y += p.y;
                if (BoardUtils.checkValid(destination)){
                    final Tile candidateTile = board.getTile(destination);
                    if (!candidateTile.isTileOccupied())
                        legalMoves.add(new MajorMove(board,this,destination));
                    else {
                        final Piece piece = candidateTile.getPiece();
                        if (this.getPieceAlliance() != piece.getPieceAlliance())
                            legalMoves.add(new AttackMove(board,this,destination,piece));
                        break;
                    }
                }
            }

        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getDestination(),move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceType.BISHOP.toString();
    }
}
