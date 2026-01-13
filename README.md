# HytaleVoteListener

A vote reward plugin for Hytale servers. Listens for votes from [HytaleVotifier](https://github.com/Hyvote/hytale-votifier) and rewards players with configurable commands.

## Installation

1. Download the latest `hytale-votelistener-1.0.0-SNAPSHOT.jar` from [Releases](https://github.com/Hyvote/hytale-votelistener/releases)
2. Place the JAR file in your server's `mods/` folder
3. Install [HytaleVotifier](https://github.com/Hyvote/hytale-votifier) if you haven't already
4. Start your server - a default `config.json` will be generated
5. Configure your rewards in `plugins/HytaleVoteListener/config.json`
6. Restart the server or reload the plugin

## Configuration

The plugin creates a `config.json` file in `plugins/HytaleVoteListener/` with the following options:

### Basic Settings

```json
{
  "commands": [
    "say %player% has voted on %service%",
    "give %player% Weapon_Staff_Onyxium"
  ],
  "broadcastVote": false,
  "debugMode": false
}
```

| Option | Type | Description |
|--------|------|-------------|
| `commands` | Array | List of commands to execute when a player votes. Supports placeholders (see below). |
| `broadcastVote` | Boolean | Currently unused. Use a `say` command in your commands list instead. |
| `debugMode` | Boolean | Enable verbose logging for troubleshooting. |

### Placeholders

Use these placeholders in your commands - they will be replaced with actual values:

| Placeholder | Description |
|-------------|-------------|
| `%player%` | The username of the player who voted |
| `%service%` | The name of the voting site |
| `%uuid%` | The player's UUID (if available) |
| `%timestamp%` | When the vote was received |
| `%streak%` | The player's current vote streak (consecutive days) |
| `%totalvotes%` | The player's total lifetime votes |
| `%reward%` | The name of the random reward tier received |

### Random Rewards

Give players a chance to receive bonus rewards with weighted probability.

```json
{
  "randomRewardsEnabled": true,
  "randomRewards": [
    {
      "name": "common",
      "chance": 70.0,
      "commands": ["give %player% Consumable_Apple 5"]
    },
    {
      "name": "rare",
      "chance": 25.0,
      "commands": ["give %player% Armor_Leather_Chest"]
    },
    {
      "name": "legendary",
      "chance": 5.0,
      "commands": [
        "give %player% Weapon_Sword_Legendary",
        "say %player% received a legendary vote reward!"
      ]
    }
  ]
}
```

| Option | Type | Description |
|--------|------|-------------|
| `randomRewardsEnabled` | Boolean | Enable or disable random rewards. |
| `randomRewards` | Array | List of reward tiers with weighted chances. |
| `name` | String | Display name for the reward tier. |
| `chance` | Number | Weight for this tier (does not need to sum to 100). |
| `commands` | Array | Commands to execute if this tier is selected. |

**How chances work:** The chances are weights, not percentages. If you have rewards with chances 70, 25, and 5, the total is 100, so they work out to 70%, 25%, and 5%. But if you had chances 7, 2.5, and 0.5, it would work the same way.

### Vote Streak Bonuses

Reward players for voting on consecutive days.

```json
{
  "streakBonusEnabled": true,
  "streakBonuses": [
    {
      "streakDays": 3,
      "name": "3-day",
      "commands": ["give %player% Consumable_Potion_Health"]
    },
    {
      "streakDays": 7,
      "name": "weekly",
      "commands": [
        "give %player% Tool_Pickaxe_Iron",
        "say %player% has a %streak%-day vote streak!"
      ]
    },
    {
      "streakDays": 30,
      "name": "monthly",
      "commands": ["give %player% Armor_Set_Rare"]
    }
  ]
}
```

| Option | Type | Description |
|--------|------|-------------|
| `streakBonusEnabled` | Boolean | Enable or disable streak bonuses. |
| `streakBonuses` | Array | List of streak milestones. |
| `streakDays` | Number | The exact streak day to trigger this bonus (e.g., 7 = on their 7th consecutive day). |
| `name` | String | Display name for this streak bonus. |
| `commands` | Array | Commands to execute when the player reaches this streak. |

**Note:** Streak bonuses trigger when the player's streak exactly matches `streakDays`. A player with a 7-day streak will receive the 7-day bonus but not the 3-day bonus on that vote.

### Milestone Bonuses

Reward players for reaching total vote milestones.

```json
{
  "milestoneBonusEnabled": true,
  "milestoneBonuses": [
    {
      "votesRequired": 10,
      "name": "first-ten",
      "commands": ["give %player% Consumable_Potion_Health 3"]
    },
    {
      "votesRequired": 50,
      "name": "fifty",
      "commands": ["give %player% Weapon_Bow_Rare"]
    },
    {
      "votesRequired": 100,
      "name": "century",
      "commands": [
        "give %player% Mount_Horse_Rare",
        "say %player% reached %totalvotes% total votes!"
      ]
    }
  ]
}
```

| Option | Type | Description |
|--------|------|-------------|
| `milestoneBonusEnabled` | Boolean | Enable or disable milestone bonuses. |
| `milestoneBonuses` | Array | List of vote count milestones. |
| `votesRequired` | Number | The exact total vote count to trigger this bonus. |
| `name` | String | Display name for this milestone. |
| `commands` | Array | Commands to execute when the player reaches this milestone. |

## Complete Example Configuration

```json
{
  "commands": [
    "say %player% has voted on %service%",
    "give %player% Weapon_Staff_Onyxium"
  ],
  "broadcastVote": false,
  "debugMode": false,
  "randomRewardsEnabled": true,
  "randomRewards": [
    {
      "name": "common",
      "chance": 70.0,
      "commands": ["give %player% Consumable_Apple 5"]
    },
    {
      "name": "rare",
      "chance": 25.0,
      "commands": ["give %player% Armor_Leather_Chest"]
    },
    {
      "name": "legendary",
      "chance": 5.0,
      "commands": ["give %player% Weapon_Sword_Legendary"]
    }
  ],
  "streakBonusEnabled": true,
  "streakBonuses": [
    {
      "streakDays": 7,
      "name": "weekly",
      "commands": ["give %player% Tool_Pickaxe_Iron"]
    }
  ],
  "milestoneBonusEnabled": true,
  "milestoneBonuses": [
    {
      "votesRequired": 100,
      "name": "century",
      "commands": ["give %player% Mount_Horse_Rare"]
    }
  ]
}
```

## Offline Voting

If a player votes while offline, their rewards are stored and delivered automatically when they next join the server. Players can also use the `/claimvotes` command to manually claim any pending rewards.

## Data Files

The plugin stores data in the `plugins/HytaleVoteListener/` folder:

| File | Description |
|------|-------------|
| `config.json` | Plugin configuration |
| `vote_data.json` | Player vote statistics (streaks, total votes) |
| `pending_rewards.json` | Queued rewards for offline players |

## Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/claimvotes` | `hyvote.claimvotes` | Manually claim pending vote rewards |

## Requirements

- Hytale Server
- [HytaleVotifier](https://github.com/Hyvote/hytale-votifier) plugin

## Support

For issues and feature requests, please open an issue on [GitHub](https://github.com/Hyvote/hytale-votelistener/issues).

## License

MIT License

Copyright (c) 2026 Hyvote

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
