package com.hyvote.votelistener.config;

import java.util.ArrayList;
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
     * Creates a new Config with default values.
     */
    public Config() {
        this.commands = new ArrayList<>();
        this.commands.add("say %player% voted on %service%!");
        this.commands.add("give %player% diamond 1");
        this.broadcastVote = true;
        this.debugMode = false;
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
}
