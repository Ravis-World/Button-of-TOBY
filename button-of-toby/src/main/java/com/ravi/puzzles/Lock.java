package com.ravi.puzzles;

import java.util.List;
import com.ravi.items.Item;
import com.ravi.items.ItemColor;
import com.ravi.items.ItemShape;

public class Lock {
    private final int roomNumber;
    private final String lockDescription;
    private final int requiredItemCount;

    public Lock(int roomNumber, String lockDescription, int requiredItemCount) {
        this.roomNumber = roomNumber;
        this.lockDescription = lockDescription;
        this.requiredItemCount = requiredItemCount;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getLockDescription() {
        return lockDescription;
    }

    public int getRequiredItemCount() {
        return requiredItemCount;
    }

    public boolean evaluate(List<Item> items) {
        if (items == null || items.size() < requiredItemCount) {
            return false;
        }

        return switch (roomNumber) {
            // Room 5: Requires 2 items matching in shape
            case 5 -> items.size() >= 2 && items.get(0).getShape() == items.get(1).getShape();

            // Room 8: Requires 2 items matching in color
            case 8 -> items.size() >= 2 && items.get(0).getColor() == items.get(1).getColor();

            // Room 12: Requires 3 items of distinct shapes
            case 12 -> items.size() >= 3
                    && items.get(0).getShape() != items.get(1).getShape()
                    && items.get(1).getShape() != items.get(2).getShape()
                    && items.get(0).getShape() != items.get(2).getShape();

            // Room 18: Requires 3 Red items
            case 18 -> items.stream().filter(i -> i.getColor() == ItemColor.RED).count() >= 3;

            // Room 24: Requires 4 Star items
            case 24 -> items.stream().filter(i -> i.getShape() == ItemShape.STAR).count() >= 4;

            default -> true;
        };
    }
}