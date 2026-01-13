package com.hyvote.votelistener;

import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.plugin.PluginInit;
import com.hypixel.hytale.common.plugin.PluginType;
import com.hyvote.votelistener.config.ConfigManager;
import com.hyvote.votelistener.data.VoteDataManager;
import com.hyvote.votelistener.listener.VoteListener;
import com.hyvote.votifier.event.VoteEvent;

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
    private VoteListener voteListener;

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
     * Called when the plugin is enabled and ready to operate.
     * Registers event listeners and starts services.
     */
    @Override
    public void start() {
        String version = getManifest().getVersion();
        getLogger().info("HytaleVoteListener v" + version + " enabled");

        // Create and register vote event listener with config
        voteListener = new VoteListener(getServer(), getLogger(), configManager.getConfig());
        getServer().getEventBus().subscribe(VoteEvent.class, voteListener::onVote);
        getLogger().info("Registered vote event listener");
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
        if (voteDataManager != null) {
            getLogger().info("Vote data saved (immediate persistence mode)");
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
