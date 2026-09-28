package com.ravi.puzzles;

import com.ravi.items.Item;
import java.util.List;

/**
 * Interface defining the behavioral contract for all puzzles and lock
 * mechanisms
 * inside Dr. Γ's escape facility.
 */
public interface Puzzle {

    /**
     * Checks whether the current state or placed items solve the puzzle.
     * 
     * @param placedItems Items currently assigned to the puzzle or podium.
     * @return true if puzzle requirements are satisfied.
     */
    boolean evaluate(List<Item> placedItems);

    /**
     * Returns whether the puzzle has already been solved.
     * 
     * @return true if solved.
     */
    boolean isSolved();

    /**
     * Gets a descriptive hint or rule instruction for the player.
     * 
     * @return The puzzle clue text.
     */
    String getClue();

    /**
     * Marks the puzzle as solved or unsolved.
     * 
     * @param solved target state.
     */
    void setSolved(boolean solved);
}