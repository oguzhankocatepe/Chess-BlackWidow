package han.chess.engine.player;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.Move;
import han.chess.engine.board.Tile;
import han.chess.engine.pieces.Piece;
import han.chess.engine.pieces.Rook;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player{
    public WhitePlayer(final Board board,final Collection<Move> whiteMoves, final Collection<Move> blackMoves) {
        super(board,whiteMoves,blackMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<Move>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()){
            if(!this.board.getTile(new Point(5,0)).isTileOccupied() &&
               !this.board.getTile(new Point(6,0)).isTileOccupied()){
               final Tile rookTile = this.board.getTile(new Point(7,0));
               if (rookTile.isTileOccupied() && rookTile.getPiece().getPieceType()== Piece.PieceType.ROOK && rookTile.getPiece().isFirstMove()){
                   if (Player.calculateAttacksOnTile(new Point(5,0),opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTile(new Point(6,0),opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTile(new Point(7,0),opponentLegals).isEmpty() ) {
                       kingCastles.add(new Move.KingSideCastleMove(board, playerKing, new Point(6, 0),(Rook)rookTile.getPiece(),rookTile.getTileCoordinate(), new Point(5, 0)));
                   }
               }
            }
            if(!this.board.getTile(new Point(3,0)).isTileOccupied() &&
               !this.board.getTile(new Point(2,0)).isTileOccupied() &&
               !this.board.getTile(new Point(1,0)).isTileOccupied() ){
                final Tile rookTile = this.board.getTile(new Point(0,0));
                if (rookTile.isTileOccupied() && rookTile.getPiece().getPieceType()== Piece.PieceType.ROOK && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttacksOnTile(new Point(3,0),opponentLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(new Point(2,0),opponentLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(new Point(1,0),opponentLegals).isEmpty() ) {
                        kingCastles.add(new Move.QueenSideCastleMove(board,playerKing,new Point(2,0),(Rook)rookTile.getPiece(),rookTile.getTileCoordinate(), new Point(3, 0)));
                    }
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
