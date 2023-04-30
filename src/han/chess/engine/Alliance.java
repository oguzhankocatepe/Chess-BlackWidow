package han.chess.engine;

import han.chess.engine.player.BlackPlayer;
import han.chess.engine.player.Player;
import han.chess.engine.player.WhitePlayer;

public enum Alliance {
     WHITE{
         @Override
         public int getDirection(){
             return 1;
         }

         @Override
         public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
             return whitePlayer;
         }
     },BLACK{
        @Override
        public int getDirection(){
            return -1;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };

    public abstract int getDirection();

    public abstract Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer);

}
