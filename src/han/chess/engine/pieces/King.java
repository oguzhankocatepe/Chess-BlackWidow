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

public class King extends Piece{

    private final static Point[] candidates = { new Point (0,1), new Point(-1,0), new Point(1,0) , new Point(0,-1),
            new Point (1,1), new Point(-1,1), new Point(-1,-1) , new Point(1,-1), new Point(2,0), new Point(-2,0)};

    public King(final Point position,final  Alliance alliance) {
        super(PieceType.KING, position, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<Move>();
        for (final Point p:candidates) {
            final Point destination = new Point(piecePosition.x + p.x,piecePosition.y + p.y);
            if (BoardUtils.checkValid(destination)) { // valid Coordinate
                final Tile candidateTile = board.getTile(destination);
                if (!candidateTile.isTileOccupied()){
                    if (p == candidates[8] ) {
                        if (this.getPieceAlliance() == Alliance.WHITE && piecePosition.equals(new Point(4,0))) {
                            Point betweenPoint = new Point(piecePosition.x + 1, piecePosition.y);
                            Piece rook = board.getTile(new Point(7,0)).getPiece();
                            if (rook instanceof Rook && BoardUtils.checkValid(betweenPoint) )
                                if (!board.getTile(betweenPoint).isTileOccupied() && rook.isFirstMove && this.isFirstMove()) {
                                    legalMoves.add(new KingSideCastleMove(board, this, destination, (Rook)rook, new Point(7, 0), new Point(5, 0)));
                            }
                        }else if (this.getPieceAlliance() == Alliance.BLACK && piecePosition.equals(new Point(4,7))) {
                            Point betweenPoint = new Point(piecePosition.x + 1, piecePosition.y);
                            Piece rook = board.getTile(new Point(7,7)).getPiece();
                            if (rook instanceof Rook && BoardUtils.checkValid(betweenPoint) )
                                if (!board.getTile(betweenPoint).isTileOccupied() && rook.isFirstMove && this.isFirstMove()) {
                                    legalMoves.add(new KingSideCastleMove(board, this, destination, (Rook)rook, new Point(7, 7), new Point(5, 7)));
                            }
                        }
                    } else if (p == candidates[9]) {
                        if (this.getPieceAlliance() == Alliance.WHITE && piecePosition.equals(new Point(4,0))) {
                            Point betweenPoint = new Point(piecePosition.x -  1, piecePosition.y);
                            Point betweenPoint2 = new Point(piecePosition.x -  3, piecePosition.y);
                            Piece rook = board.getTile(new Point(0,0)).getPiece();
                            if (rook instanceof Rook && BoardUtils.checkValid(betweenPoint) && BoardUtils.checkValid(betweenPoint2))
                                if (!board.getTile(betweenPoint).isTileOccupied() && !board.getTile(betweenPoint2).isTileOccupied() && rook.isFirstMove && this.isFirstMove()) {
                                    legalMoves.add(new QueenSideCastleMove(board, this, destination, (Rook)rook, new Point(0, 0), new Point(3, 0)));
                            }
                        }else if (this.getPieceAlliance() == Alliance.BLACK && piecePosition.equals(new Point(4,7))) {
                            Point betweenPoint = new Point(piecePosition.x -  1, piecePosition.y);
                            Point betweenPoint2 = new Point(piecePosition.x -  3, piecePosition.y);
                            Piece rook = board.getTile(new Point(0,7)).getPiece();
                            if (rook instanceof Rook && BoardUtils.checkValid(betweenPoint) && BoardUtils.checkValid(betweenPoint2))
                                if (!board.getTile(betweenPoint).isTileOccupied() && !board.getTile(betweenPoint2).isTileOccupied() && rook.isFirstMove && this.isFirstMove()) {
                                    legalMoves.add(new QueenSideCastleMove(board, this, destination, (Rook)rook, new Point(0, 7), new Point(3, 7)));
                            }
                        }
                    } else {
                        legalMoves.add(new MajorMove(board, this, destination));
                    }
                }
                else  if (candidateTile.isTileOccupied()){
                    final Piece piece = candidateTile.getPiece();
                    if (this.getPieceAlliance() != piece.getPieceAlliance())
                        legalMoves.add(new AttackMove(board,this,destination,piece));
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public King movePiece(Move move) {
        King king = new King(move.getDestination(),move.getMovedPiece().getPieceAlliance());
        king.setFirstMove(false);
        return king;
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }
}
