package com.ravi.puzzles;

import java.util.List;
import com.ravi.items.Item;
import com.ravi.items.ItemColor;

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
            // Room 5: Requires at least 2 red items
            case 5 -> items.stream().filter(item -> item.getColor() == ItemColor.RED).count() >= 2;

            // Room 8: Requires 3 distinct shapes
            case 8 -> items.stream().map(Item::getShape).distinct().count() >= 3;

            // Room 12: Requires 3 items of the same shape and different colours
            case 12 -> items.subList(0, 3).stream().map(Item::getShape).distinct().count() == 1
                && items.subList(0, 3).stream().map(Item::getColor).distinct().count() == 3;

            // Room 18: Requires all 4 colours
            case 18 -> items.stream().map(Item::getColor).distinct().count() >= 4;

            // Room 24: Requires 4 distinct shapes and colours
            case 24 -> items.stream().map(Item::getShape).distinct().count() >= 4
                && items.stream().map(Item::getColor).distinct().count() >= 4;

            default -> true;
        };
    }
}