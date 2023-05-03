package han.chess.engine.pieces;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.BoardUtils;
import han.chess.engine.board.Move;
import han.chess.engine.board.Tile;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static han.chess.engine.board.Move.*;

public class Rook extends Piece {
    private final static Point[] candidates = { new Point (0,1), new Point(-1,0), new Point(1,0) , new Point(0,-1)};

    public Rook(final Point position, final Alliance alliance) {    super(PieceType.ROOK,position, alliance);    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<Move>();
        for (final Point p: candidates) {
            Point destination = new Point(piecePosition);
            while (BoardUtils.checkValid(destination)){
                destination = new Point(destination.x + p.x,destination.y + p.y);
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
    public Rook movePiece(Move move) {
        return new Rook(move.getDestination(),move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }
}
