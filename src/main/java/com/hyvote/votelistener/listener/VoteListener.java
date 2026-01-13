package com.hyvote.votelistener.listener;

import com.hyvote.votifier.event.VoteEvent;
import com.hyvote.votifier.Vote;
import com.hyvote.votelistener.config.Config;
import com.hyvote.votelistener.config.RandomReward;
import com.hyvote.votelistener.config.StreakBonus;
import com.hyvote.votelistener.data.PlayerVoteData;
import com.hyvote.votelistener.data.VoteDataManager;
import com.hyvote.votelistener.reward.RewardSelector;
import com.hyvote.votelistener.util.PlaceholderProcessor;
import com.hypixel.hytale.server.core.Server;
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

    /**
     * Creates a new VoteListener.
     *
     * @param server The server instance for executing commands
     * @param logger The logger for debug and info messages
     * @param config The configuration containing command list and settings
     * @param voteDataManager The vote data manager for tracking streaks and statistics
     */
    public VoteListener(Server server, Logger logger, Config config, VoteDataManager voteDataManager) {
        this.server = server;
        this.logger = logger;
        this.config = config;
        this.voteDataManager = voteDataManager;
    }

    /**
     * Handles incoming vote events from HytaleVotifier.
     *
     * @param event The vote event containing vote details
     */
    public void onVote(VoteEvent event) {
        Vote vote = event.getVote();
        String username = vote.getUsername();
        String serviceName = vote.getServiceName();

        logger.info("Vote received from " + serviceName + " for player: " + username);

        // Record vote and get updated player data with streak info
        PlayerVoteData playerData = voteDataManager.recordVote(vote.getUuid(), username);
        int currentStreak = playerData.getCurrentStreak();
        int totalVotes = playerData.getTotalVotes();

        // Execute all configured commands with placeholder replacement
        List<String> commands = config.getCommands();
        for (String command : commands) {
            String processedCommand = PlaceholderProcessor.process(command, vote, null, currentStreak, totalVotes);
            server.executeCommand(processedCommand);

            if (config.isDebugMode()) {
                logger.info("[Debug] Executed command: " + processedCommand);
            }
        }

        // Execute random reward commands if enabled
        if (config.isRandomRewardsEnabled()) {
            RandomReward selectedReward = RewardSelector.select(config.getRandomRewards());
            if (selectedReward != null) {
                logger.info("Selected random reward: " + selectedReward.getName());

                for (String rewardCommand : selectedReward.getCommands()) {
                    String processedRewardCommand = PlaceholderProcessor.process(
                        rewardCommand, vote, selectedReward.getName(), currentStreak, totalVotes);
                    server.executeCommand(processedRewardCommand);

                    if (config.isDebugMode()) {
                        logger.info("[Debug] Executed reward command: " + processedRewardCommand);
                    }
                }
            }
        }

        // Execute streak bonus rewards if enabled
        if (config.isStreakBonusEnabled()) {
            for (StreakBonus streakBonus : config.getStreakBonuses()) {
                if (currentStreak == streakBonus.getStreakDays()) {
                    logger.info("Awarding streak bonus: " + streakBonus.getName());

                    for (String bonusCommand : streakBonus.getCommands()) {
                        String processedBonusCommand = PlaceholderProcessor.process(
                            bonusCommand, vote, streakBonus.getName(), currentStreak, totalVotes);
                        server.executeCommand(processedBonusCommand);

                        if (config.isDebugMode()) {
                            logger.info("[Debug] Executed streak bonus command: " + processedBonusCommand);
                        }
                    }
                    break; // Only award one streak bonus per vote
                }
            }
        }

        // Handle broadcast vote setting (additional announcement if enabled)
        if (config.isBroadcastVote()) {
            String broadcastCommand = "say " + username + " voted on " + serviceName + "!";
            server.executeCommand(broadcastCommand);

            if (config.isDebugMode()) {
                logger.info("[Debug] Broadcast vote: " + broadcastCommand);
            }
        }
    }
}
