package com.ravi.world;

import java.util.HashMap;
import java.util.Map;
import com.ravi.puzzles.Podium;

public class Room {
    private final int id;
    private final String description;
    private final Map<String, Room> neighbors;
    private Podium podium;
    private boolean isUnlocked;

    public Room(int id, String description) {
        this.id = id;
        this.description = description;
        this.neighbors = new HashMap<>();
        this.isUnlocked = true;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setNeighbor(String direction, Room room) {
        neighbors.put(direction.toLowerCase(), room);
    }

    public Room getNeighbor(String direction) {
        return neighbors.get(direction.toLowerCase());
    }

    public Podium getPodium() {
        return podium;
    }

    public void setPodium(Podium podium) {
        this.podium = podium;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.isUnlocked = unlocked;
    }

}