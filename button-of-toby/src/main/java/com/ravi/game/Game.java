// ... existing code ...
package com.ravi.game;

import com.ravi.items.ItemSpawner;
import com.ravi.puzzles.Lock;
import com.ravi.world.Facility;
import com.ravi.world.Room;

public class Game {
    private static final int[] LOCK_ORDER = { 5, 8, 12, 18, 24 };
    private final Facility facility;
    private final Player player;
    private static final long SEED = 20260928L; // Playthrough seed for speedruns

    public Game() {
        this.facility = new Facility();
        this.facility.initializeRooms();

        setupMajorLocks();

        this.player = new Player(facility.getRoom(11)); // Starting bottom room position

        // Populate items deterministically
        ItemSpawner.populateFacility(this, SEED);
    }

    private void setupMajorLocks() {
        configureLock(5, "Gate 5: Requires at least 2 RED items", 2);
        configureLock(8, "Gate 8: Requires 3 items of DISTINCT SHAPES", 3);
        configureLock(12, "Gate 12: Requires 3 items of SAME SHAPE but DIFFERENT COLOURS", 3);
        configureLock(18, "Gate 18: Requires ALL 4 COLOURS (RED, BLUE, GREEN, YELLOW)", 4);
        configureLock(24, "Button of TOBY Gate: Requires 4 DISTINCT SHAPES & 4 DISTINCT COLOURS", 4);
    }

    private void configureLock(int roomId, String clue, int itemSlots) {
        Room room = facility.getRoom(roomId);
        if (room != null) {
            Lock lock = new Lock(roomId, clue, itemSlots);
            room.getPodium().setPuzzle(lock);
            room.setUnlocked(false);
        }
    }

    public boolean canSolveLock(int roomId) {
        for (int index = 0; index < LOCK_ORDER.length; index++) {
            if (LOCK_ORDER[index] == roomId) {
                if (index == 0) {
                    return true;
                }
                Room previousLockRoom = facility.getRoom(LOCK_ORDER[index - 1]);
                return previousLockRoom.getPodium().isSolved();
            }
        }
        return true;
    }

    public void advanceLockProgression(int roomId) {
        for (int index = 0; index < LOCK_ORDER.length; index++) {
            if (LOCK_ORDER[index] == roomId) {
                if (index + 1 < LOCK_ORDER.length) {
                    facility.getRoom(LOCK_ORDER[index + 1]).setUnlocked(true);
                }
                return;
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Facility getFacility() {
        return facility;
    }
}