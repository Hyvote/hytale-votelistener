package com.hyvote.votelistener.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration model for a streak bonus reward tier.
 *
 * <p>Each streak bonus has a number of consecutive days required to trigger,
 * a name (identifier), and a list of commands to execute when the streak is reached.
 *
 * <p>Example configuration:
 * <pre>
 * {
 *   "streakDays": 7,
 *   "name": "weekly",
 *   "commands": ["give %player% diamond 2", "say %player% has a %streak%-day vote streak!"]
 * }
 * </pre>
 */
public class StreakBonus {

    /**
     * Number of consecutive days required to trigger this bonus.
     */
    private int streakDays;

    /**
     * Identifier for the bonus tier (e.g., "3-day", "weekly", "monthly").
     */
    private String name;

    /**
     * Commands to execute when this streak bonus is reached.
     * Supports placeholders: %player%, %service%, %uuid%, %streak%, %totalvotes%
     */
    private List<String> commands;

    /**
     * Creates a new StreakBonus with default values.
     * Required for Gson deserialization.
     */
    public StreakBonus() {
        this.streakDays = 1;
        this.name = "default";
        this.commands = new ArrayList<>();
    }

    /**
     * Creates a new StreakBonus with specified values.
     *
     * @param streakDays Number of consecutive days required to trigger
     * @param name       Identifier for the bonus tier
     * @param commands   Commands to execute when streak is reached
     */
    public StreakBonus(int streakDays, String name, List<String> commands) {
        this.streakDays = streakDays;
        this.name = name;
        this.commands = commands != null ? commands : new ArrayList<>();
    }

    /**
     * Gets the number of consecutive days required for this bonus.
     *
     * @return The streak days threshold
     */
    public int getStreakDays() {
        return streakDays;
    }

    /**
     * Gets the bonus tier name.
     *
     * @return The identifier for this bonus tier
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the commands to execute when this streak bonus is reached.
     *
     * @return List of command strings with placeholders
     */
    public List<String> getCommands() {
        return commands;
    }
}
