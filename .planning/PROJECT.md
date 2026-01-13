# HytaleVoteListener

## What This Is

A Hytale server plugin that listens for vote events from HytaleVotifier and rewards players with configurable commands, random rewards, vote streaks, and milestone bonuses. The GAListener equivalent for the Hytale ecosystem.

## Core Value

Reliable vote reward delivery — every vote triggers the correct rewards, whether the player is online or offline.

## Requirements

### Validated

(None yet — ship to validate)

### Active

- [ ] Listen to VoteEvent from HytaleVotifier plugin
- [ ] Execute configurable commands on vote with placeholder support (%player%, %service%, %uuid%)
- [ ] Random rewards with weighted chances and multiple tiers
- [ ] Vote streak tracking (consecutive daily votes)
- [ ] Milestone rewards (every N votes)
- [ ] Offline vote storage with pending rewards queue
- [ ] Auto-delivery of pending rewards on player login
- [ ] /claimvotes command to manually claim pending rewards
- [ ] JSON configuration file for all settings
- [ ] Permission-based access to commands

### Out of Scope

- Vote party system (server-wide events) — complexity, can add in v2
- Database storage (MySQL/SQLite) — JSON file storage sufficient for v1
- Web API endpoints — not needed for a listener plugin
- Multi-server sync — single server focus for v1

## Context

- HytaleVotifier is the vote receiving plugin (already built)
- VoteEvent is fired via Hytale's EventBus system
- Must follow same code conventions as hytale-votifier for brand consistency
- Java 25, Maven build, manifest.json plugin descriptor
- WebServer plugin is NOT a dependency (unlike votifier)
- Will be published to both private (int-hytale-votelistener) and public (hytale-votelistener) repos under Hyvote org

## Constraints

- **Tech Stack**: Java 25, Maven, Hytale Server API — match votifier exactly
- **Dependency**: HytaleVotifier plugin must be installed for votes to work
- **Conventions**: Must match hytale-votifier code style, package naming, documentation standards
- **Config Format**: JSON — consistent with Hytale ecosystem

## Key Decisions

| Decision | Rationale | Outcome |
|----------|-----------|---------|
| JSON config over YAML | Match votifier and Hytale native format | — Pending |
| File-based storage for pending votes | Simple, no external DB dependency | — Pending |
| Both auto-delivery and /claimvotes | Flexibility for different server styles | — Pending |

---
*Last updated: 2026-01-13 after initialization*
