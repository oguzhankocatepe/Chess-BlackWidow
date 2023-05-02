package han.chess.gui;

import han.chess.engine.board.Board;
import han.chess.engine.board.BoardUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private final Board chessBoard;
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
    }
    private class TilePanel extends JPanel{
        private final Point tileId;
        TilePanel(final BoardPanel boardPanel, final Point tileId){
            super(new GridBagLayout());
            this.tileId = tileId;
            assignTilePieceIcon(chessBoard);
            setPreferredSize(TILE_PANEL_DIMENSION);
            assingTileColor();
            validate();
        }
        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if (board.getTile(tileId).isTileOccupied()){
                try {
                    File file = new File(defaultPieceImagesPath+board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0,1)+
                            board.getTile(tileId).getPiece().toString()+".gif");
                    final BufferedImage image = ImageIO.read(file);
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void assingTileColor() {
            setBackground((tileId.x+tileId.y) % 2 == 1 ? lightTileColor : darkTileColor);
        }
    }
}
