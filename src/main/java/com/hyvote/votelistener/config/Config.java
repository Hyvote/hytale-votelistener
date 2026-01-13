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
     * Whether streak bonus rewards are enabled.
     * When enabled, bonus rewards are given when players reach configured streak milestones.
     */
    private boolean streakBonusEnabled;

    /**
     * List of streak bonus tiers.
     * When a player's current streak matches a tier's streakDays, the bonus is awarded.
     */
    private List<StreakBonus> streakBonuses;

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

        // Initialize streak bonus configuration
        this.streakBonusEnabled = true;
        this.streakBonuses = new ArrayList<>();

        // 3-day streak bonus
        this.streakBonuses.add(new StreakBonus(
            3,
            "3-day",
            Arrays.asList("give %player% emerald 1")
        ));

        // 7-day (weekly) streak bonus
        this.streakBonuses.add(new StreakBonus(
            7,
            "weekly",
            Arrays.asList("give %player% diamond 2", "say %player% has a %streak%-day vote streak!")
        ));

        // 30-day (monthly) streak bonus
        this.streakBonuses.add(new StreakBonus(
            30,
            "monthly",
            Arrays.asList("give %player% diamond_block 1")
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

    /**
     * Returns whether streak bonus rewards are enabled.
     *
     * @return true if streak bonuses should be given when milestones are reached
     */
    public boolean isStreakBonusEnabled() {
        return streakBonusEnabled;
    }

    /**
     * Gets the list of streak bonus tiers.
     *
     * @return List of StreakBonus configurations
     */
    public List<StreakBonus> getStreakBonuses() {
        return streakBonuses;
    }
}
