package com.hyvote.votelistener.listener;

import com.hyvote.votifier.event.VoteEvent;
import com.hyvote.votifier.Vote;
import com.hypixel.hytale.server.core.Server;
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

    /**
     * Creates a new VoteListener.
     *
     * @param server The server instance for executing commands
     * @param logger The logger for debug and info messages
     */
    public VoteListener(Server server, Logger logger) {
        this.server = server;
        this.logger = logger;
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

        // Execute test command - broadcasts message to server
        String command = "say " + username + " voted on " + serviceName + "!";
        server.executeCommand(command);
    }
}
