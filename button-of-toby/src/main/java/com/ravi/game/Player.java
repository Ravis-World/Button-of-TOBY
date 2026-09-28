package com.ravi.game;

import com.ravi.items.Inventory;
import com.ravi.world.Room;

public class Player {
    private Room currentRoom;
    private final Inventory inventory;

    public Player(Room startingRoom) {
        this.currentRoom = startingRoom;
        this.inventory = new Inventory();
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public Inventory getInventory() {
        return inventory;
    }

}