package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.gui.Table.MoveLog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameHistoryPanel extends JPanel {
    private static final Dimension HISTORY_PANEL_DIMENSION = new Dimension(100,400);
    private final DataModel model;
    private final JScrollPane scroller;
    public GameHistoryPanel(){
        setLayout(new BorderLayout());
        this.model = new DataModel();
        JTable table = new JTable(model);
        this.scroller = new JScrollPane(table);
        this.scroller.setColumnHeaderView(table.getTableHeader());
        this.scroller.setPreferredSize(HISTORY_PANEL_DIMENSION);
        add(this.scroller,BorderLayout.CENTER);
        setVisible(true);
    }
    void redo(Board board, MoveLog gameHistory){
        int currentRow = 0;
        String moveText;
        this.model.clear();
        for(Move move: gameHistory.getMoves()){
            moveText = move.toString();
            if(move.getMovedPiece().getPieceAlliance().isWhite()){
                this.model.setValueAt(moveText, currentRow, 0);
            }
            else if(move.getMovedPiece().getPieceAlliance().isBlack()){
                this.model.setValueAt(moveText, currentRow,1);
                currentRow++;
            }
        }

        if(gameHistory.getMoves().size() > 0){
            Move lastMove = gameHistory.getMoves().get(gameHistory.size()-1);
            moveText = lastMove.toString();
            if(lastMove.getMovedPiece().getPieceAlliance().isWhite()){
                this.model.setValueAt(moveText + addCheckOrCheckMateSigns(board), currentRow, 0);
            }
            else if(lastMove.getMovedPiece().getPieceAlliance().isBlack()){
                this.model.setValueAt(moveText + addCheckOrCheckMateSigns(board), currentRow-1, 1);
            }
        }

        JScrollBar vertical = scroller.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private String addCheckOrCheckMateSigns(Board board) {
        if(board.getCurrentPlayer().isInCheckmate()){
            return "#";
        }
        if(board.getCurrentPlayer().isInCheck()){
            return "+";
        }
        return "";
    }

    public static class DataModel extends DefaultTableModel{
        private final List<Row> values;
        private static final String[] NAMES = {"White","Black"};
        DataModel(){
            values = new ArrayList<>();
        }
        public void clear(){
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount() {
            if(this.values == null){
                return 0;
            }
            return this.values.size();
        }

        @Override
        public int getColumnCount() {
            return NAMES.length;
        }

        @Override
        public Object getValueAt(int row, int column) {
            Row currentRow = this.values.get(row);
            if(column == 0){
                return currentRow.getWhiteMove();
            }
            if(column == 1){
                return currentRow.getBlackMove();
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            Row currentRow;
            if(row >= this.values.size()){
                currentRow = new Row();
                this.values.add(currentRow);
            }
            else{
                currentRow = this.values.get(row);
            }

            if(column == 0){
                currentRow.setWhiteMove((String) aValue);
                fireTableRowsInserted(row,row);
            }
            else if(column == 1){
                currentRow.setBlackMove((String) aValue);
                fireTableCellUpdated(row,column);
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Move.class;
        }

        @Override
        public String getColumnName(int columnIndex){
            return NAMES[columnIndex];
        }
    }
    private static class Row{
        private String whiteMove;
        private String blackMove;
        Row(){

        }
        public String getWhiteMove(){return this.whiteMove;}
        public String getBlackMove(){return this.blackMove;}
        public void setWhiteMove(String move){this.whiteMove=move;}
        public void setBlackMove(String move){this.blackMove=move;}
    }
}
