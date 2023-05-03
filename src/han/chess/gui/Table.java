package han.chess.gui;

import han.chess.engine.board.Board;
import han.chess.engine.board.BoardUtils;
import han.chess.engine.board.Move;
import han.chess.engine.board.Tile;
import han.chess.engine.pieces.Piece;
import han.chess.engine.player.MoveStatus;
import han.chess.engine.player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");
    private final static String defaultPieceImagesPath = "art/simple/";


    public Table(){
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(boardPanel,BorderLayout.CENTER);
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
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGNMenuItem = new JMenuItem("Open PGN File");
        openPGNMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open PGN");
            }
        });
        fileMenu.add(openPGNMenuItem);
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        return fileMenu;
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
            for (int i = BoardUtils.TILESIZE-1; i >=0 ; i--) {
                for (int j = 0; j < BoardUtils.TILESIZE; j++) {
                    final TilePanel tilePanel = boardTiles[i][j];
                    tilePanel.drawTile(board);
                    add(tilePanel);
                }
            }
            validate();
            repaint();
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
                            final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                            if (transition.getMoveStatus() == MoveStatus.DONE){
                                chessBoard = transition.getBoard();
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
                    SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
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
        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if (board.getTile(tileId).isTileOccupied()){
                try {
                    File file = new File(defaultPieceImagesPath+board.getTile(tileId).getPiece().getPieceAlliance().toString().charAt(0)+
                            board.getTile(tileId).getPiece().toString()+".gif");
                    final BufferedImage image = ImageIO.read(file);
                    add(new JLabel(new ImageIcon(image)));
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
            validate();
            repaint();
        }
    }
}
