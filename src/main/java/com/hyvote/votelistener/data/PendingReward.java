package com.hyvote.votelistener.data;

import java.util.List;

/**
 * Model representing a pending reward for an offline player.
 *
 * This class stores rewards that need to be delivered when an offline player
 * returns to the server. Commands are stored in their fully processed form
 * (placeholders already replaced) so rewards can be delivered exactly as
 * calculated at vote time, preserving random selections and streak values.
 */
public class PendingReward {

    /**
     * Player UUID as string for identification.
     */
    private final String uuid;

    /**
     * Player username at the time of the vote.
     */
    private final String username;

    /**
     * Name of the voting service for logging purposes.
     */
    private final String serviceName;

    /**
     * Timestamp when the vote was received (epoch milliseconds).
     */
    private final long timestamp;

    /**
     * Fully processed commands ready to execute (placeholders already replaced).
     */
    private final List<String> commands;

    /**
     * Creates a new PendingReward with all fields initialized.
     *
     * @param uuid Player UUID as string
     * @param username Player username at time of vote
     * @param serviceName Name of the voting service
     * @param timestamp When the vote was received (epoch millis)
     * @param commands Fully processed commands ready to execute
     */
    public PendingReward(String uuid, String username, String serviceName, long timestamp, List<String> commands) {
        this.uuid = uuid;
        this.username = username;
        this.serviceName = serviceName;
        this.timestamp = timestamp;
        this.commands = commands;
    }

    /**
     * Gets the player's UUID.
     *
     * @return Player UUID as string
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Gets the player's username at time of vote.
     *
     * @return Player username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the voting service name.
     *
     * @return Service name for logging
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Gets the timestamp when the vote was received.
     *
     * @return Vote timestamp in epoch milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the fully processed commands ready to execute.
     *
     * @return List of commands with placeholders already replaced
     */
    public List<String> getCommands() {
        return commands;
    }
}
