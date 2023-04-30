package han.chess.engine.pieces;

import han.chess.engine.Alliance;
import han.chess.engine.board.*;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static han.chess.engine.board.Move.*;

public class Knight extends Piece {

    private final static Point[] candidates = { new Point (2,1), new Point(1,2), new Point(-1,2) , new Point(-2,1) ,
                                                new Point (-2,-1), new Point(-1,-2), new Point(1,-2) , new Point(2,-1)};

    public Knight(Point position, Alliance alliance) {
        super(PieceType.KNIGHT,position, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        List<Move> legalMoves = new ArrayList<Move>();
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
        return PieceType.KNIGHT.toString();
    }
}
