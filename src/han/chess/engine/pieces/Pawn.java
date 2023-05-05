package han.chess.engine.pieces;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.BoardUtils;
import han.chess.engine.board.Move;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static han.chess.engine.board.Move.*;

public class Pawn extends Piece{
    private final static Point[] candidates = { new Point(0,1), new Point(0,2), new Point(-1,1), new Point(1,1)};

    public Pawn(final Point position,final  Alliance alliance) {
        super(PieceType.PAWN,position, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<Move>();
        for (final Point p:candidates) {
            Point destination = new Point(piecePosition.x + getPieceAlliance().getDirection() * p.x,piecePosition.y + getPieceAlliance().getDirection() * p.y);
            if (!BoardUtils.checkValid(destination))
                continue;

            if (p == candidates[0] && !board.getTile(destination).isTileOccupied()) {
                if((getPieceAlliance()==Alliance.WHITE &&  destination.y == BoardUtils.TILESIZE-1) ||
                   (getPieceAlliance()==Alliance.BLACK &&  destination.y == 0)) {
                    legalMoves.add(new PawnPromotion(new PawnMove(board,this,destination)));
                }else {
                    legalMoves.add(new PawnMove(board, this, destination));
                }
            }
            else if (p == candidates[1] && this.isFirstMove() &&
                    ((this.piecePosition.y == 1 && this.pieceAlliance == Alliance.WHITE) ||
                     (this.piecePosition.y == 6 && this.pieceAlliance == Alliance.BLACK) )){
                Point betweenPoint = new Point(piecePosition.x,piecePosition.y+getPieceAlliance().getDirection());
                if (!board.getTile(destination).isTileOccupied() && !board.getTile(betweenPoint).isTileOccupied()) {
                    legalMoves.add(new PawnJump(board, this, destination));
                }
            }
            else if (p == candidates[2] ||p == candidates[3]){
                if (board.getTile(destination).isTileOccupied()){
                    final Piece piece = board.getTile(destination).getPiece();
                    if (this.getPieceAlliance() != piece.getPieceAlliance())
                        if((getPieceAlliance()==Alliance.WHITE &&  destination.y == BoardUtils.TILESIZE-1) ||
                           (getPieceAlliance()==Alliance.BLACK &&  destination.y == 0)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board,this,destination, piece)));
                        }else {
                            legalMoves.add(new PawnAttackMove(board, this, destination, piece));
                        }
                }
                else {
                    if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition().x == destination.x &&
                            board.getEnPassantPawn().getPieceAlliance() != getPieceAlliance() &&
                            board.getEnPassantPawn().getPiecePosition().y == piecePosition.y &&
                            Math.abs(board.getEnPassantPawn().getPiecePosition().x - piecePosition.x) == 1) {
                        legalMoves.add(new PawnEnPassantAttackMove(board, this, destination, board.getEnPassantPawn()));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        Pawn pawn = new Pawn(move.getDestination(),move.getMovedPiece().getPieceAlliance());
        pawn.setFirstMove(false);
        return pawn;
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece() {
        return  new Queen(piecePosition,pieceAlliance);
    }
}
