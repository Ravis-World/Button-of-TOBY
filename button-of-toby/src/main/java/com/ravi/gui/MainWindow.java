package com.ravi.gui;

import com.ravi.game.Game;
import com.ravi.items.Item;

import javax.swing.*;
import java.awt.*;

/**
 * MainWindow combines RoomPanel, InventoryPanel, and MapPanel
 * into a single unified escape room game window frame.
 */
public class MainWindow extends JFrame {
    private final Game game;
    private Item selectedInventoryItem;

    private RoomPanel roomPanel;
    private InventoryPanel inventoryPanel;
    private MapPanel mapPanel;

    public MainWindow() {
        this.game = new Game();

        setTitle("Button of TOBY - Digital Escape Room");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        refreshUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        roomPanel = new RoomPanel(this, game);
        inventoryPanel = new InventoryPanel(this, game);
        mapPanel = new MapPanel(game);

        // Right split container for Inventory and Map
        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mapPanel, inventoryPanel);
        rightSplit.setResizeWeight(0.5);

        // Main horizontal split container
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, roomPanel, rightSplit);
        mainSplit.setResizeWeight(0.65);

        add(mainSplit, BorderLayout.CENTER);

        // Status bar
        JLabel statusBar = new JLabel(" Target: Infiltrate Dr. Γ's facility and release TOBY | Current Position: Room "
                + game.getPlayer().getCurrentRoom().getId());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);
    }

    public void refreshUI() {
        roomPanel.updateView();
        inventoryPanel.updateView();
        mapPanel.updateView();
    }

    public Item getSelectedInventoryItem() {
        return selectedInventoryItem;
    }

    public void setSelectedInventoryItem(Item item) {
        this.selectedInventoryItem = item;
    }
}