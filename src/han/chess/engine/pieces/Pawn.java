package han.chess.engine.pieces;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.BoardUtils;
import han.chess.engine.board.Move;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Pawn extends Piece{
    private final static Point[] candidates = { new Point(0,1), new Point(0,2), new Point(-1,1), new Point(1,1)};

    public Pawn(Point position, Alliance alliance) {
        super(PieceType.PAWN,position, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        java.util.List<Move> legalMoves = new ArrayList<Move>();
        Point destination = new Point();
        for (final Point p:candidates) {
            destination.x = piecePosition.x + this.getPieceAlliance().getDirection() * p.x;
            destination.y = piecePosition.y + this.getPieceAlliance().getDirection() * p.y;

            if (!BoardUtils.checkValid(destination))
                continue;

            if (p == candidates[0] && !board.getTile(destination).isTileOccupied()) {
                // TODO : Promotions
                legalMoves.add(new Move.PawnMove(board, this, destination));
                //this.setFirstMove(false);
            }
            else if (p == candidates[1] && //this.isFirstMove() &&
                    ((this.piecePosition.y == 1 && this.pieceAlliance == Alliance.WHITE) ||
                     (this.piecePosition.y == 6 && this.pieceAlliance == Alliance.BLACK) )){
                Point betweenPoint = new Point(piecePosition.x,piecePosition.y-1*getPieceAlliance().getDirection());
                if (!board.getTile(destination).isTileOccupied() && !board.getTile(betweenPoint).isTileOccupied()) {
                    legalMoves.add(new Move.PawnMove(board, this, destination));
                    //this.setFirstMove(false);
                }
            }
            else if (p == candidates[2] ||p == candidates[3]){
                if (board.getTile(destination).isTileOccupied()){
                    final Piece piece = board.getTile(destination).getPiece();
                    if (this.getPieceAlliance() != piece.getPieceAlliance())
                        // TODO : Promotions
                        legalMoves.add(new Move.AttackMove(board,this,destination,piece));
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
}
