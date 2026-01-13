package com.hyvote.votelistener.command;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hyvote.votelistener.HytaleVoteListener;
import com.hyvote.votelistener.data.PendingReward;
import com.hyvote.votelistener.data.PendingRewardsManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Command for /claimvotes - allows players to manually claim pending vote rewards.
 *
 * This is an alternative to the automatic delivery on player join,
 * useful for edge cases or servers that prefer manual reward claiming.
 */
public class ClaimVotesCommand extends AbstractCommand {

    private final HytaleVoteListener plugin;
    private final HytaleLogger logger;
    private final PendingRewardsManager pendingRewardsManager;

    /**
     * Creates a new ClaimVotesCommand.
     *
     * @param plugin The plugin instance
     * @param pendingRewardsManager The pending rewards manager for retrieving and clearing rewards
     */
    public ClaimVotesCommand(HytaleVoteListener plugin, PendingRewardsManager pendingRewardsManager) {
        super("claimvotes", "Claim your pending vote rewards");
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.pendingRewardsManager = pendingRewardsManager;

        // Require permission for access
        requirePermission("hyvote.claimvotes");
    }

    /**
     * Executes a command through the command manager.
     *
     * @param command The command string to execute
     */
    private void executeCommand(String command) {
        CommandManager.get().handleCommand((CommandSender) null, command);
    }

    /**
     * Executes the /claimvotes command.
     *
     * Checks permissions, verifies sender is a player, and delivers any pending
     * vote rewards to the player.
     *
     * @param context The command context
     * @return CompletableFuture that completes when command is done
     */
    @Override
    public CompletableFuture<Void> execute(CommandContext context) {
        // Check if sender is a player (not console)
        if (!context.isPlayer()) {
            context.sendMessage(Message.raw("This command can only be used by players"));
            return CompletableFuture.completedFuture(null);
        }

        Player player = context.senderAs(Player.class);
        PlayerRef playerRef = player.getPlayerRef();
        String uuid = playerRef.getUuid().toString();
        String username = playerRef.getUsername();

        // Check if player has pending rewards
        if (!pendingRewardsManager.hasPendingRewards(uuid)) {
            context.sendMessage(Message.raw("You have no pending vote rewards to claim"));
            return CompletableFuture.completedFuture(null);
        }

        // Get all pending rewards for this player
        List<PendingReward> pendingRewards = pendingRewardsManager.getPendingRewards(uuid);
        int totalRewards = pendingRewards.size();

        logger.at(Level.INFO).log("Player %s claiming %d pending vote rewards via /claimvotes",
                username, totalRewards);

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

        // Send success message to player
        context.sendMessage(Message.raw("Claimed " + totalRewards + " pending vote reward"
                + (totalRewards > 1 ? "s" : "") + "! Thank you for voting!"));

        logger.at(Level.INFO).log("Successfully delivered %d commands from %d pending rewards to %s via /claimvotes",
                totalCommandsExecuted, totalRewards, username);

        return CompletableFuture.completedFuture(null);
    }
}
