---
phase: 03-configuration-system
plan: 01
subsystem: config
tags: [hytale-plugin, json-config, gson, configuration, java]

# Dependency graph
requires:
  - phase: 02-01
    provides: Plugin skeleton with setup(), start(), shutdown() lifecycle methods and VoteListener
provides:
  - Config model class with commands list, broadcastVote, debugMode fields
  - ConfigManager for JSON file load/save with Gson
  - Default config.json generation on first run
  - ConfigManager integration in plugin setup() lifecycle
affects: [03-02-placeholder-replacement, 04-random-rewards, 05-streaks, 06-milestones, 07-offline-storage]

# Tech tracking
tech-stack:
  added: [gson-2.10.1]
  patterns: [json-config-model, config-manager-pattern]

key-files:
  created:
    - src/main/java/com/hyvote/votelistener/config/Config.java
    - src/main/java/com/hyvote/votelistener/config/ConfigManager.java
  modified:
    - src/main/java/com/hyvote/votelistener/HytaleVoteListener.java

key-decisions:
  - "Gson with setPrettyPrinting() for human-readable config files"
  - "Config model with sensible defaults in constructor"
  - "ConfigManager takes Path and Logger via constructor for testability"
  - "Default config.json created automatically on first run"

patterns-established:
  - "Config subpackage: com.hyvote.votelistener.config for all configuration classes"
  - "ConfigManager pattern: loadConfig() creates default if missing, then parses JSON"
  - "Plugin data folder convention: plugins/{pluginName}/"

issues-created: []

# Metrics
duration: 3min
completed: 2026-01-13
---

# Phase 3: Configuration System - Plan 01 Summary

**JSON configuration system with Config model and ConfigManager for load/save operations**

## Performance

- **Duration:** 3 min
- **Started:** 2026-01-13
- **Completed:** 2026-01-13
- **Tasks:** 2
- **Files modified:** 3

## Accomplishments
- Config model class created with commands list, broadcastVote, and debugMode fields with sensible defaults
- ConfigManager handles JSON file I/O using Gson with pretty printing
- Default config.json automatically generated on first run with example vote reward commands
- Plugin setup() now initializes ConfigManager and loads configuration

## Task Commits

Each task was committed atomically:

1. **Task 1: Add Gson dependency and create Config model class** - `5acdf88` (feat)
2. **Task 2: Create ConfigManager with load/save and integrate into plugin lifecycle** - `913e6bd` (feat)

**Plan metadata:** pending

## Files Created/Modified
- `src/main/java/com/hyvote/votelistener/config/Config.java` - Config model with commands, broadcastVote, debugMode
- `src/main/java/com/hyvote/votelistener/config/ConfigManager.java` - JSON config manager with loadConfig(), saveDefaultConfig(), getConfig()
- `src/main/java/com/hyvote/votelistener/HytaleVoteListener.java` - Added ConfigManager field, setup() integration, getConfigManager() getter

## Default Config JSON Structure

```json
{
  "commands": [
    "say %player% voted on %service%!",
    "give %player% diamond 1"
  ],
  "broadcastVote": true,
  "debugMode": false
}
```

## Decisions Made
- Used Gson 2.10.1 (already in pom.xml from prior setup) with pretty printing for human-readable config
- Config model uses constructor defaults rather than static factory for simplicity
- ConfigManager creates plugin data folder if not exists before saving
- Path.of("plugins", getName()) used as plugin data folder (fallback since PluginBase.getDataFolder() may not exist)

## Deviations from Plan

- Gson dependency was already present in pom.xml from prior phase (shade plugin configured)
- No deviation - proceeded with Config class creation as planned

## Issues Encountered
None

## Next Phase Readiness
- Config model ready for placeholder replacement system (03-02)
- ConfigManager.getConfig().getCommands() provides command list for execution
- %player%, %service%, %uuid% placeholders documented in config
- VoteListener can be updated to use config commands instead of hardcoded "say" command

---
*Phase: 03-configuration-system*
*Completed: 2026-01-13*
