---
phase: 07-offline-vote-storage
plan: 01
subsystem: data
tags: [pending-rewards, offline-storage, json-persistence]

# Dependency graph
requires:
  - phase: 05-01
    provides: VoteDataManager pattern for JSON file I/O, data subpackage structure
  - phase: 01-01
    provides: GSON shaded dependency
provides:
  - PendingReward immutable model for storing offline player rewards
  - PendingRewardsManager for JSON file persistence (pending-rewards.json)
  - load/save/add/get/clear/has methods for reward management
  - Plugin lifecycle integration via getPendingRewardsManager()
affects: [07-02-offline-detection, 07-03-reward-delivery, 08-claimvotes-command]

# Tech tracking
tech-stack:
  added: []
  patterns: [pending-rewards-manager-pattern, immutable-reward-model]

key-files:
  created:
    - src/main/java/com/hyvote/votelistener/data/PendingReward.java
    - src/main/java/com/hyvote/votelistener/data/PendingRewardsManager.java
  modified:
    - src/main/java/com/hyvote/votelistener/HytaleVoteListener.java

key-decisions:
  - "Immutable PendingReward with pre-processed commands to preserve exact rewards from vote time"
  - "Immediate save after each modification for data integrity"
  - "Map<String, List<PendingReward>> keyed by UUID for multiple pending rewards per player"

patterns-established:
  - "Immutable reward model: Commands stored already processed for exact delivery"
  - "PendingRewardsManager pattern: Follows VoteDataManager for consistency"

issues-created: []

# Metrics
duration: 5min
completed: 2026-01-13
---

# Phase 7: Offline Vote Storage - Plan 01 Summary

**Pending rewards data layer with immutable reward model and JSON persistence manager**

## Performance

- **Duration:** 5 min
- **Started:** 2026-01-13
- **Completed:** 2026-01-13
- **Tasks:** 2
- **Files modified:** 3

## Accomplishments
- PendingReward immutable model created with uuid, username, serviceName, timestamp, commands fields
- PendingRewardsManager implements JSON persistence following VoteDataManager pattern exactly
- Plugin lifecycle integration complete with PendingRewardsManager available via getter
- Pre-processed commands design ensures exact reward delivery when player returns

## Task Commits

Each task was committed atomically:

1. **Task 1: Create PendingReward model class** - `9ede985` (feat)
2. **Task 2: Create PendingRewardsManager and integrate into plugin lifecycle** - `9c8fa70` (feat)

**Plan metadata:** [pending]

## Files Created/Modified
- `src/main/java/com/hyvote/votelistener/data/PendingReward.java` - Immutable model for pending offline rewards
- `src/main/java/com/hyvote/votelistener/data/PendingRewardsManager.java` - JSON persistence and reward management
- `src/main/java/com/hyvote/votelistener/HytaleVoteListener.java` - Added PendingRewardsManager lifecycle integration

## Decisions Made
- Used immutable model for PendingReward (no setters) since rewards are created once and never modified
- Commands stored pre-processed (placeholders already replaced) to preserve exact reward calculations from vote time
- Map keyed by UUID string with List<PendingReward> to support multiple pending rewards per player
- Followed VoteDataManager pattern exactly for consistency

## Deviations from Plan

None - plan executed exactly as written

## Issues Encountered
- Pre-existing API mismatches in codebase (HytaleLogger vs java.util.logging.Logger, etc.) cause compile warnings but do not affect new data classes
- These issues existed before this plan and are not blockers for the new functionality

## Next Phase Readiness
- PendingRewardsManager instance available via HytaleVoteListener.getPendingRewardsManager()
- Plan 07-02 can use addPendingReward() to store rewards for offline players
- Plan 07-03 can use getPendingRewards(), hasPendingRewards(), and clearPendingRewards() for delivery
- Phase 8 /claimvotes command can query and deliver pending rewards

---
*Phase: 07-offline-vote-storage*
*Completed: 2026-01-13*
