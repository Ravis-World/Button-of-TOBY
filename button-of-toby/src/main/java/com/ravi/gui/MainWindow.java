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

        // Load custom window frame icon or programmatically generate one to prevent Linux Tux default
        loadWindowIcon();

        initUI();
        refreshUI();
    }

    private void loadWindowIcon() {
        Image loadedIcon = null;

        // Java's built-in image readers support PNG but not ICO.
        String[] possiblePaths = {"/icon.png", "/icon.ico", "/images/icon.png"};
        for (String path : possiblePaths) {
            try {
                java.net.URL iconUrl = getClass().getResource(path);
                if (iconUrl != null) {
                    // Try Toolkit first as standard ImageIO may lack ICO decoders without plugins
                    Image img = Toolkit.getDefaultToolkit().getImage(iconUrl);
                    if (img != null) {
                        MediaTracker tracker = new MediaTracker(this);
                        tracker.addImage(img, 0);
                        tracker.waitForID(0, 1000);
                        if (!tracker.isErrorAny() && img.getWidth(null) > 0) {
                            loadedIcon = img;
                            break;
                        }
                    }

                    // Fallback to ImageIO.read if Toolkit read fails
                    loadedIcon = javax.imageio.ImageIO.read(iconUrl);
                    if (loadedIcon != null) break;
                }
            } catch (Exception ignored) {
                // Fall through to procedural icon fallback
            }
        }

        // 2. If no external icon file exists or decoding fails, construct a custom procedural TOBY button icon
        if (loadedIcon == null) {
            loadedIcon = createProceduralTobyIcon();
        }

        if (loadedIcon != null) {
            setIconImage(loadedIcon);
        }
    }

    /**
     * Programmatically renders a custom 64x64 'Button of TOBY' icon badge
     * so Swing never falls back to the system default penguin icon on Linux/X11 desktop environments.
     */
    private Image createProceduralTobyIcon() {
        int size = 64;
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        // Enable anti-aliasing for smooth circular borders
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Outer frame (Dark Navy/Purple)
        g2.setColor(new Color(22, 27, 34));
        g2.fillOval(2, 2, 60, 60);

        // Outer border ring (Purple accent)
        g2.setColor(new Color(137, 87, 229));
        g2.setStroke(new BasicStroke(3.0f));
        g2.drawOval(2, 2, 60, 60);

        // Red central TOBY button element
        g2.setColor(new Color(218, 54, 51));
        g2.fillOval(14, 14, 36, 36);
        g2.setColor(new Color(248, 81, 73));
        g2.drawOval(14, 14, 36, 36);

        // Center emblem letter 'T'
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth("T")) / 2;
        int y = (size + fm.getAscent() - fm.getDescent()) / 2 - 1;
        g2.drawString("T", x, y);

        g2.dispose();
        return img;
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
        JLabel statusBar = new JLabel(" Target: Infiltrate Dr. Γ's facility and release TOBY");
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