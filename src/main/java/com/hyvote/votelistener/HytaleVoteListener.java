package com.hyvote.votelistener;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hyvote.votelistener.config.ConfigManager;
import com.hyvote.votelistener.data.PendingRewardsManager;
import com.hyvote.votelistener.data.VoteDataManager;
import com.hyvote.votelistener.listener.PlayerJoinListener;
import com.hyvote.votelistener.command.ClaimVotesCommand;
import com.hyvote.votelistener.listener.VoteListener;
import com.hyvote.votifier.event.VoteEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.logging.Level;

/**
 * HytaleVoteListener - Vote reward plugin for Hytale servers.
 *
 * Listens for vote events from HytaleVotifier and delivers configurable
 * rewards including commands, random rewards, vote streaks, and milestone bonuses.
 */
public class HytaleVoteListener extends JavaPlugin {

    private ConfigManager configManager;
    private VoteDataManager voteDataManager;
    private PendingRewardsManager pendingRewardsManager;
    private VoteListener voteListener;
    private PlayerJoinListener playerJoinListener;
    private ClaimVotesCommand claimVotesCommand;

    /**
     * Plugin constructor called by the server during plugin loading.
     *
     * @param pluginInit Initialization context provided by the server
     */
    public HytaleVoteListener(@Nonnull JavaPluginInit pluginInit) {
        super(pluginInit);
    }

    /**
     * Called during plugin setup phase before start.
     * Used for loading configuration and initializing services.
     */
    @Override
    protected void setup() {
        getLogger().at(Level.INFO).log("Setting up HytaleVoteListener...");

        // Initialize and load configuration using the plugin's data directory
        Path dataFolder = getDataDirectory();
        configManager = new ConfigManager(dataFolder, getLogger());
        configManager.loadConfig();

        // Initialize and load vote data
        voteDataManager = new VoteDataManager(dataFolder, getLogger());
        voteDataManager.loadVoteData();

        // Initialize and load pending rewards
        pendingRewardsManager = new PendingRewardsManager(dataFolder, getLogger());
        pendingRewardsManager.loadPendingRewards();
    }

    /**
     * Gets the configuration manager for accessing plugin settings.
     *
     * @return The ConfigManager instance
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Gets the vote data manager for accessing player vote statistics.
     *
     * @return The VoteDataManager instance
     */
    public VoteDataManager getVoteDataManager() {
        return voteDataManager;
    }

    /**
     * Gets the pending rewards manager for handling offline player rewards.
     *
     * @return The PendingRewardsManager instance
     */
    public PendingRewardsManager getPendingRewardsManager() {
        return pendingRewardsManager;
    }

    /**
     * Called when the plugin is enabled and ready to operate.
     * Registers event listeners and starts services.
     */
    @Override
    protected void start() {
        String version = getManifest().getVersion().toString();
        getLogger().at(Level.INFO).log("HytaleVoteListener v%s enabled", version);

        // Create vote listener
        voteListener = new VoteListener(this, configManager.getConfig(), voteDataManager, pendingRewardsManager);

        // Register globally for VoteEvent (HytaleVotifier dispatches with its plugin class as key)
        getEventRegistry().registerGlobal(VoteEvent.class, voteListener::onVote);
        getLogger().at(Level.INFO).log("Registered vote event listener (global)");

        // Create player join listener for pending reward delivery
        playerJoinListener = new PlayerJoinListener(this, pendingRewardsManager);

        // Register for PlayerConnectEvent
        getEventRegistry().register(PlayerConnectEvent.class, playerJoinListener::onPlayerConnect);
        getLogger().at(Level.INFO).log("Registered player join listener for pending reward delivery");

        // Create and register /claimvotes command for manual reward claiming
        claimVotesCommand = new ClaimVotesCommand(this, pendingRewardsManager);
        getCommandRegistry().registerCommand(claimVotesCommand);
        getLogger().at(Level.INFO).log("Registered /claimvotes command");
    }

    /**
     * Called when the plugin is being disabled/shutdown.
     * Performs cleanup and saves any pending data.
     */
    @Override
    protected void shutdown() {
        if (voteListener != null) {
            getLogger().at(Level.INFO).log("Unregistered vote event listener");
        }
        if (playerJoinListener != null) {
            getLogger().at(Level.INFO).log("Unregistered player join listener");
        }
        if (claimVotesCommand != null) {
            getLogger().at(Level.INFO).log("Unregistered /claimvotes command");
        }
        if (voteDataManager != null) {
            getLogger().at(Level.INFO).log("Vote data saved (immediate persistence mode)");
        }
        if (pendingRewardsManager != null) {
            getLogger().at(Level.INFO).log("Pending rewards data saved (immediate persistence mode)");
        }
        getLogger().at(Level.INFO).log("HytaleVoteListener disabled");
    }
}
