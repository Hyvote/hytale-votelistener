package com.hyvote.votelistener.listener;

import com.hyvote.votifier.event.VoteEvent;
import com.hyvote.votifier.Vote;
import com.hyvote.votelistener.config.Config;
import com.hyvote.votelistener.config.MilestoneBonus;
import com.hyvote.votelistener.config.RandomReward;
import com.hyvote.votelistener.config.StreakBonus;
import com.hyvote.votelistener.data.PendingReward;
import com.hyvote.votelistener.data.PendingRewardsManager;
import com.hyvote.votelistener.data.PlayerVoteData;
import com.hyvote.votelistener.data.VoteDataManager;
import com.hyvote.votelistener.reward.RewardSelector;
import com.hyvote.votelistener.util.PlaceholderProcessor;
import com.hypixel.hytale.server.core.Server;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Listens for vote events from HytaleVotifier and handles reward delivery.
 *
 * This class subscribes to VoteEvent and executes configured commands
 * when a player votes on a voting site.
 */
public class VoteListener {
    private final Server server;
    private final Logger logger;
    private final Config config;
    private final VoteDataManager voteDataManager;
    private final PendingRewardsManager pendingRewardsManager;

    /**
     * Creates a new VoteListener.
     *
     * @param server The server instance for executing commands
     * @param logger The logger for debug and info messages
     * @param config The configuration containing command list and settings
     * @param voteDataManager The vote data manager for tracking streaks and statistics
     * @param pendingRewardsManager The pending rewards manager for offline player rewards
     */
    public VoteListener(Server server, Logger logger, Config config, VoteDataManager voteDataManager,
                        PendingRewardsManager pendingRewardsManager) {
        this.server = server;
        this.logger = logger;
        this.config = config;
        this.voteDataManager = voteDataManager;
        this.pendingRewardsManager = pendingRewardsManager;
    }

    /**
     * Checks if a player is currently online on the server.
     *
     * @param username The username to check
     * @return true if the player is online, false otherwise
     */
    private boolean isPlayerOnline(String username) {
        // Try to find player by name in the online players list
        return server.getOnlinePlayers().stream()
                .anyMatch(player -> player.getName().equalsIgnoreCase(username));
    }

    /**
     * Handles incoming vote events from HytaleVotifier.
     *
     * @param event The vote event containing vote details
     */
    public void onVote(VoteEvent event) {
        Vote vote = event.getVote();
        String uuid = vote.getUuid();
        String username = vote.getUsername();
        String serviceName = vote.getServiceName();

        logger.info("Vote received from " + serviceName + " for player: " + username);

        // Record vote and get updated player data with streak info
        PlayerVoteData playerData = voteDataManager.recordVote(uuid, username);
        int currentStreak = playerData.getCurrentStreak();
        int totalVotes = playerData.getTotalVotes();

        // Collect all reward commands before execution
        List<String> allCommands = new ArrayList<>();

        // Add base commands with placeholder replacement
        List<String> commands = config.getCommands();
        for (String command : commands) {
            String processedCommand = PlaceholderProcessor.process(command, vote, null, currentStreak, totalVotes);
            allCommands.add(processedCommand);

            if (config.isDebugMode()) {
                logger.info("[Debug] Queued command: " + processedCommand);
            }
        }

        // Add random reward commands if enabled
        if (config.isRandomRewardsEnabled()) {
            RandomReward selectedReward = RewardSelector.select(config.getRandomRewards());
            if (selectedReward != null) {
                logger.info("Selected random reward: " + selectedReward.getName());

                for (String rewardCommand : selectedReward.getCommands()) {
                    String processedRewardCommand = PlaceholderProcessor.process(
                        rewardCommand, vote, selectedReward.getName(), currentStreak, totalVotes);
                    allCommands.add(processedRewardCommand);

                    if (config.isDebugMode()) {
                        logger.info("[Debug] Queued reward command: " + processedRewardCommand);
                    }
                }
            }
        }

        // Add streak bonus commands if enabled
        if (config.isStreakBonusEnabled()) {
            for (StreakBonus streakBonus : config.getStreakBonuses()) {
                if (currentStreak == streakBonus.getStreakDays()) {
                    logger.info("Awarding streak bonus: " + streakBonus.getName());

                    for (String bonusCommand : streakBonus.getCommands()) {
                        String processedBonusCommand = PlaceholderProcessor.process(
                            bonusCommand, vote, streakBonus.getName(), currentStreak, totalVotes);
                        allCommands.add(processedBonusCommand);

                        if (config.isDebugMode()) {
                            logger.info("[Debug] Queued streak bonus command: " + processedBonusCommand);
                        }
                    }
                    break; // Only award one streak bonus per vote
                }
            }
        }

        // Add milestone bonus commands if enabled
        if (config.isMilestoneBonusEnabled()) {
            for (MilestoneBonus milestoneBonus : config.getMilestoneBonuses()) {
                if (totalVotes == milestoneBonus.getVotesRequired()) {
                    logger.info("Awarding milestone bonus: " + milestoneBonus.getName());

                    for (String bonusCommand : milestoneBonus.getCommands()) {
                        String processedBonusCommand = PlaceholderProcessor.process(
                            bonusCommand, vote, milestoneBonus.getName(), currentStreak, totalVotes);
                        allCommands.add(processedBonusCommand);

                        if (config.isDebugMode()) {
                            logger.info("[Debug] Queued milestone bonus command: " + processedBonusCommand);
                        }
                    }
                    break; // Only award one milestone bonus per vote
                }
            }
        }

        // Check if player is online and either execute or queue rewards
        if (isPlayerOnline(username)) {
            // Player is online - execute all commands immediately
            for (String cmd : allCommands) {
                server.executeCommand(cmd);

                if (config.isDebugMode()) {
                    logger.info("[Debug] Executed command: " + cmd);
                }
            }
        } else {
            // Player is offline - queue rewards for later delivery
            PendingReward pendingReward = new PendingReward(
                uuid,
                username,
                serviceName,
                System.currentTimeMillis(),
                allCommands
            );
            pendingRewardsManager.addPendingReward(uuid, pendingReward);
            logger.info("Player " + username + " is offline, queued " + allCommands.size() + " reward commands for later delivery");
        }

        // Handle broadcast vote setting (always executes immediately - server-wide announcement)
        if (config.isBroadcastVote()) {
            String broadcastCommand = "say " + username + " voted on " + serviceName + "!";
            server.executeCommand(broadcastCommand);

            if (config.isDebugMode()) {
                logger.info("[Debug] Broadcast vote: " + broadcastCommand);
            }
        }
    }
}
