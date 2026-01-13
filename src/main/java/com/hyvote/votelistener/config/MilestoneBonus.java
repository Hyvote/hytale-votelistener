package com.hyvote.votelistener.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration model for a milestone bonus reward tier.
 *
 * <p>Each milestone bonus has a number of total votes required to trigger,
 * a name (identifier), and a list of commands to execute when the milestone is reached.
 *
 * <p>Example configuration:
 * <pre>
 * {
 *   "votesRequired": 100,
 *   "name": "century",
 *   "commands": ["give %player% diamond_block 1", "say %player% reached %totalvotes% total votes!"]
 * }
 * </pre>
 */
public class MilestoneBonus {

    /**
     * Number of total votes required to trigger this bonus.
     */
    private int votesRequired;

    /**
     * Identifier for the bonus tier (e.g., "first-ten", "fifty", "century").
     */
    private String name;

    /**
     * Commands to execute when this milestone bonus is reached.
     * Supports placeholders: %player%, %service%, %uuid%, %streak%, %totalvotes%
     */
    private List<String> commands;

    /**
     * Creates a new MilestoneBonus with default values.
     * Required for Gson deserialization.
     */
    public MilestoneBonus() {
        this.votesRequired = 1;
        this.name = "default";
        this.commands = new ArrayList<>();
    }

    /**
     * Creates a new MilestoneBonus with specified values.
     *
     * @param votesRequired Number of total votes required to trigger
     * @param name          Identifier for the bonus tier
     * @param commands      Commands to execute when milestone is reached
     */
    public MilestoneBonus(int votesRequired, String name, List<String> commands) {
        this.votesRequired = votesRequired;
        this.name = name;
        this.commands = commands != null ? commands : new ArrayList<>();
    }

    /**
     * Gets the number of total votes required for this bonus.
     *
     * @return The total votes threshold
     */
    public int getVotesRequired() {
        return votesRequired;
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
     * Gets the commands to execute when this milestone bonus is reached.
     *
     * @return List of command strings with placeholders
     */
    public List<String> getCommands() {
        return commands;
    }
}
