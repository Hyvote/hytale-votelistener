package com.hyvote.votelistener.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration model for HytaleVoteListener.
 *
 * This class represents the JSON configuration structure and provides
 * sensible defaults for vote reward settings.
 */
public class Config {

    /**
     * Commands to execute when a player votes.
     * Supports placeholders: %player%, %service%, %uuid%
     */
    private List<String> commands;

    /**
     * Whether to broadcast vote announcements to the server.
     */
    private boolean broadcastVote;

    /**
     * Enable verbose debug logging for troubleshooting.
     */
    private boolean debugMode;

    /**
     * Whether random rewards are enabled.
     * When enabled, a random reward from randomRewards is selected on each vote.
     */
    private boolean randomRewardsEnabled;

    /**
     * List of random reward tiers with weighted chances.
     * One reward is selected randomly based on chance weights when a player votes.
     */
    private List<RandomReward> randomRewards;

    /**
     * Creates a new Config with default values.
     */
    public Config() {
        this.commands = new ArrayList<>();
        this.commands.add("say %player% voted on %service%!");
        this.commands.add("give %player% diamond 1");
        this.broadcastVote = true;
        this.debugMode = false;
        this.randomRewardsEnabled = true;
        this.randomRewards = new ArrayList<>();

        // Common tier - 70% chance
        this.randomRewards.add(new RandomReward(
            "common",
            70.0,
            Arrays.asList("give %player% iron_ingot 5")
        ));

        // Rare tier - 25% chance
        this.randomRewards.add(new RandomReward(
            "rare",
            25.0,
            Arrays.asList("give %player% gold_ingot 3")
        ));

        // Legendary tier - 5% chance
        this.randomRewards.add(new RandomReward(
            "legendary",
            5.0,
            Arrays.asList("give %player% diamond 1", "say %player% got a legendary reward!")
        ));
    }

    /**
     * Gets the list of commands to execute on vote.
     *
     * @return List of command strings with placeholders
     */
    public List<String> getCommands() {
        return commands;
    }

    /**
     * Returns whether vote broadcasts are enabled.
     *
     * @return true if votes should be announced to server
     */
    public boolean isBroadcastVote() {
        return broadcastVote;
    }

    /**
     * Returns whether debug mode is enabled.
     *
     * @return true if verbose logging is enabled
     */
    public boolean isDebugMode() {
        return debugMode;
    }

    /**
     * Returns whether random rewards are enabled.
     *
     * @return true if random rewards should be given on vote
     */
    public boolean isRandomRewardsEnabled() {
        return randomRewardsEnabled;
    }

    /**
     * Gets the list of random reward tiers.
     *
     * @return List of RandomReward configurations
     */
    public List<RandomReward> getRandomRewards() {
        return randomRewards;
    }
}
