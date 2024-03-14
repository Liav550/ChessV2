package com.chess.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClockFrame extends JFrame{
    private JLabel timerLabel;
    private Timer timer;
    private int secondsRemaining;

    public ClockFrame(int minutes) {
        secondsRemaining = minutes * 60;

        timerLabel = new JLabel();
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        updateTimerLabel();

        getContentPane().add(timerLabel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle("Countdown Timer");
        setSize(300, 100);
        setLocationRelativeTo(null);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (secondsRemaining > 0) {
                    secondsRemaining--;
                    updateTimerLabel();
                } else {
                    timer.stop();
                    JOptionPane.showMessageDialog(ClockFrame.this, "Time's up!");
                }
            }
        });

        setVisible(true);
    }

    private void updateTimerLabel() {
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }
}
