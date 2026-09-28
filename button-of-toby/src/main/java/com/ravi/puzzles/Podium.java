package com.ravi.puzzles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.ravi.items.Item;

public class Podium {
    private String description;
    private final List<Item> placedItems;
    private Lock lock;
    private boolean isSolved;

    // Default Constructor
    public Podium() {
        this("A standard facility podium.");
    }

    // Constructor required by Facility.java
    public Podium(String description) {
        this.description = description;
        this.placedItems = new ArrayList<>();
        this.isSolved = false;
    }

    public String getDescription() {
        if (lock != null) {
            return lock.getLockDescription();
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSlots() {
        if (lock != null) {
            return lock.getRequiredItemCount();
        }
        return 1;
    }

    public boolean placeItem(Item item) {
        if (item == null) {
            return false;
        }

        placedItems.add(item);

        if (lock != null) {
            if (lock.evaluate(placedItems)) {
                isSolved = true;
                return true;
            }
        } else {
            isSolved = true;
        }

        return false;
    }

    public List<Item> getPlacedItems() {
        return Collections.unmodifiableList(placedItems);
    }

    public void clearItems() {
        placedItems.clear();
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public void setPuzzle(Lock lock) {
        setLock(lock);
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        this.isSolved = solved;
    }
}