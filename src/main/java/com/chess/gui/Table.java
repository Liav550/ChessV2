package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private static final Dimension OUTER_FRAME_DIMENTION = new Dimension(600,600);
    private static final Dimension BOARD_PANEL_DIMENTION = new Dimension(400,350);
    private static final Dimension TILE_PANEL_DIMENTION = new Dimension(10,10);
    private static final Color LIGHT_TILE_COLOR = new Color(229, 172, 172);
    private static final Color DARK_TILE_COLOR = new Color(128, 66, 66);

    public Table() {
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setSize(OUTER_FRAME_DIMENTION);

        JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);

        this.boardPanel = new BoardPanel();
        this.gameFrame.add(boardPanel,BorderLayout.CENTER);

        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("file");
        JMenuItem openPGN = new JMenuItem("Load PGN file");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("OPEN THAT PGN"); // todo - when I get to pgn
            }
        });
        fileMenu.add(openPGN);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
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

            setPreferredSize(BOARD_PANEL_DIMENTION);
            validate();
        }
    }

    private class TilePanel extends JPanel{
        private final int tileId;
        TilePanel(BoardPanel boardPanel, int tileId){
            super(new GridBagLayout());
            this.tileId= tileId;
            setPreferredSize(TILE_PANEL_DIMENTION);
            assignTileColor();
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
            String pieceIconPath = "";
            try {
                BufferedImage image = ImageIO.read(new File(pieceIconPath + board.getTile(tileId).getPiece().
                        getPieceAlliance().toString().substring(0,1) +
                        board.getTile(tileId).getPiece().toString() +
                        ".gif "));
                add(new JLabel(new ImageIcon(image)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
