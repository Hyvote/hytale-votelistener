# Known Issues

## Open

### BUG-001: Pending rewards commands don't execute on login/claimvotes
**Status:** Open
**Severity:** High
**Reported:** 2026-01-13

**Description:**
When a player votes while offline, the pending rewards are stored correctly (visible in JSON file). When the player logs in, they receive the "thanks for voting" message, but the actual reward commands don't execute. Same issue with `/claimvotes` command.

**Expected Behavior:**
- Player votes while offline
- Player logs in
- Player receives message AND reward commands execute

**Actual Behavior:**
- Player votes while offline
- Player logs in
- Player receives message but reward commands do NOT execute

**Files involved:**
- `PlayerJoinListener.java` - auto-delivery on login
- `ClaimVotesCommand.java` - manual claim command
- `PendingRewardsManager.java` - reward storage

**Investigation notes:**
- Messages are sent successfully
- Commands list is retrieved from pending rewards
- Commands are passed to `executeCommand()` but may not be executing properly
- Possible issue with `ConsoleSender.INSTANCE` or command format

---

## Closed

(none yet)
