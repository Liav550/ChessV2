package com.chess.gui;

import com.chess.engine.player.Player;
import com.chess.engine.player.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameSetup extends JDialog {
    private static final String HUMAN_TEXT = "Human";
    private static final String COMPUTER_TEXT = "Computer";

    private PlayerType whitePlayerType;
    private PlayerType blackPlayerType;
    JSpinner depthSpinner;

    public GameSetup(JFrame frame, boolean modal){
        super(frame,modal);
        JPanel panel = new JPanel(new GridLayout(0,1));
        JRadioButton whiteHuman = new JRadioButton(HUMAN_TEXT);
        JRadioButton whiteComputer = new JRadioButton(COMPUTER_TEXT);
        JRadioButton blackHuman = new JRadioButton(HUMAN_TEXT); // going to get punished for this variable
        JRadioButton blackComputer = new JRadioButton(COMPUTER_TEXT);

        whiteHuman.setActionCommand(HUMAN_TEXT);

        ButtonGroup whiteButtonGroup = new ButtonGroup();
        whiteButtonGroup.add(whiteHuman);
        whiteButtonGroup.add(whiteComputer);
        whiteHuman.setSelected(true);

        ButtonGroup blackButtonGroup = new ButtonGroup();
        blackButtonGroup.add(blackHuman);
        blackButtonGroup.add(blackComputer);
        blackHuman.setSelected(true);

        getContentPane().add(panel);
        panel.add(new JLabel("White"));
        panel.add(whiteHuman);
        panel.add(whiteComputer);
        panel.add(new JLabel("Black"));
        panel.add(blackHuman);
        panel.add(blackComputer);

        panel.add(new JLabel("Search"));
        this.depthSpinner = addLabeledSpinner
                (panel, "Search Depth", new SpinnerNumberModel(6,1,6, 1));

        JButton cancelButton = new JButton("Cancel");
        JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                whitePlayerType = whiteHuman.isSelected()? PlayerType.HUMAN: PlayerType.AI;
                blackPlayerType = blackHuman.isSelected()? PlayerType.HUMAN: PlayerType.AI;
                GameSetup.this.setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel");
                GameSetup.this.setVisible(false);
            }
        });

        panel.add(okButton);
        panel.add(cancelButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }
    public void promptUser(){
        setVisible(true);
        repaint();
    }
     public boolean isAIPlayer(Player player){
        if(player.getAlliance().isWhite()){
            return this.whitePlayerType == PlayerType.AI;
        }
        return this.blackPlayerType == PlayerType.AI;
    }
    private static JSpinner addLabeledSpinner(Container c,
                                              String label,
                                              SpinnerModel model) {
        final JLabel l = new JLabel(label);
        c.add(l);
        final JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);
        return spinner;
    }
}
