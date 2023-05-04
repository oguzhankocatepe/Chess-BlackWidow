package han.chess.gui;

import han.chess.engine.Alliance;
import han.chess.engine.board.Board;
import han.chess.engine.board.Move;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameHistoryPanel extends JPanel {
    private final DataModel model;
    private final JScrollPane scrollPane;
    private static final Dimension HISTORY_PANEL_DIMENSION = new Dimension(100,400);
    public GameHistoryPanel(){
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
        this.add(scrollPane,BorderLayout.CENTER);
        this.setVisible(true);
    }
    void redo(final Board board, final Table.MoveLog moveLog){
        int currentRow = 0;
        this.model.clear();
        for (final Move move : moveLog.getMoves()){
            final String moveText = move.toString();
            if (move.getMovedPiece().getPieceAlliance() == Alliance.WHITE)
                this.model.setValueAt(moveText,currentRow,0);
            else if (move.getMovedPiece().getPieceAlliance() == Alliance.BLACK) {
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }
        if (moveLog.getMoves().size() > 1  ){
            final Move lastMove = moveLog.getMoves().get(moveLog.getMoves().size()-1);
            final String moveText = lastMove.toString();
            if (lastMove.getMovedPiece().getPieceAlliance() == Alliance.WHITE)
                this.model.setValueAt(moveText+calculateCheckHash(board),currentRow,0);
            else if (lastMove.getMovedPiece().getPieceAlliance() == Alliance.BLACK)
                this.model.setValueAt(moveText+calculateCheckHash(board),currentRow-1,1);

        }
        final JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private String calculateCheckHash(final Board board) {
        if (board.getCurrentPlayer().isInCheckMate())
            return "#";
        else if (board.getCurrentPlayer().isInCheck())
            return "+";
        else return "";
    }

    private static class DataModel extends DefaultTableModel{
        private final List<Row> values;
        private static final String[] NAMES = {Alliance.WHITE.toString(), Alliance.BLACK.toString()};
        DataModel(){
            this.values = new ArrayList<>();
        }

        public void clear(){
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount(){
            if (this.values == null)
                return 0;
            return this.values.size();
        }
        @Override
        public int getColumnCount(){
            return NAMES.length;
        }

        @Override
        public Object getValueAt(final int row, final int column){
            final Row currentRow = values.get(row);
            if (column == 0)
                return currentRow.getWhiteMove();
            else if (column == 1)
                return currentRow.getBlackMove();
            else return null;
        }
        @Override
        public void setValueAt(final Object value,final int row,final int column){
            final Row currentRow;
            if (this.values.size() <= row){
                currentRow = new Row();
                this.values.add(currentRow);
            }else {
                currentRow = this.values.get(row);
            }
            if (column == 0){
                currentRow.setWhiteMove(value.toString());
                fireTableRowsInserted(row,row);
            }
            else if (column == 1) {
                currentRow.setBlackMove(value.toString());
                fireTableCellUpdated(row, column);
            }
        }

        @Override
        public Class<?> getColumnClass(final int column){
            return Move.class;
        }

        @Override
        public String getColumnName(final int column){
            return NAMES[column];
        }

        private static class Row{

            private String whiteMove;
            private String blackMove;
            public String getWhiteMove() {
                return whiteMove;
            }
            public String getBlackMove() {
                return blackMove;
            }
            public void setWhiteMove(String whiteMove) {
                this.whiteMove = whiteMove;
            }
            public void setBlackMove(String blackMove) {
                this.blackMove = blackMove;
            }
        }
    }

}
