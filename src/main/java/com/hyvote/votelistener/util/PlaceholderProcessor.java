package com.hyvote.votelistener.util;

import com.hyvote.votifier.Vote;

/**
 * Utility class for processing command placeholders.
 *
 * Replaces placeholders in command strings with actual vote data:
 * - %player% - The username of the player who voted
 * - %service% - The name of the voting service
 * - %uuid% - The UUID of the player (if available)
 * - %timestamp% - The timestamp of the vote
 */
public final class PlaceholderProcessor {

    private PlaceholderProcessor() {
        // Utility class - prevent instantiation
    }

    /**
     * Processes placeholders in a command string, replacing them with actual vote data.
     *
     * @param command The command string containing placeholders
     * @param vote The vote object containing replacement values
     * @return The processed command with all placeholders replaced
     */
    public static String process(String command, Vote vote) {
        if (command == null) {
            return "";
        }
        if (vote == null) {
            return command;
        }

        String result = command;

        // Replace %player% with username
        String username = vote.getUsername();
        result = result.replace("%player%", username != null ? username : "");

        // Replace %service% with service name
        String serviceName = vote.getServiceName();
        result = result.replace("%service%", serviceName != null ? serviceName : "");

        // Replace %uuid% with UUID (if available)
        String uuid = vote.getUuid();
        result = result.replace("%uuid%", uuid != null ? uuid : "");

        // Replace %timestamp% with timestamp
        String timestamp = vote.getTimeStamp();
        result = result.replace("%timestamp%", timestamp != null ? timestamp : "");

        return result;
    }
}
