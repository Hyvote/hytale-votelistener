package com.hyvote.votelistener.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Manages loading and saving of plugin configuration.
 *
 * Handles JSON config file I/O using Gson, including default config
 * generation when config.json does not exist.
 */
public class ConfigManager {

    private static final String CONFIG_FILE_NAME = "config.json";

    private final Path pluginDataFolder;
    private final Logger logger;
    private final Gson gson;
    private Config config;

    /**
     * Creates a new ConfigManager.
     *
     * @param pluginDataFolder The plugin's data directory (e.g., plugins/HytaleVoteListener/)
     * @param logger The logger for info and error messages
     */
    public ConfigManager(Path pluginDataFolder, Logger logger) {
        this.pluginDataFolder = pluginDataFolder;
        this.logger = logger;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Loads the configuration from config.json.
     *
     * If config.json does not exist, creates a default config first.
     *
     * @return The loaded Config object
     */
    public Config loadConfig() {
        Path configPath = pluginDataFolder.resolve(CONFIG_FILE_NAME);

        if (!Files.exists(configPath)) {
            logger.info("Config file not found, creating default config.json");
            saveDefaultConfig();
        }

        try {
            String json = Files.readString(configPath);
            config = gson.fromJson(json, Config.class);
            logger.info("Loaded configuration from " + configPath);
        } catch (IOException e) {
            logger.severe("Failed to load config.json: " + e.getMessage());
            config = new Config(); // Fallback to defaults
        }

        return config;
    }

    /**
     * Saves a default configuration file.
     *
     * Creates the plugin data directory if it does not exist,
     * then writes a default config.json with example values.
     */
    public void saveDefaultConfig() {
        try {
            if (!Files.exists(pluginDataFolder)) {
                Files.createDirectories(pluginDataFolder);
                logger.info("Created plugin data folder: " + pluginDataFolder);
            }

            Path configPath = pluginDataFolder.resolve(CONFIG_FILE_NAME);
            Config defaultConfig = new Config();
            String json = gson.toJson(defaultConfig);
            Files.writeString(configPath, json);
            logger.info("Saved default config.json");
        } catch (IOException e) {
            logger.severe("Failed to save default config.json: " + e.getMessage());
        }
    }

    /**
     * Gets the currently loaded configuration.
     *
     * Must call loadConfig() before using this method.
     *
     * @return The loaded Config object, or null if not yet loaded
     */
    public Config getConfig() {
        return config;
    }
}
