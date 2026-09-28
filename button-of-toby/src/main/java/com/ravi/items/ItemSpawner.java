package com.ravi.items;

import com.ravi.game.Game;
import com.ravi.world.Facility;
import com.ravi.world.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * ItemSpawner generates and distributes all puzzle items across non-locked
 * rooms
 * in Dr. Γ's facility using a fixed random seed for speedrunning consistency.
 */
public class ItemSpawner {

    public static void populateFacility(Game game, long seed) {
        Facility facility = game.getFacility();
        Random random = new Random(seed);

        // Generate full deck of coloured shapes
        List<Item> itemPool = new ArrayList<>();
        int idCounter = 1;

        for (ItemColor color : ItemColor.values()) {
            for (ItemShape shape : ItemShape.values()) {
                String id = "ITEM_" + idCounter++;
                String name = color.name() + " " + shape.name();
                itemPool.add(new Item(id, name, color, shape));
            }
        }

        Collections.shuffle(itemPool, random);

        // Give player 2 starting items in inventory for immediate puzzle interaction
        if (!itemPool.isEmpty()) {
            game.getPlayer().getInventory().addItem(itemPool.remove(0));
        }
        if (!itemPool.isEmpty()) {
            game.getPlayer().getInventory().addItem(itemPool.remove(0));
        }

        int[] accessibleRooms = { 10, 17, 7, 11, 16, 4, 2, 19, 1, 15, 13, 22, 14, 3, 21, 9, 6, 23 };
        int index = 0;

        while (!itemPool.isEmpty()) {
            int roomId = accessibleRooms[index % accessibleRooms.length];
            Room room = facility.getRoom(roomId);
            if (room != null && room.getPodium() != null) {
                room.getPodium().placeItem(itemPool.remove(0));
            }
            index++;
        }
    }
}