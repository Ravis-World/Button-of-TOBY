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
    private final Map<Integer, JButton> roomButtons;

    // Physical layout 6-row grid matrix matching the specification
    private static final int[][] GRID_LAYOUT = {
            { -1, -1, 24, -1, -1, -1, -1 },
            { 6, 12, 23, -1, -1, -1, -1 },
            { -1, 3, 21, 9, -1, -1, -1 },
            { -1, 20, 15, 13, 22, 14, -1 },
            { 8, 18, 2, 19, 1, -1, -1 },
            { 10, 17, 7, 11, 16, 4, 5 }
    };

    public MapPanel(Game game) {
        this.game = game;
        this.roomButtons = new HashMap<>();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Facility Map Grid"));

        initGrid();
    }

    private void initGrid() {
        JPanel gridPanel = new JPanel(new GridLayout(6, 7, 4, 4));
        gridPanel.setBackground(Color.DARK_GRAY);

        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                int roomId = GRID_LAYOUT[r][c];
                if (roomId == -1) {
                    JLabel emptyLabel = new JLabel("");
                    emptyLabel.setOpaque(true);
                    emptyLabel.setBackground(Color.DARK_GRAY);
                    gridPanel.add(emptyLabel);
                } else {
                    JButton btn = new JButton(String.valueOf(roomId));
                    btn.setFont(new Font("SansSerif", Font.BOLD, 12));
                    btn.setFocusable(false);
                    btn.setEnabled(false); // Map display only
                    roomButtons.put(roomId, btn);
                    gridPanel.add(btn);
                }
            }
        }

        add(gridPanel, BorderLayout.CENTER);
    }

    public void updateView() {
        Room current = game.getPlayer().getCurrentRoom();
        int currentId = (current != null) ? current.getId() : -1;

        for (Map.Entry<Integer, JButton> entry : roomButtons.entrySet()) {
            int id = entry.getKey();
            JButton btn = entry.getValue();
            Room room = game.getFacility().getRoom(id);

            if (id == currentId) {
                btn.setBackground(Color.GREEN);
                btn.setForeground(Color.BLACK);
                btn.setText("★ " + id);
            } else if (!room.isUnlocked()) {
                btn.setBackground(Color.RED);
                btn.setForeground(Color.WHITE);
                btn.setText("🔒 " + id);
            } else {
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setForeground(Color.BLACK);
                btn.setText(String.valueOf(id));
            }
        }
    }
}