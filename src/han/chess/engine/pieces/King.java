package han.chess.engine.pieces;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.BoardUtils;
import han.chess.engine.board.Move;
import han.chess.engine.board.Move.AttackMove;
import han.chess.engine.board.Move.MajorMove;
import han.chess.engine.board.Tile;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.awt.*;
import java.util.*;

public class King extends Piece{

    private final static Point[] candidates = { new Point (0,1), new Point(-1,0), new Point(1,0) , new Point(0,-1),
            new Point (1,1), new Point(-1,1), new Point(-1,-1) , new Point(1,-1)};

    public King(Point position, Alliance alliance) {
        super(position, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        java.util.List<Move> legalMoves = new ArrayList<Move>();
        for (final Point p:candidates) {
            final Point destination = new Point();
            destination.x = piecePosition.x + p.x;
            destination.y = piecePosition.y + p.y;

            if (BoardUtils.checkValid(destination)) { // valid Coordinate
                final Tile candidateTile = board.getTile(destination);
                if (!candidateTile.isTileOccupied())
                    legalMoves.add(new MajorMove(board,this,destination));
                else {
                    final Piece piece = candidateTile.getPiece();
                    if (this.getPieceAlliance() != piece.getPieceAlliance())
                        legalMoves.add(new AttackMove(board,this,destination,piece));
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }
}
