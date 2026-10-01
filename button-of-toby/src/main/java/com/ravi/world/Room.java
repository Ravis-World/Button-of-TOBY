package com.ravi.world;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.ravi.items.Item;
import com.ravi.puzzles.Podium;

public class Room {
    private final int id;
    private final String description;
    private final Map<String, Room> neighbors;
    private final List<Item> scatteredItems;
    private Podium podium;
    private boolean unlocked;

    public Room(int id, String description) {
        this.id = id;
        this.description = description;
        this.neighbors = new HashMap<>();
        this.scatteredItems = new ArrayList<>();
        this.unlocked = true;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setNeighbor(String direction, Room neighbor) {
        neighbors.put(direction.toLowerCase(), neighbor);
    }

    public Room getNeighbor(String direction) {
        return neighbors.get(direction.toLowerCase());
    }

    public void addScatteredItem(Item item) {
        if (item != null) {
            scatteredItems.add(item);
        }
    }

    public List<Item> getScatteredItems() {
        return Collections.unmodifiableList(scatteredItems);
    }

    public List<Item> takeScatteredItems() {
        List<Item> items = new ArrayList<>(scatteredItems);
        scatteredItems.clear();
        return items;
    }

    public Podium getPodium() {
        return podium;
    }

    public void setPodium(Podium podium) {
        this.podium = podium;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}