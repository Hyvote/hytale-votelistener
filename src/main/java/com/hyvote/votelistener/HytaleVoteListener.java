package com.hyvote.votelistener;

import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.plugin.PluginInit;
import com.hypixel.hytale.common.plugin.PluginType;

/**
 * HytaleVoteListener - Vote reward plugin for Hytale servers.
 *
 * Listens for vote events from HytaleVotifier and delivers configurable
 * rewards including commands, random rewards, vote streaks, and milestone bonuses.
 */
public class HytaleVoteListener extends PluginBase {

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
    }

    /**
     * Called when the plugin is enabled and ready to operate.
     * Registers event listeners and starts services.
     */
    @Override
    public void start() {
        String version = getManifest().getVersion();
        getLogger().info("HytaleVoteListener v" + version + " enabled");
    }

    /**
     * Called when the plugin is being disabled/shutdown.
     * Performs cleanup and saves any pending data.
     */
    @Override
    public void shutdown() {
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
