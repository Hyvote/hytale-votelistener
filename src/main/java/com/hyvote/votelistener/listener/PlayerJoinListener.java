package com.hyvote.votelistener.listener;

import com.hypixel.hytale.server.core.Server;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hyvote.votelistener.data.PendingReward;
import com.hyvote.votelistener.data.PendingRewardsManager;

import java.util.List;
import java.util.logging.Logger;

/**
 * Listens for player join events to deliver pending vote rewards.
 *
 * When a player joins the server, this listener checks if they have any
 * pending rewards from votes received while offline, and delivers them
 * automatically.
 */
public class PlayerJoinListener {

    private final Server server;
    private final Logger logger;
    private final PendingRewardsManager pendingRewardsManager;

    /**
     * Creates a new PlayerJoinListener.
     *
     * @param server The server instance for executing commands
     * @param logger The logger for info messages
     * @param pendingRewardsManager The pending rewards manager for retrieving and clearing rewards
     */
    public PlayerJoinListener(Server server, Logger logger, PendingRewardsManager pendingRewardsManager) {
        this.server = server;
        this.logger = logger;
        this.pendingRewardsManager = pendingRewardsManager;
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
        String uuid = event.getPlayer().getUniqueId().toString();
        String username = event.getPlayer().getName();

        // Check if player has pending rewards
        if (!pendingRewardsManager.hasPendingRewards(uuid)) {
            return;
        }

        // Get all pending rewards for this player
        List<PendingReward> pendingRewards = pendingRewardsManager.getPendingRewards(uuid);
        int totalRewards = pendingRewards.size();

        logger.info("Delivering " + totalRewards + " pending vote rewards to " + username);

        // Execute all commands from all pending rewards
        int totalCommandsExecuted = 0;
        for (PendingReward reward : pendingRewards) {
            List<String> commands = reward.getCommands();
            for (String command : commands) {
                server.executeCommand(command);
                totalCommandsExecuted++;
            }
        }

        // Clear pending rewards after successful delivery
        pendingRewardsManager.clearPendingRewards(uuid);

        // Notify the player about their rewards
        String playerMessage = "tellraw " + username + " {\"text\":\"You received " + totalRewards
                + " pending vote reward" + (totalRewards > 1 ? "s" : "") + "! Thank you for voting!\",\"color\":\"green\"}";
        server.executeCommand(playerMessage);

        logger.info("Successfully delivered " + totalCommandsExecuted + " commands from "
                + totalRewards + " pending rewards to " + username);
    }
}
