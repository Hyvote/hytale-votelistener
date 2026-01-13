package com.hyvote.votelistener.command;

import com.hypixel.hytale.server.command.CommandExecutor;
import com.hypixel.hytale.server.command.CommandSender;
import com.hypixel.hytale.server.core.Server;
import com.hypixel.hytale.server.core.entity.player.Player;
import com.hyvote.votelistener.data.PendingReward;
import com.hyvote.votelistener.data.PendingRewardsManager;

import java.util.List;
import java.util.logging.Logger;

/**
 * Command executor for /claimvotes command.
 *
 * Allows players to manually claim their pending vote rewards with permission
 * check. This is an alternative to the automatic delivery on player join,
 * useful for edge cases or servers that prefer manual reward claiming.
 */
public class ClaimVotesCommand implements CommandExecutor {

    private final Server server;
    private final Logger logger;
    private final PendingRewardsManager pendingRewardsManager;

    /**
     * Creates a new ClaimVotesCommand.
     *
     * @param server The server instance for executing reward commands
     * @param logger The logger for info and audit messages
     * @param pendingRewardsManager The pending rewards manager for retrieving and clearing rewards
     */
    public ClaimVotesCommand(Server server, Logger logger, PendingRewardsManager pendingRewardsManager) {
        this.server = server;
        this.logger = logger;
        this.pendingRewardsManager = pendingRewardsManager;
    }

    /**
     * Executes the /claimvotes command.
     *
     * Checks permissions, verifies sender is a player, and delivers any pending
     * vote rewards to the player. Returns appropriate messages for each case.
     *
     * @param sender The command sender (must be a player)
     * @param label The command label used
     * @param args Command arguments (not used)
     * @return true if command was handled successfully
     */
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        // Check if sender is a player (not console)
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players");
            return true;
        }

        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();
        String username = player.getName();

        // Check permission
        if (!sender.hasPermission("hyvote.claimvotes")) {
            sender.sendMessage("You don't have permission to use this command");
            return true;
        }

        // Check if player has pending rewards
        if (!pendingRewardsManager.hasPendingRewards(uuid)) {
            sender.sendMessage("You have no pending vote rewards to claim");
            return true;
        }

        // Get all pending rewards for this player
        List<PendingReward> pendingRewards = pendingRewardsManager.getPendingRewards(uuid);
        int totalRewards = pendingRewards.size();

        logger.info("Player " + username + " claiming " + totalRewards + " pending vote rewards via /claimvotes");

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

        // Send success message to player
        sender.sendMessage("Claimed " + totalRewards + " pending vote reward"
                + (totalRewards > 1 ? "s" : "") + "! Thank you for voting!");

        logger.info("Successfully delivered " + totalCommandsExecuted + " commands from "
                + totalRewards + " pending rewards to " + username + " via /claimvotes");

        return true;
    }
}
