package com.hyvote.votelistener.util;

import org.hyvote.plugins.votifier.vote.Vote;

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
        String username = vote.username();
        result = result.replace("%player%", username != null ? username : "");

        // Replace %service% with service name
        String serviceName = vote.serviceName();
        result = result.replace("%service%", serviceName != null ? serviceName : "");

        // Note: %uuid% placeholder handled separately - Vote record doesn't contain UUID
        // The calling code should pass UUID if needed via a different overload

        // Replace %timestamp% with timestamp (long converted to String)
        result = result.replace("%timestamp%", String.valueOf(vote.timestamp()));

        return result;
    }

    /**
     * Processes placeholders in a command string, including reward name placeholder.
     *
     * <p>This overloaded method first calls the standard process method for vote data,
     * then additionally replaces %reward% with the provided reward name.
     *
     * @param command The command string containing placeholders
     * @param vote The vote object containing replacement values
     * @param rewardName The name of the reward tier to replace %reward% placeholder
     * @return The processed command with all placeholders replaced
     */
    public static String process(String command, Vote vote, String rewardName) {
        // First process standard vote placeholders
        String result = process(command, vote);

        // Replace %reward% with reward name
        result = result.replace("%reward%", rewardName != null ? rewardName : "");

        return result;
    }

    /**
     * Processes placeholders in a command string, including reward name, streak, total votes, and UUID.
     *
     * <p>This overloaded method provides full placeholder support for streak bonus commands,
     * including all standard vote placeholders plus %reward%, %streak%, %totalvotes%, and %uuid%.
     *
     * @param command The command string containing placeholders
     * @param vote The vote object containing replacement values
     * @param rewardName The name of the reward tier to replace %reward% placeholder
     * @param streak The player's current vote streak to replace %streak% placeholder
     * @param totalVotes The player's total vote count to replace %totalvotes% placeholder
     * @param uuid The player's UUID to replace %uuid% placeholder
     * @return The processed command with all placeholders replaced
     */
    public static String process(String command, Vote vote, String rewardName, int streak, int totalVotes, String uuid) {
        // First process standard vote and reward placeholders
        String result = process(command, vote, rewardName);

        // Replace %streak% with streak value
        result = result.replace("%streak%", String.valueOf(streak));

        // Replace %totalvotes% with total votes value
        result = result.replace("%totalvotes%", String.valueOf(totalVotes));

        // Replace %uuid% with UUID
        result = result.replace("%uuid%", uuid != null ? uuid : "");

        return result;
    }
}
