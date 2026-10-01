package com.ravi.gui;

import com.ravi.game.Game;
import com.ravi.items.Item;
import com.ravi.items.ItemColor;
import com.ravi.items.ItemShape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
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
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Player Inventory"));

        listModel = new DefaultListModel<>();
        itemJList = new JList<>(listModel);
        itemJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemJList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        itemJList.setBackground(Color.WHITE);
        itemJList.setCellRenderer(new ItemCellRenderer());

        // Initialize label before attached listeners reference it
        selectedItemLabel = new JLabel("No item selected", SwingConstants.CENTER);
        selectedItemLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        selectedItemLabel.setBackground(Color.WHITE);
        selectedItemLabel.setOpaque(true);

        itemJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Item selected = itemJList.getSelectedValue();
                mainWindow.setSelectedInventoryItem(selected);
                if (selected != null) {
                    selectedItemLabel.setText("Selected for placement");
                } else {
                    selectedItemLabel.setText("No item selected");
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
            selectedItemLabel.setText("No item selected");
            mainWindow.setSelectedInventoryItem(null);
        }
    }

    private static class ItemCellRenderer extends JLabel implements ListCellRenderer<Item> {
        private ItemCellRenderer() {
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Item> list, Item item, int index,
                boolean isSelected, boolean cellHasFocus) {
            setText(item.toString());
            setFont(list.getFont());
            setIcon(new ShapeIcon(item.getShape(), colorFor(item.getColor())));
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return this;
        }

        private static Color colorFor(ItemColor color) {
            return switch (color) {
                case RED -> new Color(220, 55, 55);
                case BLUE -> new Color(55, 105, 220);
                case GREEN -> new Color(55, 165, 85);
                case YELLOW -> new Color(235, 185, 35);
            };
        }
    }

    private static class ShapeIcon implements Icon {
        private static final int SIZE = 24;
        private final ItemShape shape;
        private final Color color;

        private ShapeIcon(ItemShape shape, Color color) {
            this.shape = shape;
            this.color = color;
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x + 3, y + 3);
            g2.setColor(color);
            Shape iconShape = createShape();
            g2.fill(iconShape);
            g2.setColor(color.darker());
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(iconShape);
            g2.dispose();
        }

        private Shape createShape() {
            return switch (shape) {
                case CIRCLE -> new Ellipse2D.Float(1, 1, 16, 16);
                case SQUARE -> new Rectangle(1, 1, 16, 16);
                case TRIANGLE -> polygon(3, 8.5, 8, Math.PI / 2);
                case HEXAGON -> polygon(6, 8.5, 8, 0);
                case STAR -> star();
            };
        }

        private Shape polygon(int sides, double center, double radius, double startAngle) {
            Path2D path = new Path2D.Double();
            for (int i = 0; i < sides; i++) {
                double angle = startAngle + 2 * Math.PI * i / sides;
                double pointX = center + radius * Math.cos(angle);
                double pointY = center + radius * Math.sin(angle);
                if (i == 0) {
                    path.moveTo(pointX, pointY);
                } else {
                    path.lineTo(pointX, pointY);
                }
            }
            path.closePath();
            return path;
        }

        private Shape star() {
            Path2D path = new Path2D.Double();
            for (int i = 0; i < 10; i++) {
                double radius = i % 2 == 0 ? 8.5 : 3.8;
                double angle = -Math.PI / 2 + Math.PI * i / 5;
                double pointX = 9 + radius * Math.cos(angle);
                double pointY = 9 + radius * Math.sin(angle);
                if (i == 0) {
                    path.moveTo(pointX, pointY);
                } else {
                    path.lineTo(pointX, pointY);
                }
            }
            path.closePath();
            return path;
        }
    }
}