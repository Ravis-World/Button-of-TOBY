package com.ravi.gui;

import javax.swing.*;
import java.awt.*;

public class TobyButtonWindow extends JDialog {
    public TobyButtonWindow(JFrame owner, Runnable onPress) {
        super(owner, "Button of TOBY", false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setResizable(false);

        JPanel content = new JPanel(new BorderLayout(8, 10));
        content.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        content.setBackground(new Color(245, 245, 240));

        JLabel heading = new JLabel("FINAL GATE SOLVED", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        content.add(heading, BorderLayout.NORTH);

        CircularButton button = new CircularButton();
        button.addActionListener(event -> {
            setVisible(false);
            onPress.run();
        });
        content.add(button, BorderLayout.CENTER);

        JLabel caption = new JLabel("Press to activate TOBY", SwingConstants.CENTER);
        caption.setFont(new Font("SansSerif", Font.PLAIN, 13));
        content.add(caption, BorderLayout.SOUTH);

        setContentPane(content);
        pack();
        setLocationRelativeTo(owner);
    }

    private static class CircularButton extends JButton {
        private CircularButton() {
            setPreferredSize(new Dimension(176, 176));
            setFocusable(true);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setToolTipText("Press the Button of TOBY");
            getAccessibleContext().setAccessibleName("Press the Button of TOBY");
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int diameter = Math.min(getWidth(), getHeight()) - 12;
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;
            boolean pressed = getModel().isArmed() && getModel().isPressed();

            g2.setColor(pressed ? new Color(125, 20, 28) : new Color(180, 32, 42));
            g2.fillOval(x, y, diameter, diameter);
            g2.setStroke(new BasicStroke(4));
            g2.setColor(new Color(85, 18, 22));
            g2.drawOval(x, y, diameter, diameter);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            drawCentred(g2, "Button of", getWidth() / 2, getHeight() / 2 - 8);
            g2.setFont(new Font("SansSerif", Font.BOLD, 24));
            drawCentred(g2, "TOBY", getWidth() / 2, getHeight() / 2 + 22);

            if (hasFocus()) {
                g2.setStroke(new BasicStroke(2));
                g2.setColor(new Color(255, 220, 110));
                g2.drawOval(x + 7, y + 7, diameter - 14, diameter - 14);
            }

            g2.dispose();
        }

        @Override
        public boolean contains(int x, int y) {
            double centreX = getWidth() / 2.0;
            double centreY = getHeight() / 2.0;
            double radius = Math.min(getWidth(), getHeight()) / 2.0;
            double offsetX = x - centreX;
            double offsetY = y - centreY;
            return offsetX * offsetX + offsetY * offsetY <= radius * radius;
        }

        private void drawCentred(Graphics2D graphics, String text, int centreX, int baselineY) {
            FontMetrics metrics = graphics.getFontMetrics();
            graphics.drawString(text, centreX - metrics.stringWidth(text) / 2, baselineY);
        }
    }
}