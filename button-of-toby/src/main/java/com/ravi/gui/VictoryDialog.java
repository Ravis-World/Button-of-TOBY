package com.ravi.gui;

import javax.swing.*;
import java.awt.*;

/**
 * VictoryDialog renders the absurd Australian Government reward modal 
 * when the player successfully activates the Button of TOBY in Room 24.
 */
public class VictoryDialog extends JDialog {

    public VictoryDialog(JFrame owner) {
        super(owner, "★ TOBY RELEASED! MISSION ACCOMPLISHED ★", true);

        setSize(600, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(240, 248, 255));

        initComponents();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("BUTTON OF TOBY ACTIVATED!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(139, 0, 0));

        JLabel subtitleLabel = new JLabel("Tubular Oriented Blueprint.yaml Secured", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        subtitleLabel.setForeground(Color.DARK_GRAY);

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JTextArea messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageArea.setMargin(new java.awt.Insets(15, 15, 15, 15));
        messageArea.setText(
            "Congratulations Agent!\n\n" +
            "You have successfully navigated Dr. Γ's 24-room facility, bypassed all five security gates, " +
            "and retrieved the sacred TOBY (Tubular Oriented Blueprint.yaml) for the Commonwealth.\n\n" +
            "In accordance with official Australian Government emergency protocol, your bravery has been rewarded:\n\n" +
            "★ YOU HAVE BEEN APPOINTED SPEAKER OF THE HOUSE ★\n\n" +
            "Please report to Parliament House immediately to maintain order during Question Time."
        );

        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        JButton closeButton = new JButton("Accept Office & Exit");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeButton.setBackground(new Color(34, 139, 34));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}