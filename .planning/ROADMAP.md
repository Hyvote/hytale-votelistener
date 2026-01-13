# Roadmap: HytaleVoteListener

## Overview

Build a vote reward plugin for Hytale servers that listens to HytaleVotifier events and delivers configurable rewards. Starting with basic event listening and command execution, we'll progressively add random rewards, streak tracking, milestone bonuses, and offline vote handling. The end result is a complete GAListener equivalent for the Hytale ecosystem.

## Domain Expertise

None

## Phases

**Phase Numbering:**
- Integer phases (1, 2, 3): Planned milestone work
- Decimal phases (2.1, 2.2): Urgent insertions (marked with INSERTED)

Decimal phases appear between their surrounding integers in numeric order.

- [x] **Phase 1: Foundation** - Project setup with Maven, manifest.json, main plugin class ✓
- [ ] **Phase 2: Vote Event Listener** - Listen to VoteEvent from HytaleVotifier, basic command execution
- [ ] **Phase 3: Configuration System** - JSON config with command placeholders
- [ ] **Phase 4: Random Rewards** - Weighted random rewards with multiple tiers
- [ ] **Phase 5: Vote Streak Tracking** - Consecutive daily vote tracking per player
- [ ] **Phase 6: Milestone Rewards** - Every-N-votes bonus rewards
- [ ] **Phase 7: Offline Vote Storage** - Pending rewards queue with file-based persistence
- [ ] **Phase 8: Reward Delivery** - Auto-delivery on login and /claimvotes command

## Phase Details

### Phase 1: Foundation
**Goal**: Set up Maven project structure, manifest.json, and main plugin class matching hytale-votifier conventions
**Depends on**: Nothing (first phase)
**Research**: Unlikely (project setup, match existing votifier patterns)
**Plans**: TBD

Plans:
- [x] 01-01: Maven project setup with pom.xml and directory structure ✓
- [x] 01-02: Plugin manifest.json and main class with onEnable/onDisable ✓

### Phase 2: Vote Event Listener
**Goal**: Subscribe to VoteEvent from HytaleVotifier and execute a basic test command
**Depends on**: Phase 1
**Research**: Likely (new integration with HytaleVotifier)
**Research topics**: HytaleVotifier VoteEvent API, EventBus subscription pattern, Vote object structure
**Plans**: TBD

Plans:
- [ ] 02-01: VoteEvent listener registration and basic handling

### Phase 3: Configuration System
**Goal**: JSON configuration file with command lists and placeholder support (%player%, %service%, %uuid%)
**Depends on**: Phase 2
**Research**: Unlikely (standard JSON parsing, established patterns)
**Plans**: TBD

Plans:
- [ ] 03-01: Config file loading with default generation
- [ ] 03-02: Command placeholder replacement system

### Phase 4: Random Rewards
**Goal**: Weighted random reward selection with multiple tiers (common, rare, legendary)
**Depends on**: Phase 3
**Research**: Unlikely (internal logic, no external dependencies)
**Plans**: TBD

Plans:
- [ ] 04-01: Random reward configuration and selection logic
- [ ] 04-02: Tiered rewards with weighted chances

### Phase 5: Vote Streak Tracking
**Goal**: Track consecutive daily votes per player with streak count
**Depends on**: Phase 3
**Research**: Unlikely (internal data structure, date logic)
**Plans**: TBD

Plans:
- [ ] 05-01: Player vote data storage and streak calculation
- [ ] 05-02: Streak bonus rewards configuration

### Phase 6: Milestone Rewards
**Goal**: Bonus rewards at configurable vote milestones (every N votes)
**Depends on**: Phase 5
**Research**: Unlikely (internal logic building on streak tracking)
**Plans**: TBD

Plans:
- [ ] 06-01: Milestone detection and bonus reward execution

### Phase 7: Offline Vote Storage
**Goal**: Queue pending rewards for offline players with JSON file persistence
**Depends on**: Phase 3
**Research**: Unlikely (file-based JSON storage, internal patterns)
**Plans**: TBD

Plans:
- [ ] 07-01: Pending rewards queue data structure
- [ ] 07-02: File-based persistence for pending rewards

### Phase 8: Reward Delivery
**Goal**: Auto-deliver pending rewards on player login and /claimvotes command with permissions
**Depends on**: Phase 7
**Research**: Unlikely (command execution, event handling from earlier phases)
**Plans**: TBD

Plans:
- [ ] 08-01: Player login event handler for auto-delivery
- [ ] 08-02: /claimvotes command with permission checks

## Progress

**Execution Order:**
Phases execute in numeric order: 1 → 2 → 3 → 4 → 5 → 6 → 7 → 8

| Phase | Plans Complete | Status | Completed |
|-------|----------------|--------|-----------|
| 1. Foundation | 2/2 | Complete | 2026-01-13 |
| 2. Vote Event Listener | 0/1 | Not started | - |
| 3. Configuration System | 0/2 | Not started | - |
| 4. Random Rewards | 0/2 | Not started | - |
| 5. Vote Streak Tracking | 0/2 | Not started | - |
| 6. Milestone Rewards | 0/1 | Not started | - |
| 7. Offline Vote Storage | 0/2 | Not started | - |
| 8. Reward Delivery | 0/2 | Not started | - |
