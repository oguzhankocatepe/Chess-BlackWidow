package han.chess;

import han.chess.engine.board.Board;
import han.chess.gui.Table;

public class HANChess {
    public static void main(String[] args){
        Board board = Board.createStandardBoard();
        System.out.println(board);
        Table table =  new Table();
    }
}
