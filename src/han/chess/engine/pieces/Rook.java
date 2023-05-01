package han.chess.engine.pieces;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.BoardUtils;
import han.chess.engine.board.Move;
import han.chess.engine.board.Tile;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Rook extends Piece {
    private final static Point[] candidates = { new Point (0,1), new Point(-1,0), new Point(1,0) , new Point(0,-1)};

    public Rook(final Point position, final Alliance alliance) {
        super(PieceType.ROOK,position, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        java.util.List<Move> legalMoves = new ArrayList<Move>();
        for (final Point p: candidates) {
            final Point destination = piecePosition;
            while (BoardUtils.checkValid(destination)){
                destination.x += p.x;
                destination.y += p.y;
                if (BoardUtils.checkValid(destination)){
                    final Tile candidateTile = board.getTile(destination);
                    if (!candidateTile.isTileOccupied())
                        legalMoves.add(new Move.MajorMove(board,this,destination));
                    else {
                        final Piece piece = candidateTile.getPiece();
                        if (this.getPieceAlliance() != piece.getPieceAlliance())
                            legalMoves.add(new Move.AttackMove(board,this,destination,piece));
                        break;
                    }
                }
            }

        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getDestination(),move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }
}
