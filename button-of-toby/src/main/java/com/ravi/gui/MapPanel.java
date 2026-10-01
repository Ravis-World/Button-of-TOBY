package com.ravi.gui;

import com.ravi.game.Game;
import com.ravi.world.Room;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * MapPanel renders a visual 24-room grid of Dr. Γ's facility layout,
 * highlighting current player position, open rooms, and major lock locations.
 */
public class MapPanel extends JPanel {
    private final Game game;
    private final Map<Integer, JLabel> roomTiles;

    // Physical layout 6-row grid matrix matching the specification
    private static final int[][] GRID_LAYOUT = {
            { -1, -1, -1, 24, -1, -1, -1 },
            { -1, -1, 6, 12, 23, -1, -1 },
            { -1, -1, 3, 21, 9, -1, -1 },
            { -1, 20, 15, 13, 22, 14, -1 },
            { -1, 8, 18, 2, 19, 1, -1 },
            { 10, 17, 7, 11, 16, 4, 5 }
    };

    public MapPanel(Game game) {
        this.game = game;
        this.roomTiles = new HashMap<>();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Facility Map Grid"));

        initGrid();
    }

    private void initGrid() {
        JPanel gridPanel = new JPanel(new GridLayout(6, 7, 4, 4));
        gridPanel.setBackground(Color.WHITE);

        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                int roomId = GRID_LAYOUT[r][c];
                if (roomId == -1) {
                    JLabel emptyLabel = new JLabel("");
                    emptyLabel.setOpaque(true);
                    emptyLabel.setBackground(Color.WHITE);
                    gridPanel.add(emptyLabel);
                } else {
                    JLabel tile = new JLabel(String.valueOf(roomId), SwingConstants.CENTER);
                    tile.setFont(new Font("SansSerif", Font.BOLD, 12));
                    tile.setOpaque(true);
                    tile.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    roomTiles.put(roomId, tile);
                    gridPanel.add(tile);
                }
            }
        }

        add(gridPanel, BorderLayout.CENTER);
    }

    public void updateView() {
        Room current = game.getPlayer().getCurrentRoom();
        int currentId = (current != null) ? current.getId() : -1;

        for (Map.Entry<Integer, JLabel> entry : roomTiles.entrySet()) {
            int id = entry.getKey();
            JLabel tile = entry.getValue();
            Room room = game.getFacility().getRoom(id);
            boolean isCurrentRoom = id == currentId;
            boolean isMajorLock = room.getPodium() != null && room.getPodium().getLock() != null;

            if (isMajorLock) {
                if (room.getPodium().isSolved()) {
                    tile.setBackground(Color.GREEN);
                    tile.setForeground(Color.BLACK);
                    tile.setText((isCurrentRoom ? "★ " : "✓ ") + id);
                } else {
                    boolean lockAvailable = game.canSolveLock(id);
                    tile.setBackground(lockAvailable ? new Color(255, 215, 0) : Color.RED);
                    tile.setForeground(lockAvailable ? Color.BLACK : Color.WHITE);
                    tile.setText((isCurrentRoom ? "★ " : lockAvailable ? "" : "🔒 ") + id);
                }
            } else if (isCurrentRoom) {
                tile.setBackground(Color.GREEN);
                tile.setForeground(Color.BLACK);
                tile.setText("★ " + id);
            } else if (!room.isUnlocked()) {
                tile.setBackground(Color.RED);
                tile.setForeground(Color.WHITE);
                tile.setText("🔒 " + id);
            } else {
                tile.setBackground(Color.LIGHT_GRAY);
                tile.setForeground(Color.BLACK);
                tile.setText(String.valueOf(id));
            }
        }
    }
}