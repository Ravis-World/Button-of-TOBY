package com.ravi;

import javax.swing.SwingUtilities;

import com.ravi.gui.MainWindow;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}