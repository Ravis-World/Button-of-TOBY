package com.ravi.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.ravi.game.Game;
import com.ravi.game.Player;
import com.ravi.items.Item;
import com.ravi.puzzles.Podium;
import com.ravi.world.Room;

public class RoomPanel extends JPanel {
    private final MainWindow mainWindow;
    private final Game game;

    private final JLabel roomTitleLabel;
    private final JTextArea descriptionArea;
    private final JLabel podiumInfoLabel;
    private final JLabel slotStatusLabel;

    private final JButton northButton;
    private final JButton southButton;
    private final JButton eastButton;
    private final JButton westButton;
    private final JButton placeItemButton;
    private final JButton clearPodiumButton;

    public RoomPanel(MainWindow mainWindow, Game game) {
        this.mainWindow = mainWindow;
        this.game = game;

        setLayout(new BorderLayout(10, 10));

        // Top Header
        roomTitleLabel = new JLabel("Room", SwingConstants.CENTER);
        roomTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(roomTitleLabel, BorderLayout.NORTH);

        // Center Area: Room Description & Podium Status
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        centerPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        JPanel podiumPanel = new JPanel(new GridLayout(2, 1));
        podiumInfoLabel = new JLabel("Podium: None", SwingConstants.CENTER);
        slotStatusLabel = new JLabel("Slots filled: 0 / 0", SwingConstants.CENTER);
        podiumPanel.add(podiumInfoLabel);
        podiumPanel.add(slotStatusLabel);
        centerPanel.add(podiumPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // South Panel: Movement Controls & Podium Interaction
        JPanel controlsPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // Navigation Grid
        JPanel navPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        northButton = new JButton("North");
        southButton = new JButton("South");
        eastButton = new JButton("East");
        westButton = new JButton("West");

        northButton.addActionListener(e -> movePlayer("north"));
        southButton.addActionListener(e -> movePlayer("south"));
        eastButton.addActionListener(e -> movePlayer("east"));
        westButton.addActionListener(e -> movePlayer("west"));

        navPanel.add(northButton);
        navPanel.add(southButton);
        navPanel.add(eastButton);
        navPanel.add(westButton);

        // Action Buttons Grid
        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        placeItemButton = new JButton("Place Selected Item");
        clearPodiumButton = new JButton("Clear Podium Items");

        placeItemButton.addActionListener(e -> handlePlaceItem());
        clearPodiumButton.addActionListener(e -> handleClearPodium());

        actionPanel.add(placeItemButton);
        actionPanel.add(clearPodiumButton);

        controlsPanel.add(navPanel);
        controlsPanel.add(actionPanel);

        add(controlsPanel, BorderLayout.SOUTH);

        updatePanel();
    }

    private void movePlayer(String direction) {
        Player player = game.getPlayer();
        Room currentRoom = player.getCurrentRoom();
        Room targetRoom = currentRoom.getNeighbor(direction);

        if (targetRoom == null) {
            JOptionPane.showMessageDialog(this, "There is no room in that direction.", "Blocked",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!targetRoom.isUnlocked()) {
            JOptionPane.showMessageDialog(this, "Room " + targetRoom.getId() + " is locked! Solve the lock to proceed.",
                    "Room Locked", JOptionPane.ERROR_MESSAGE);
            return;
        }

        player.setCurrentRoom(targetRoom);
        mainWindow.refreshUI();
    }

    private void handlePlaceItem() {
        Item selectedItem = mainWindow.getSelectedInventoryItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Select an item from your inventory first!", "No Item Selected",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Room currentRoom = game.getPlayer().getCurrentRoom();
        Podium podium = currentRoom.getPodium();

        if (podium == null) {
            JOptionPane.showMessageDialog(this, "There is no podium interactive slot here.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean solved = podium.placeItem(selectedItem);
        game.getPlayer().getInventory().removeItem(selectedItem);
        mainWindow.setSelectedInventoryItem(null);

        if (solved) {
            JOptionPane.showMessageDialog(this, "Lock activated! Mechanism solved successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Unlocks adjacent rooms safely without depending on getNeighbors()
            String[] dirs = { "north", "south", "east", "west" };
            for (String dir : dirs) {
                Room n = currentRoom.getNeighbor(dir);
                if (n != null) {
                    n.setUnlocked(true);
                }
            }

            // Room 24 climax trigger
            if (currentRoom.getId() == 24) {
                VictoryDialog victory = new VictoryDialog(mainWindow);
                victory.setVisible(true);
            }
        }

        mainWindow.refreshUI();
    }

    private void handleClearPodium() {
        Room currentRoom = game.getPlayer().getCurrentRoom();
        Podium podium = currentRoom.getPodium();

        if (podium != null && !podium.isSolved()) {
            for (Item item : podium.getPlacedItems()) {
                game.getPlayer().getInventory().addItem(item);
            }
            podium.clearItems();
            mainWindow.refreshUI();
        }
    }

    public void updatePanel() {
        Room room = game.getPlayer().getCurrentRoom();
        if (room == null)
            return;

        roomTitleLabel.setText("Room " + room.getId());
        descriptionArea.setText(room.getDescription());

        northButton.setEnabled(room.getNeighbor("north") != null);
        southButton.setEnabled(room.getNeighbor("south") != null);
        eastButton.setEnabled(room.getNeighbor("east") != null);
        westButton.setEnabled(room.getNeighbor("west") != null);

        Podium podium = room.getPodium();
        if (podium != null) {
            podiumInfoLabel.setText("Podium: " + podium.getDescription());
            int totalSlots = podium.getSlots();
            int placedCount = podium.getPlacedItems().size();
            slotStatusLabel.setText("Slots filled: " + placedCount + " / " + totalSlots);
            placeItemButton.setEnabled(!podium.isSolved());
            clearPodiumButton.setEnabled(!podium.isSolved() && placedCount > 0);
        } else {
            podiumInfoLabel.setText("Podium: None");
            slotStatusLabel.setText("Slots filled: 0 / 0");
            placeItemButton.setEnabled(false);
            clearPodiumButton.setEnabled(false);
        }
    }

    // Alias method to satisfy MainWindow calls
    public void updateView() {
        updatePanel();
    }
}