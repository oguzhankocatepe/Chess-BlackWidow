package han.chess.gui;

import han.chess.engine.board.Board;
import han.chess.engine.board.BoardUtils;
import han.chess.engine.board.Move;
import han.chess.engine.board.Tile;
import han.chess.engine.pieces.Piece;
import han.chess.engine.player.MoveStatus;
import han.chess.engine.player.MoveTransition;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final MoveLog moveLog;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(700,600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(500,350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");
    private final static String defaultPieceImagesPath = "art/simple/";

    public enum BoardDirection{
        NORMAL {
            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };
        abstract BoardDirection opposite();
    }


    public Table(){
        this.gameFrame = new JFrame("HANChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.moveLog = new MoveLog();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.highlightLegalMoves = true;
        this.gameFrame.add(boardPanel,BorderLayout.CENTER);
        this.gameFrame.add(takenPiecesPanel,BorderLayout.WEST);
        this.gameFrame.add(gameHistoryPanel,BorderLayout.EAST);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setVisible(true);
        gameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private JMenuBar createMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGNMenuItem = new JMenuItem("Open PGN File");
        openPGNMenuItem.addActionListener(e -> System.out.println("Open PGN"));
        fileMenu.add(openPGNMenuItem);
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(e -> {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();
        final JCheckBoxMenuItem highlightLegalMovesChk = new JCheckBoxMenuItem("Highlight Legal Moves",true);
        highlightLegalMovesChk.addActionListener(e -> highlightLegalMoves = highlightLegalMovesChk.getState());
        preferencesMenu.add(highlightLegalMovesChk);
        return preferencesMenu;
    }
    private class BoardPanel extends JPanel{
        final TilePanel[][] boardTiles;
        BoardPanel(){
            super(new GridLayout(BoardUtils.TILESIZE,BoardUtils.TILESIZE));
            this.boardTiles = new TilePanel[BoardUtils.TILESIZE][BoardUtils.TILESIZE];
            for (int i = BoardUtils.TILESIZE-1; i >=0 ; i--) {
                for (int j = 0; j < BoardUtils.TILESIZE; j++) {
                    final TilePanel tilePanel = new TilePanel(this,new Point(j,i));
                    this.boardTiles[i][j] = tilePanel;
                    add(tilePanel);
                }
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            if (boardDirection == BoardDirection.NORMAL)
                for (int i = BoardUtils.TILESIZE-1; i >=0 ; i--) {
                    for (int j = 0; j < BoardUtils.TILESIZE; j++) {
                        final TilePanel tilePanel = boardTiles[i][j];
                        tilePanel.drawTile(board);
                        add(tilePanel);
                    }
                }
            else
                for (int i = 0; i < BoardUtils.TILESIZE ; i++) {
                    for (int j = 0; j < BoardUtils.TILESIZE ; j++) {
                        final TilePanel tilePanel = boardTiles[i][j];
                        tilePanel.drawTile(board);
                        add(tilePanel);
                    }
                }
            validate();
            repaint();
        }
    }

    public static class MoveLog{

        private final List<Move> moves;
        MoveLog(){
            this.moves = new ArrayList<Move>();
        }
        public List<Move> getMoves() {
            return moves;
        }
        public void addMove(final Move move){
            this.moves.add(move);
        }
        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }
        public Move removeMove(int index){
            return this.moves.remove(index);
        }
        public void clear(){
            this.moves.clear();
        }
        public int size(){
            return this.moves.size();
        }
    }

    private class TilePanel extends JPanel{
        private final Point tileId;
        TilePanel(final BoardPanel boardPanel, final Point tileId){
            super(new GridBagLayout());
            this.tileId = tileId;
            assignTilePieceIcon(chessBoard);
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (isLeftMouseButton(e)){
                        if (sourceTile == null){
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null)
                                sourceTile = null;
                        }else{
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(chessBoard,sourceTile.getTileCoordinate(),destinationTile.getTileCoordinate());
                            if (move != Move.NULL_MOVE){
                                final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                                if (transition.getMoveStatus() == MoveStatus.DONE) {
                                    move.getMovedPiece().setFirstMove(false);
                                    chessBoard = transition.getBoard();
                                    moveLog.addMove(move);
                                }
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                    }else if (isRightMouseButton(e)){
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                    SwingUtilities.invokeLater(() -> {
                        boardPanel.drawBoard(chessBoard);
                        gameHistoryPanel.redo(chessBoard,moveLog);
                        takenPiecesPanel.redo(moveLog);
                    });
                }
                @Override
                public void mousePressed(final MouseEvent e) {}
                @Override
                public void mouseReleased(final MouseEvent e) {}
                @Override
                public void mouseEntered(final MouseEvent e) {}
                @Override
                public void mouseExited(final MouseEvent e) {}
            });
            validate();
        }

        private void highLightLegalMoves(final Board board){
            if (highlightLegalMoves){
                for (final Move move:getSelectedPieceLegalMoves(board)) {
                    if (move.getDestination().equals(tileId)) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }        
            }
        }

        private Collection<Move> getSelectedPieceLegalMoves(final Board board) {
            if (humanMovedPiece != null)
                return humanMovedPiece.calculateLegalMoves(board);
            return Collections.EMPTY_LIST;
        }


        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if (board.getTile(tileId).isTileOccupied()){
                try {
                    File file = new File(defaultPieceImagesPath+board.getTile(tileId).getPiece().getPieceAlliance().toString().charAt(0)+
                            board.getTile(tileId).getPiece().toString()+".gif");
                    add(new JLabel(new ImageIcon(ImageIO.read(file))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void assignTileColor() {
            setBackground((tileId.x+tileId.y) % 2 == 1 ? lightTileColor : darkTileColor);
        }

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highLightLegalMoves(board);
            validate();
            repaint();
        }
    }
}
