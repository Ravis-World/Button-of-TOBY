package com.ravi.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.ravi.game.Game;
import com.ravi.game.Player;
import com.ravi.items.Item;
import com.ravi.puzzles.Podium;
import com.ravi.world.Room;

public class RoomPanel extends JPanel {
    private final MainWindow mainWindow;
    private final Game game;

    private final JLabel roomTitleLabel;
    private final JTextPane roomView;
    private final JLabel podiumInfoLabel;
    private final JLabel slotStatusLabel;

    private final JButton northButton;
    private final JButton southButton;
    private final JButton eastButton;
    private final JButton westButton;
    private final JButton pickUpItemsButton;
    private final JButton placeItemButton;
    private final JButton clearPodiumButton;

    public RoomPanel(MainWindow mainWindow, Game game) {
        this.mainWindow = mainWindow;
        this.game = game;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Top Header
        roomTitleLabel = new JLabel("Room", SwingConstants.CENTER);
        roomTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(roomTitleLabel, BorderLayout.NORTH);

        // Center Area: Room Description & Podium Status
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBackground(Color.WHITE);

        roomView = new JTextPane();
        roomView.setEditable(false);
        roomView.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        roomView.setBackground(Color.BLACK);
        roomView.setForeground(Color.WHITE);
        JScrollPane roomScrollPane = new JScrollPane(roomView);
        roomScrollPane.getViewport().setBackground(Color.BLACK);
        centerPanel.add(roomScrollPane, BorderLayout.CENTER);

        JPanel podiumPanel = new JPanel(new GridLayout(2, 1));
        podiumPanel.setBackground(Color.WHITE);
        podiumInfoLabel = new JLabel("Podium: None", SwingConstants.CENTER);
        slotStatusLabel = new JLabel("Slots filled: 0 / 0", SwingConstants.CENTER);
        podiumPanel.add(podiumInfoLabel);
        podiumPanel.add(slotStatusLabel);
        centerPanel.add(podiumPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // South Panel: Movement Controls & Podium Interaction
        JPanel controlsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        controlsPanel.setBackground(Color.WHITE);

        // Navigation Grid
        JPanel navPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        navPanel.setBackground(Color.WHITE);
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
        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        actionPanel.setBackground(Color.WHITE);
        pickUpItemsButton = new JButton("Collect Items");
        placeItemButton = new JButton("Place Selected Item");
        clearPodiumButton = new JButton("Clear Podium Items");

        pickUpItemsButton.addActionListener(e -> handlePickUpItems());
        placeItemButton.addActionListener(e -> handlePlaceItem());
        clearPodiumButton.addActionListener(e -> handleClearPodium());

        actionPanel.add(pickUpItemsButton);
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
        
        if (currentRoom == null) return;
    
        Room targetRoom = currentRoom.getNeighbor(direction);
    
        if (targetRoom == null) {
            JOptionPane.showMessageDialog(this, "There is no room in that direction.", "Blocked", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        // Move exactly one room step
        player.setCurrentRoom(targetRoom);
    
        if (!targetRoom.isUnlocked()) {
            JOptionPane.showMessageDialog(this, "This room is locked! Use the podium to place items and activate the mechanism.", "Room Locked", JOptionPane.INFORMATION_MESSAGE);
        }
    
        // Refresh GUI once
        mainWindow.refreshUI();
    }

    private void handlePickUpItems() {
        Room currentRoom = game.getPlayer().getCurrentRoom();
        for (Item item : currentRoom.takeScatteredItems()) {
            game.getPlayer().getInventory().addItem(item);
        }
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

        if (podium.getLock() != null && !game.canSolveLock(currentRoom.getId())) {
            JOptionPane.showMessageDialog(this, "Solve the previous major lock before attempting this one.",
                "Lock Sequence", JOptionPane.INFORMATION_MESSAGE);
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
                if (n != null && (n.getPodium() == null || n.getPodium().getLock() == null)) {
                    n.setUnlocked(true);
                }
            }

            if (podium.getLock() != null) {
                currentRoom.setUnlocked(true);
                game.advanceLockProgression(currentRoom.getId());
            }

            if (currentRoom.getId() == 24) {
                TobyButtonWindow buttonWindow = new TobyButtonWindow(mainWindow,
                        () -> new VictoryDialog(mainWindow).setVisible(true));
                buttonWindow.setVisible(true);
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
        renderRoom(room);

        Podium podium = room.getPodium();
        int scatteredItemCount = room.getScatteredItems().size();
        pickUpItemsButton.setText("Collect Items (" + scatteredItemCount + ")");
        pickUpItemsButton.setEnabled(scatteredItemCount > 0);
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

    private void renderRoom(Room room) {
        StyledDocument document = roomView.getStyledDocument();
        try {
            document.remove(0, document.getLength());
        } catch (BadLocationException e) {
            throw new IllegalStateException(e);
        }

        appendText("+" + "-".repeat(48) + "+\n", Color.WHITE);
        appendCentered("ROOM " + room.getId(), Color.WHITE);
        appendDirection(room.getNeighbor("north"), "^", "NORTH");
        appendFloorItems(room.getScatteredItems());
        appendCentered("+------------+", Color.WHITE);
        appendCentered("|   PODIUM   |", Color.WHITE);
        appendCentered("+------------+", Color.WHITE);
        appendSideDirections(room);
        appendDirection(room.getNeighbor("south"), "v", "SOUTH");
        appendText("+" + "-".repeat(48) + "+\n", Color.WHITE);
        roomView.setCaretPosition(0);
    }

    private void appendDirection(Room neighbor, String arrow, String direction) {
        if (neighbor == null) {
            appendCentered(" ", Color.WHITE);
            return;
        }
        String roomSign = direction + ": ROOM " + neighbor.getId();
        if ("NORTH".equals(direction)) {
            appendCentered(roomSign, Color.CYAN);
            appendCentered(arrow, Color.CYAN);
        } else {
            appendCentered(arrow, Color.CYAN);
            appendCentered(roomSign, Color.CYAN);
        }
    }

    private void appendFloorItems(List<Item> items) {
        appendText("| FLOOR: ", Color.WHITE);
        int used = 8;
        for (Item item : items) {
            String marker = "[" + item.getColor().name() + " " + shapeMarker(item) + "]";
            if (used + marker.length() + 1 > 47) {
                break;
            }
            appendText(marker + " ", itemColor(item));
            used += marker.length() + 1;
        }
        appendText(" ".repeat(Math.max(0, 48 - used)) + "|\n", Color.WHITE);
    }

    private void appendSideDirections(Room room) {
        Room west = room.getNeighbor("west");
        Room east = room.getNeighbor("east");
        String westSign = west == null ? "" : "< ROOM " + west.getId();
        String eastSign = east == null ? "" : "ROOM " + east.getId() + " >";
        int gap = Math.max(1, 48 - westSign.length() - eastSign.length());

        appendText("|", Color.WHITE);
        appendText(westSign, west == null ? Color.WHITE : Color.CYAN);
        appendText(" ".repeat(gap), Color.WHITE);
        appendText(eastSign, east == null ? Color.WHITE : Color.CYAN);
        appendText("|\n", Color.WHITE);
    }

    private void appendCentered(String text, Color color) {
        int leftPadding = Math.max(0, (48 - text.length()) / 2);
        int rightPadding = Math.max(0, 48 - text.length() - leftPadding);
        appendText("|" + " ".repeat(leftPadding), Color.WHITE);
        appendText(text, color);
        appendText(" ".repeat(rightPadding) + "|\n", Color.WHITE);
    }

    private void appendText(String text, Color color) {
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setForeground(attributes, color);
        try {
            roomView.getStyledDocument().insertString(roomView.getStyledDocument().getLength(), text, attributes);
        } catch (BadLocationException e) {
            throw new IllegalStateException(e);
        }
    }

    private Color itemColor(Item item) {
        switch (item.getColor()) {
            case RED:
                return new Color(255, 90, 90);
            case BLUE:
                return new Color(100, 170, 255);
            case GREEN:
                return new Color(100, 220, 120);
            case YELLOW:
                return new Color(255, 220, 80);
            default:
                return Color.WHITE;
        }
    }

    private String shapeMarker(Item item) {
        switch (item.getShape()) {
            case CIRCLE:
                return "O";
            case TRIANGLE:
                return "^";
            case SQUARE:
                return "[]";
            case HEXAGON:
                return "<=>";
            case STAR:
                return "*";
            default:
                return "?";
        }
    }

    // Alias method to satisfy MainWindow calls
    public void updateView() {
        updatePanel();
    }
}