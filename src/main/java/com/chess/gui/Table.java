package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private static final String DEFAULT_PIECES_IMAGES_PATH = "piecesIcons/"; // in case we want to use different images in the future
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private boolean highlightLegals;
    private BoardDirection boardDirection;
    private final GameHistoryPanel gameHistoryPanel;
    //private final TakenPiecesPanel takenPiecesPanel;
    private final MoveLog moveLog;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(800,700);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private static final Color LIGHT_TILE_COLOR = new Color(229, 172, 172);
    private static final Color DARK_TILE_COLOR = new Color(128, 66, 66);

    public Table() {
        this.chessBoard = Board.createStandardBoard();
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);

        this.gameHistoryPanel = new GameHistoryPanel();
        this.moveLog = new MoveLog();
        this.gameFrame.add(gameHistoryPanel,BorderLayout.EAST);

        this.highlightLegals = false;

        JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);

        this.boardPanel = new BoardPanel();
        this.gameFrame.add(boardPanel,BorderLayout.CENTER);

        this.boardDirection = BoardDirection.NORMAL;

        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem openPGN = new JMenuItem("Load PGN file");
        openPGN.addActionListener(e -> {
            System.out.println("OPEN THAT PGN"); // todo - when I get to pgn
        });
        fileMenu.add(openPGN);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }
    private JMenu createPreferencesMenu(){
        JMenu preferencesMenu = new JMenu("Preferences");
        JMenuItem flipBoard = new JMenuItem("Flip Board");
        flipBoard.addActionListener(e -> {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });

        preferencesMenu.add(flipBoard);
        preferencesMenu.addSeparator();

        JCheckBoxMenuItem highLightLegalMovesOption = new JCheckBoxMenuItem("Highlight Legal Moves", false);
        highLightLegalMovesOption.addActionListener(e -> highlightLegals = highLightLegalMovesOption.isSelected());

        preferencesMenu.add(highLightLegalMovesOption);
        return preferencesMenu;
    }

    private class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;
        BoardPanel(){
            super(new GridLayout(8,8));

            this.boardTiles = new ArrayList<>();
            TilePanel currentPanel;
            for (int i = 0; i < BoardUtils.NUMBER_OF_TILES; i++) {
                currentPanel = new TilePanel(this,i);
                this.boardTiles.add(currentPanel);
                add(currentPanel);
            }

            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(Board board) {
            removeAll();
            for(TilePanel tilePanel: boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    private class TilePanel extends JPanel{
        private final int tileId;
        TilePanel(BoardPanel boardPanel, int tileId){
            super(new GridBagLayout());
            this.tileId= tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(isRightMouseButton(e)){
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                    else if(isLeftMouseButton(e)){
                        System.out.println("left on " + tileId);
                        if(sourceTile == null){
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if(humanMovedPiece == null){
                                sourceTile = null;
                            }
                        }
                        else{
                            destinationTile = chessBoard.getTile(tileId);
                            Move move = MoveFactory.createMove(chessBoard,
                                    sourceTile.getTileIndex(),
                                    destinationTile.getTileIndex());
                            MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()){
                                chessBoard = transition.getTransitionBoard();
                                System.out.println(chessBoard);
                                System.out.println(chessBoard.getCurrentPlayer().getAlliance().toString());
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                gameHistoryPanel.redo(chessBoard, moveLog);
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            validate();
        }

        private void assignTileColor() {
            for (int i = 0; i < 8; i++) {
                if(BoardUtils.isInRow(tileId, i)){
                    if(i % 2 == 0){
                        setBackground(tileId % 2 == 0? LIGHT_TILE_COLOR: DARK_TILE_COLOR);
                    }
                    else{
                        setBackground(tileId%2 != 0? LIGHT_TILE_COLOR: DARK_TILE_COLOR);
                    }
                    break;
                }
            }
        }
        private void assignTilePieceIcon(Board board){
            removeAll();
            if(!board.getTile(tileId).isTileOccupied()){
                return;
            }
            try {
                BufferedImage image = ImageIO.read(new File(DEFAULT_PIECES_IMAGES_PATH +
                        board.getTile(tileId).getPiece().
                        getPieceAlliance().toString().charAt(0) +
                        board.getTile(tileId).getPiece().toString() +
                        ".gif "));
                add(new JLabel(new ImageIcon(image)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        private void highlightLegalMoves(Board board){
            if(highlightLegals){
                for(Move m: getCurrentPieceLegalMoves(board)){
                    if(m.getDestinationIndex() == this.tileId){
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("piecesIcons/greenDot.png")))));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

        private Collection<Move> getCurrentPieceLegalMoves(Board board) {
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == chessBoard.getCurrentPlayer().getAlliance()){
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        public void drawTile(Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegalMoves(board);
            validate();
            repaint();
        }

        /*private Collection<Move> pieceLegalMoves(Board board) {
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()){
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        } todo - make sure it is not useful*/
    }

    public enum BoardDirection{
        NORMAL{
            @Override
            public BoardDirection opposite() {
                return FLIPPED;
            }

            @Override
            public List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }
        },
        FLIPPED{
            @Override
            public BoardDirection opposite() {
                return NORMAL;
            }
            @Override
            public List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }
        };
        public abstract BoardDirection opposite();
        public abstract List<TilePanel> traverse(List<TilePanel> boardTiles);
    }

    public static class MoveLog{
        private final List<Move> moves;
        public MoveLog(){
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves(){return this.moves;}
        public void addMove(Move move){
            this.moves.add(move);
        }
        public Move removeMove(int index){
            return this.moves.remove(index);
        }
        public boolean removeMove(Move move){
            return this.moves.remove(move);
        }

        public int size(){
            return this.moves.size();
        }
    }
}
