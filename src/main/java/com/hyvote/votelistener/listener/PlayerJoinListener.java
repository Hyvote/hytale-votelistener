package com.hyvote.votelistener.listener;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hyvote.votelistener.HytaleVoteListener;
import com.hyvote.votelistener.data.PendingReward;
import com.hyvote.votelistener.data.PendingRewardsManager;

import java.util.List;
import java.util.logging.Level;

/**
 * Listens for player join events to deliver pending vote rewards.
 *
 * When a player joins the server, this listener checks if they have any
 * pending rewards from votes received while offline, and delivers them
 * automatically.
 */
public class PlayerJoinListener {

    private final HytaleVoteListener plugin;
    private final HytaleLogger logger;
    private final PendingRewardsManager pendingRewardsManager;

    /**
     * Creates a new PlayerJoinListener.
     *
     * @param plugin The plugin instance
     * @param pendingRewardsManager The pending rewards manager for retrieving and clearing rewards
     */
    public PlayerJoinListener(HytaleVoteListener plugin, PendingRewardsManager pendingRewardsManager) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.pendingRewardsManager = pendingRewardsManager;
    }

    /**
     * Executes a command through the command manager.
     *
     * @param command The command string to execute
     */
    private void executeCommand(String command) {
        CommandManager.get().handleCommand(ConsoleSender.INSTANCE, command);
    }

    /**
     * Handles player connect events to deliver pending vote rewards.
     *
     * Checks if the joining player has any pending rewards from votes
     * received while offline, executes all reward commands, and clears
     * the pending rewards after successful delivery.
     *
     * @param event The player connect event
     */
    public void onPlayerConnect(PlayerConnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        String uuid = playerRef.getUuid().toString();
        String username = playerRef.getUsername();

        // Check if player has pending rewards
        if (!pendingRewardsManager.hasPendingRewards(uuid)) {
            return;
        }

        // Get all pending rewards for this player
        List<PendingReward> pendingRewards = pendingRewardsManager.getPendingRewards(uuid);
        int totalRewards = pendingRewards.size();

        logger.at(Level.INFO).log("Delivering %d pending vote rewards to %s", totalRewards, username);

        // Execute all commands from all pending rewards
        int totalCommandsExecuted = 0;
        for (PendingReward reward : pendingRewards) {
            List<String> commands = reward.getCommands();
            for (String command : commands) {
                executeCommand(command);
                totalCommandsExecuted++;
            }
        }

        // Clear pending rewards after successful delivery
        pendingRewardsManager.clearPendingRewards(uuid);

        // Notify the player about their rewards
        String message = "You received " + totalRewards + " pending vote reward"
                + (totalRewards > 1 ? "s" : "") + "! Thank you for voting!";
        playerRef.sendMessage(Message.raw(message));

        logger.at(Level.INFO).log("Successfully delivered %d commands from %d pending rewards to %s",
                totalCommandsExecuted, totalRewards, username);
    }
}
