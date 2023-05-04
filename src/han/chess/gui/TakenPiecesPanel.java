package han.chess.gui;

import han.chess.engine.Alliance;
import han.chess.engine.board.BoardUtils;
import han.chess.engine.board.Move;
import han.chess.engine.pieces.Piece;
import org.carrot2.shaded.guava.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TakenPiecesPanel extends JPanel {
    private final JPanel northPanel;
    private final JPanel southPanel;
    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(80,80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder();
    private final static String defaultPieceImagesPath = "art/holywarriors/";

    public TakenPiecesPanel(){
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(BoardUtils.TILESIZE, Alliance.values().length));
        this.southPanel = new JPanel(new GridLayout(BoardUtils.TILESIZE, Alliance.values().length));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel,BorderLayout.NORTH);
        this.add(this.southPanel,BorderLayout.SOUTH);
        this.setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final Table.MoveLog moveLog){
        northPanel.removeAll();
        southPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<Piece>();
        final List<Piece> blackTakenPieces = new ArrayList<Piece>();

        for (final Move move : moveLog.getMoves()){
            if (move.isAttack()){
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceAlliance() == Alliance.WHITE)
                    whiteTakenPieces.add(takenPiece);
                else
                    blackTakenPieces.add(takenPiece);
            }
        }
        Collections.sort(whiteTakenPieces, (o1, o2) -> Ints.compare(o1.getPieceValue(),o2.getPieceValue()));
        Collections.sort(blackTakenPieces, (o1, o2) -> Ints.compare(o1.getPieceValue(),o2.getPieceValue()));

        for (final Piece takenPiece : whiteTakenPieces) {
            try {
                File file = new File(defaultPieceImagesPath+takenPiece.getPieceAlliance().toString().charAt(0)+takenPiece.toString()+".gif");
                this.northPanel.add(new JLabel(new ImageIcon(ImageIO.read(file))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (final Piece takenPiece : blackTakenPieces) {
            try {
                File file = new File(defaultPieceImagesPath+takenPiece.getPieceAlliance().toString().charAt(0)+takenPiece.toString()+".gif");
                this.southPanel.add(new JLabel(new ImageIcon(ImageIO.read(file))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        validate();
    }
}
