package com.ravi.world;

import java.util.HashMap;
import java.util.Map;
import com.ravi.puzzles.Podium;

public class Facility {
    private final Map<Integer, Room> rooms;

    public Facility() {
        this.rooms = new HashMap<>();
    }

    public void initializeRooms() {
        // Instantiate all 24 rooms with default descriptions
        for (int id = 1; id <= 24; id++) {
            Room room = new Room(id, "Room " + id + " inside Dr. \u0393's facility.");
            room.setPodium(new Podium("Podium for Room " + id));

            // Major lock rooms are locked by default
            if (id == 5 || id == 8 || id == 12 || id == 18 || id == 24) {
                room.setUnlocked(false);
            }
            rooms.put(id, room);
        }

        // Row 1 (Bottom): 10, 17, 7, 11, 16, 4, 5
        link("east", 10, 17);
        link("east", 17, 7);
        link("east", 7, 11);
        link("east", 11, 16);
        link("east", 16, 4);
        link("east", 4, 5);

        // Row 2: 8, 18, 2, 19, 1
        link("east", 8, 18);
        link("east", 18, 2);
        link("east", 2, 19);
        link("east", 19, 1);

        // Row 3: 20, 15, 13, 22, 14
        link("east", 20, 15);
        link("east", 15, 13);
        link("east", 13, 22);
        link("east", 22, 14);

        // Row 4: 3, 21, 9
        link("east", 3, 21);
        link("east", 21, 9);

        // Row 5: 6, 12, 23
        link("east", 6, 12);
        link("east", 12, 23);

        // Vertical Connections between Rows:
        // Row 1 to Row 2
        link("north", 10, 8);
        link("north", 17, 18);
        link("north", 7, 2);
        link("north", 11, 19);
        link("north", 16, 1);

        // Row 2 to Row 3
        link("north", 18, 20);
        link("north", 2, 15);
        link("north", 19, 13);
        link("north", 1, 22);

        // Row 3 to Row 4
        link("north", 20, 3);
        link("north", 15, 21);
        link("north", 13, 9);

        // Row 4 to Row 5
        link("north", 3, 12);
        link("north", 21, 23);

        // Row 5 to Row 6 (Northernmost Room 24)
        link("north", 23, 24);
    }

    private void link(String direction, int fromId, int toId) {
        Room r1 = rooms.get(fromId);
        Room r2 = rooms.get(toId);
        if (r1 != null && r2 != null) {
            r1.setNeighbor(direction, r2);
            r2.setNeighbor(getOppositeDirection(direction), r1);
        }
    }

    private String getOppositeDirection(String direction) {
        switch (direction.toLowerCase()) {
            case "north":
                return "south";
            case "south":
                return "north";
            case "east":
                return "west";
            case "west":
                return "east";
            default:
                return "";
        }
    }

    public Room getRoom(int id) {
        return rooms.get(id);
    }

    public Map<Integer, Room> getAllRooms() {
        return rooms;
    }
}