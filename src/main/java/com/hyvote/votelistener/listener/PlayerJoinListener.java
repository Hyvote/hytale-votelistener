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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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

        // Check if player has pending rewards (by UUID or username - offline votes use username)
        String lookupKey = null;
        if (pendingRewardsManager.hasPendingRewards(uuid)) {
            lookupKey = uuid;
        } else if (pendingRewardsManager.hasPendingRewards(username)) {
            lookupKey = username;
        }

        if (lookupKey == null) {
            return;
        }

        // Get all pending rewards for this player
        List<PendingReward> pendingRewards = pendingRewardsManager.getPendingRewards(lookupKey);
        int totalRewards = pendingRewards.size();

        logger.at(Level.INFO).log("Delivering %d pending vote rewards to %s (key: %s)", totalRewards, username, lookupKey);

        // Clear pending rewards first to prevent double-delivery on reconnect
        pendingRewardsManager.clearPendingRewards(lookupKey);

        // Delay command execution to ensure player is fully connected
        // PlayerConnectEvent fires early - give the server time to fully load the player
        final String finalUsername = username;
        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
            int commandsExecuted = 0;
            for (PendingReward reward : pendingRewards) {
                List<String> commands = reward.getCommands();
                for (String command : commands) {
                    logger.at(Level.INFO).log("Executing pending reward command: %s", command);
                    try {
                        executeCommand(command);
                        commandsExecuted++;
                    } catch (Exception e) {
                        logger.at(Level.SEVERE).log("Failed to execute command '%s': %s", command, e.getMessage());
                    }
                }
            }
            logger.at(Level.INFO).log("Successfully delivered %d commands from %d pending rewards to %s",
                    commandsExecuted, totalRewards, finalUsername);
        });

        // Notify the player about their rewards (immediate)
        String message = "You received " + totalRewards + " pending vote reward"
                + (totalRewards > 1 ? "s" : "") + "! Thank you for voting!";
        playerRef.sendMessage(Message.raw(message));
    }
}
