package com.hyvote.votelistener;

import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.plugin.PluginInit;
import com.hypixel.hytale.common.plugin.PluginType;
import com.hyvote.votelistener.config.ConfigManager;
import com.hyvote.votelistener.data.PendingRewardsManager;
import com.hyvote.votelistener.data.VoteDataManager;
import com.hyvote.votelistener.listener.PlayerJoinListener;
import com.hyvote.votelistener.listener.VoteListener;
import com.hyvote.votifier.event.VoteEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;

import java.nio.file.Path;

/**
 * HytaleVoteListener - Vote reward plugin for Hytale servers.
 *
 * Listens for vote events from HytaleVotifier and delivers configurable
 * rewards including commands, random rewards, vote streaks, and milestone bonuses.
 */
public class HytaleVoteListener extends PluginBase {

    private ConfigManager configManager;
    private VoteDataManager voteDataManager;
    private PendingRewardsManager pendingRewardsManager;
    private VoteListener voteListener;
    private PlayerJoinListener playerJoinListener;

    /**
     * Plugin constructor called by the server during plugin loading.
     *
     * @param pluginInit Initialization context provided by the server
     */
    public HytaleVoteListener(PluginInit pluginInit) {
        super(pluginInit);
    }

    /**
     * Called during plugin setup phase before start.
     * Used for loading configuration and initializing services.
     */
    @Override
    public void setup() {
        getLogger().info("Setting up HytaleVoteListener...");

        // Initialize and load configuration
        Path dataFolder = Path.of("plugins", getName());
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
    public void start() {
        String version = getManifest().getVersion();
        getLogger().info("HytaleVoteListener v" + version + " enabled");

        // Create and register vote event listener with config, data managers for vote tracking and offline rewards
        voteListener = new VoteListener(getServer(), getLogger(), configManager.getConfig(), voteDataManager, pendingRewardsManager);
        getServer().getEventBus().subscribe(VoteEvent.class, voteListener::onVote);
        getLogger().info("Registered vote event listener");

        // Create and register player join listener for pending reward delivery
        playerJoinListener = new PlayerJoinListener(getServer(), getLogger(), pendingRewardsManager);
        getServer().getEventBus().subscribe(PlayerConnectEvent.class, playerJoinListener::onPlayerConnect);
        getLogger().info("Registered player join listener for pending reward delivery");
    }

    /**
     * Called when the plugin is being disabled/shutdown.
     * Performs cleanup and saves any pending data.
     */
    @Override
    public void shutdown() {
        if (voteListener != null) {
            getLogger().info("Unregistered vote event listener");
        }
        if (playerJoinListener != null) {
            getLogger().info("Unregistered player join listener");
        }
        if (voteDataManager != null) {
            getLogger().info("Vote data saved (immediate persistence mode)");
        }
        if (pendingRewardsManager != null) {
            getLogger().info("Pending rewards data saved (immediate persistence mode)");
        }
        getLogger().info("HytaleVoteListener disabled");
    }

    /**
     * Returns the plugin type for the server's plugin system.
     *
     * @return PluginType.PLUGIN indicating this is a standard plugin
     */
    @Override
    public PluginType getType() {
        return PluginType.PLUGIN;
    }
}
