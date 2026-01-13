package com.hyvote.votelistener.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hypixel.hytale.logger.HytaleLogger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Manages player vote data persistence and streak calculation.
 *
 * Handles loading and saving vote data from JSON file, and provides
 * methods for recording votes with automatic streak tracking.
 */
public class VoteDataManager {

    private static final String VOTE_DATA_FILE_NAME = "vote-data.json";

    private final Path pluginDataFolder;
    private final HytaleLogger logger;
    private final Gson gson;
    private Map<String, PlayerVoteData> voteDataMap;

    /**
     * Creates a new VoteDataManager.
     *
     * @param pluginDataFolder The plugin's data directory for storing vote-data.json
     * @param logger The logger for info and error messages
     */
    public VoteDataManager(Path pluginDataFolder, HytaleLogger logger) {
        this.pluginDataFolder = pluginDataFolder;
        this.logger = logger;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.voteDataMap = new HashMap<>();
    }

    /**
     * Loads vote data from vote-data.json.
     *
     * If vote-data.json does not exist, creates an empty data file.
     */
    public void loadVoteData() {
        Path voteDataPath = pluginDataFolder.resolve(VOTE_DATA_FILE_NAME);

        if (!Files.exists(voteDataPath)) {
            logger.at(Level.INFO).log("Vote data file not found, creating empty vote-data.json");
            saveVoteData();
            return;
        }

        try {
            String json = Files.readString(voteDataPath);
            Type mapType = new TypeToken<Map<String, PlayerVoteData>>(){}.getType();
            Map<String, PlayerVoteData> loadedData = gson.fromJson(json, mapType);

            if (loadedData != null) {
                voteDataMap = loadedData;
                logger.at(Level.INFO).log("Loaded vote data for " + voteDataMap.size() + " players from " + voteDataPath);
            } else {
                voteDataMap = new HashMap<>();
                logger.at(Level.INFO).log("Vote data file was empty, initialized empty map");
            }
        } catch (IOException e) {
            logger.at(Level.SEVERE).log("Failed to load vote-data.json: " + e.getMessage());
            voteDataMap = new HashMap<>();
        }
    }

    /**
     * Saves vote data to vote-data.json.
     *
     * Creates the plugin data directory if it does not exist.
     */
    public void saveVoteData() {
        try {
            if (!Files.exists(pluginDataFolder)) {
                Files.createDirectories(pluginDataFolder);
                logger.at(Level.INFO).log("Created plugin data folder: " + pluginDataFolder);
            }

            Path voteDataPath = pluginDataFolder.resolve(VOTE_DATA_FILE_NAME);
            String json = gson.toJson(voteDataMap);
            Files.writeString(voteDataPath, json);
        } catch (IOException e) {
            logger.at(Level.SEVERE).log("Failed to save vote-data.json: " + e.getMessage());
        }
    }

    /**
     * Gets existing player vote data or creates a new record.
     *
     * @param uuid Player UUID as string
     * @param username Player username
     * @return PlayerVoteData for the specified player
     */
    public PlayerVoteData getOrCreatePlayerData(String uuid, String username) {
        PlayerVoteData data = voteDataMap.get(uuid);

        if (data == null) {
            data = new PlayerVoteData(uuid, username, 0, 0, 0);
            voteDataMap.put(uuid, data);
        } else {
            // Update username in case it changed
            data.setUsername(username);
        }

        return data;
    }

    /**
     * Records a vote for a player and updates their streak.
     *
     * Streak logic:
     * - If last vote was today: streak unchanged (already voted today)
     * - If last vote was yesterday: streak incremented
     * - If last vote was older than yesterday: streak reset to 1
     *
     * @param uuid Player UUID as string
     * @param username Player username
     * @return Updated PlayerVoteData with new vote recorded
     */
    public PlayerVoteData recordVote(String uuid, String username) {
        PlayerVoteData data = getOrCreatePlayerData(uuid, username);

        // Increment total votes
        data.setTotalVotes(data.getTotalVotes() + 1);

        // Calculate streak
        long now = System.currentTimeMillis();
        long lastVoteTimestamp = data.getLastVoteTimestamp();

        if (lastVoteTimestamp == 0) {
            // First vote ever
            data.setCurrentStreak(1);
        } else {
            LocalDate today = Instant.ofEpochMilli(now)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate lastVoteDate = Instant.ofEpochMilli(lastVoteTimestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (today.equals(lastVoteDate)) {
                // Same day - streak unchanged (already voted today)
                // Don't change streak
            } else if (today.minusDays(1).equals(lastVoteDate)) {
                // Yesterday - increment streak
                data.setCurrentStreak(data.getCurrentStreak() + 1);
            } else {
                // Older than yesterday - reset streak
                data.setCurrentStreak(1);
            }
        }

        // Update last vote timestamp
        data.setLastVoteTimestamp(now);

        // Save immediately for persistence
        saveVoteData();

        logger.at(Level.INFO).log("Recorded vote for " + username + " - Total: " + data.getTotalVotes()
                + ", Streak: " + data.getCurrentStreak());

        return data;
    }

    /**
     * Gets the vote data map for all players.
     *
     * @return Map of UUID to PlayerVoteData
     */
    public Map<String, PlayerVoteData> getVoteDataMap() {
        return voteDataMap;
    }

    /**
     * Gets player vote data by UUID.
     *
     * @param uuid Player UUID as string
     * @return PlayerVoteData or null if not found
     */
    public PlayerVoteData getPlayerData(String uuid) {
        return voteDataMap.get(uuid);
    }
}
