package com.hyvote.votelistener.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manages pending rewards for offline players with JSON persistence.
 *
 * Handles loading, saving, and managing pending rewards that need to be
 * delivered when offline players return to the server. Follows the same
 * pattern as VoteDataManager for consistency.
 */
public class PendingRewardsManager {

    private static final String PENDING_REWARDS_FILE_NAME = "pending-rewards.json";

    private final Path pluginDataFolder;
    private final Logger logger;
    private final Gson gson;
    private Map<String, List<PendingReward>> pendingRewardsMap;

    /**
     * Creates a new PendingRewardsManager.
     *
     * @param pluginDataFolder The plugin's data directory for storing pending-rewards.json
     * @param logger The logger for info and error messages
     */
    public PendingRewardsManager(Path pluginDataFolder, Logger logger) {
        this.pluginDataFolder = pluginDataFolder;
        this.logger = logger;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.pendingRewardsMap = new HashMap<>();
    }

    /**
     * Loads pending rewards from pending-rewards.json.
     *
     * If pending-rewards.json does not exist, creates an empty data file.
     */
    public void loadPendingRewards() {
        Path pendingRewardsPath = pluginDataFolder.resolve(PENDING_REWARDS_FILE_NAME);

        if (!Files.exists(pendingRewardsPath)) {
            logger.info("Pending rewards file not found, creating empty pending-rewards.json");
            savePendingRewards();
            return;
        }

        try {
            String json = Files.readString(pendingRewardsPath);
            Type mapType = new TypeToken<Map<String, List<PendingReward>>>(){}.getType();
            Map<String, List<PendingReward>> loadedData = gson.fromJson(json, mapType);

            if (loadedData != null) {
                pendingRewardsMap = loadedData;
                int totalPending = pendingRewardsMap.values().stream()
                        .mapToInt(List::size)
                        .sum();
                logger.info("Loaded pending rewards for " + pendingRewardsMap.size()
                        + " players (" + totalPending + " total rewards) from " + pendingRewardsPath);
            } else {
                pendingRewardsMap = new HashMap<>();
                logger.info("Pending rewards file was empty, initialized empty map");
            }
        } catch (IOException e) {
            logger.severe("Failed to load pending-rewards.json: " + e.getMessage());
            pendingRewardsMap = new HashMap<>();
        }
    }

    /**
     * Saves pending rewards to pending-rewards.json.
     *
     * Creates the plugin data directory if it does not exist.
     */
    public void savePendingRewards() {
        try {
            if (!Files.exists(pluginDataFolder)) {
                Files.createDirectories(pluginDataFolder);
                logger.info("Created plugin data folder: " + pluginDataFolder);
            }

            Path pendingRewardsPath = pluginDataFolder.resolve(PENDING_REWARDS_FILE_NAME);
            String json = gson.toJson(pendingRewardsMap);
            Files.writeString(pendingRewardsPath, json);
        } catch (IOException e) {
            logger.severe("Failed to save pending-rewards.json: " + e.getMessage());
        }
    }

    /**
     * Adds a pending reward for a player.
     *
     * Creates a new list for the player if they have no pending rewards yet.
     * Immediately saves to disk for data integrity.
     *
     * @param uuid Player UUID as string
     * @param reward The pending reward to add
     */
    public void addPendingReward(String uuid, PendingReward reward) {
        List<PendingReward> rewards = pendingRewardsMap.computeIfAbsent(uuid, k -> new ArrayList<>());
        rewards.add(reward);
        savePendingRewards();
        logger.info("Added pending reward for " + reward.getUsername()
                + " from " + reward.getServiceName() + " (" + reward.getCommands().size() + " commands)");
    }

    /**
     * Gets all pending rewards for a player.
     *
     * @param uuid Player UUID as string
     * @return List of pending rewards, or empty list if none
     */
    public List<PendingReward> getPendingRewards(String uuid) {
        return pendingRewardsMap.getOrDefault(uuid, new ArrayList<>());
    }

    /**
     * Clears all pending rewards for a player.
     *
     * Used after rewards have been successfully delivered.
     * Immediately saves to disk for data integrity.
     *
     * @param uuid Player UUID as string
     */
    public void clearPendingRewards(String uuid) {
        List<PendingReward> removed = pendingRewardsMap.remove(uuid);
        if (removed != null && !removed.isEmpty()) {
            savePendingRewards();
            logger.info("Cleared " + removed.size() + " pending rewards for player " + uuid);
        }
    }

    /**
     * Checks if a player has any pending rewards.
     *
     * @param uuid Player UUID as string
     * @return true if the player has pending rewards, false otherwise
     */
    public boolean hasPendingRewards(String uuid) {
        List<PendingReward> rewards = pendingRewardsMap.get(uuid);
        return rewards != null && !rewards.isEmpty();
    }
}
