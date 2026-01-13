package com.hyvote.votelistener.listener;

import com.hyvote.votifier.event.VoteEvent;
import com.hyvote.votifier.Vote;
import com.hyvote.votelistener.config.Config;
import com.hyvote.votelistener.config.RandomReward;
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

    /**
     * Creates a new VoteListener.
     *
     * @param server The server instance for executing commands
     * @param logger The logger for debug and info messages
     * @param config The configuration containing command list and settings
     */
    public VoteListener(Server server, Logger logger, Config config) {
        this.server = server;
        this.logger = logger;
        this.config = config;
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

        // Execute all configured commands with placeholder replacement
        List<String> commands = config.getCommands();
        for (String command : commands) {
            String processedCommand = PlaceholderProcessor.process(command, vote);
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
                        rewardCommand, vote, selectedReward.getName());
                    server.executeCommand(processedRewardCommand);

                    if (config.isDebugMode()) {
                        logger.info("[Debug] Executed reward command: " + processedRewardCommand);
                    }
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
