package com.hyvote.votelistener.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration model for a random reward tier.
 *
 * <p>Each random reward has a name (identifier), a chance weight for selection,
 * and a list of commands to execute when selected. Higher chance values mean
 * more likely to be selected relative to other rewards.
 *
 * <p>Example configuration:
 * <pre>
 * {
 *   "name": "rare",
 *   "chance": 25.0,
 *   "commands": ["give %player% gold_ingot 3"]
 * }
 * </pre>
 */
public class RandomReward {

    /**
     * Identifier for the reward tier (e.g., "common", "rare", "legendary").
     */
    private String name;

    /**
     * Weight value for selection probability.
     * Higher values mean more likely to be selected relative to other rewards.
     */
    private double chance;

    /**
     * Commands to execute when this reward is selected.
     * Supports placeholders: %player%, %service%, %uuid%
     */
    private List<String> commands;

    /**
     * Creates a new RandomReward with default values.
     */
    public RandomReward() {
        this.name = "default";
        this.chance = 1.0;
        this.commands = new ArrayList<>();
    }

    /**
     * Creates a new RandomReward with specified values.
     *
     * @param name     Identifier for the reward tier
     * @param chance   Weight value for selection probability
     * @param commands Commands to execute when selected
     */
    public RandomReward(String name, double chance, List<String> commands) {
        this.name = name;
        this.chance = chance;
        this.commands = commands != null ? commands : new ArrayList<>();
    }

    /**
     * Gets the reward tier name.
     *
     * @return The identifier for this reward tier
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the chance weight for selection.
     *
     * @return The weight value (higher = more likely)
     */
    public double getChance() {
        return chance;
    }

    /**
     * Gets the commands to execute when this reward is selected.
     *
     * @return List of command strings with placeholders
     */
    public List<String> getCommands() {
        return commands;
    }
}
