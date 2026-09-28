package com.ravi.gui;

import com.ravi.game.Game;
import com.ravi.items.Item;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * InventoryPanel renders the player's visual inventory grid,
 * allowing item inspection and selection for podium puzzles.
 */
public class InventoryPanel extends JPanel {
    private final MainWindow mainWindow;
    private final Game game;

    private final DefaultListModel<Item> listModel;
    private final JList<Item> itemJList;
    private final JLabel selectedItemLabel;

    public InventoryPanel(MainWindow mainWindow, Game game) {
        this.mainWindow = mainWindow;
        this.game = game;

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Player Inventory"));

        listModel = new DefaultListModel<>();
        itemJList = new JList<>(listModel);
        itemJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemJList.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Initialize label before attached listeners reference it
        selectedItemLabel = new JLabel("Selected: None", SwingConstants.CENTER);
        selectedItemLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));

        itemJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Item selected = itemJList.getSelectedValue();
                mainWindow.setSelectedInventoryItem(selected);
                if (selected != null) {
                    selectedItemLabel.setText("Selected: " + selected.toString());
                } else {
                    selectedItemLabel.setText("Selected: None");
                }
            }
        });

        add(new JScrollPane(itemJList), BorderLayout.CENTER);
        add(selectedItemLabel, BorderLayout.SOUTH);
    }

    public void updateView() {
        Item previouslySelected = itemJList.getSelectedValue();
        listModel.clear();

        List<Item> items = game.getPlayer().getInventory().getItems();
        for (Item item : items) {
            listModel.addElement(item);
        }

        if (previouslySelected != null && items.contains(previouslySelected)) {
            itemJList.setSelectedValue(previouslySelected, true);
        } else {
            selectedItemLabel.setText("Selected: None");
            mainWindow.setSelectedInventoryItem(null);
        }
    }
}