package com.chess.gui;

import com.chess.login.DBOperations;
import entity.UserEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeFrame extends JFrame {
    private static final Color GOLD = new Color(211,175,55);
    private static final Color SILVER = new Color(192,192,192);
    private static final Color COPPER = new Color(184,115, 51);
    private UserEntity user;
    public WelcomeFrame(UserEntity u){
        super("Welcome "+ u.getUserName());

        this.user = u;
        JPanel leaderboardsPanel = createLeaderboardsPanel();
        JPanel gameSettingsPanel = createGameSettingsPanel();

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addTab("Create Game", gameSettingsPanel);
        tabbedPane.addTab("Leaderboards", leaderboardsPanel);


        JPanel mainPanel = new JPanel();
        mainPanel.add(tabbedPane);

        add(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private JPanel createGameSettingsPanel() {
        JPanel gameSettingsPanel = new JPanel();
        gameSettingsPanel.setBackground(Color.RED);
        gameSettingsPanel.setLayout(new BoxLayout(gameSettingsPanel, BoxLayout.Y_AXIS));

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1,1,10,1));
        JLabel label = new JLabel("Time control:");
        gameSettingsPanel.add(label);
        label.setLabelFor(spinner);
        gameSettingsPanel.add(spinner);

        JButton button = new JButton("Let's play!");
        gameSettingsPanel.add(button);

        button.setSize(100,50);
        button.addActionListener(e -> {
            dispose();
            Table table = Table.getInstance();
            ClockFrame clock = new ClockFrame((int)spinner.getValue());
            table.setClock(clock);
            clock.startTimer();
        });


        return gameSettingsPanel;
    }

    private JPanel createLeaderboardsPanel() {
        JPanel leaderboardsPanel = new JPanel();
        leaderboardsPanel.setBackground(Color.BLUE);
        leaderboardsPanel.setLayout(new BoxLayout(leaderboardsPanel, BoxLayout.Y_AXIS));

        Object[][] objects = DBOperations.getTopThreeLeaders();
        String[] names = {"Username","Points"};

        ReadOnlyModel readOnlyModel = new ReadOnlyModel();
        JTable table = new JTable();
        readOnlyModel.setDataVector(objects, names);
        table.setModel(readOnlyModel);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row == 0) {
                    component.setBackground(GOLD);
                }
                else if (row == 1) {
                    component.setBackground(SILVER);
                }
                else {
                    component.setBackground(COPPER);
                }
                return component;
            }
        };

        table.getColumnModel().getColumn(0).setCellRenderer(renderer);
        table.getColumnModel().getColumn(1).setCellRenderer(renderer);

        leaderboardsPanel.add(table);

        JLabel message = new JLabel(user.getUserName() + ", You currently have "+
                DBOperations.getTotalPointsById(user.getUserId()) + " points");
        leaderboardsPanel.add(message);

        return leaderboardsPanel;
    }

    private static class ReadOnlyModel extends DefaultTableModel{
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
