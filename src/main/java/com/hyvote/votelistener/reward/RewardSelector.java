package com.hyvote.votelistener.reward;

import com.hyvote.votelistener.config.RandomReward;

import java.util.List;
import java.util.Random;

/**
 * Utility class for selecting random rewards using weighted probability.
 *
 * <p>The selection algorithm uses weighted random selection where each reward's
 * chance value acts as a weight. Higher weights mean higher probability of selection.
 *
 * <p>Algorithm:
 * <ol>
 *   <li>Sum all chance values to get the total weight</li>
 *   <li>Generate a random value from 0 to totalWeight</li>
 *   <li>Iterate through rewards, accumulating weights</li>
 *   <li>Return the reward where the accumulated weight crosses the random threshold</li>
 * </ol>
 *
 * <p>Example: Given rewards with chances [70, 25, 5], totalWeight = 100.
 * A random value of 45 would select the first reward (70 > 45).
 * A random value of 80 would select the second reward (70 + 25 = 95 > 80).
 * A random value of 98 would select the third reward (70 + 25 + 5 = 100 > 98).
 */
public class RewardSelector {

    private static final Random random = new Random();

    /**
     * Selects a random reward from the given list using weighted probability.
     *
     * <p>Each reward's chance value determines its relative probability of selection.
     * Higher chance values mean more likely to be selected.
     *
     * @param rewards List of rewards to select from
     * @return The selected reward, or null if the list is empty
     */
    public static RandomReward select(List<RandomReward> rewards) {
        // Handle empty list
        if (rewards == null || rewards.isEmpty()) {
            return null;
        }

        // Handle single item - return it directly
        if (rewards.size() == 1) {
            return rewards.get(0);
        }

        // Calculate total weight
        double totalWeight = 0.0;
        for (RandomReward reward : rewards) {
            totalWeight += reward.getChance();
        }

        // Handle all zero chances - return first item to avoid infinite loop
        if (totalWeight <= 0.0) {
            return rewards.get(0);
        }

        // Generate random value from 0 to totalWeight
        double randomValue = random.nextDouble() * totalWeight;

        // Iterate and accumulate weights until threshold is crossed
        double accumulated = 0.0;
        for (RandomReward reward : rewards) {
            accumulated += reward.getChance();
            if (accumulated >= randomValue) {
                return reward;
            }
        }

        // Fallback (should not reach here due to floating point precision)
        return rewards.get(rewards.size() - 1);
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private RewardSelector() {
        // Utility class - do not instantiate
    }
}
