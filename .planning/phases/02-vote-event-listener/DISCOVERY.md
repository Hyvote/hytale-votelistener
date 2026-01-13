# Phase 2 Discovery: Vote Event Listener

## Research Context

**Phase goal:** Subscribe to VoteEvent from HytaleVotifier and execute a basic test command

**Research depth:** Level 2 - Standard Research (new integration with HytaleVotifier)

## Findings

### 1. EventBus System (from HytaleServer.jar)

The Hytale EventBus is located in `com.hypixel.hytale.event`:
- `IEventBus` - Interface for event bus operations
- `EventBus` - Main implementation
- `EventBusRegistry` - Registration management
- `SyncEventBusRegistry` / `AsyncEventBusRegistry` - Sync/async variants

**Expected subscription pattern:**
```java
// Get event bus from plugin context
IEventBus eventBus = getServer().getEventBus(); // or similar accessor

// Subscribe to events with a consumer
eventBus.subscribe(VoteEvent.class, this::onVote);
```

### 2. VoteEvent Structure (inferred from standard votifier protocol)

Based on the standard Votifier protocol (v1/v2), VoteEvent should contain:
- `username` (String) - Player who voted
- `serviceName` (String) - Vote site (e.g., "hytale-servers.org")
- `address` (String) - IP address of vote source (optional)
- `timestamp` (String/long) - When the vote was received

**Expected VoteEvent API:**
```java
public class VoteEvent {
    public String getUsername();
    public String getServiceName();
    public String getAddress();      // May be null or empty
    public String getTimestamp();    // Or getTimeStamp() or similar
}
```

### 3. HytaleVotifier Dependency

From manifest.json:
```json
"Dependencies": {
  "HytaleVotifier": ">=1.0.0"
}
```

This means:
- VoteEvent class is provided by HytaleVotifier plugin
- Import: `com.hyvote.votifier.event.VoteEvent` (assumed package structure)
- At runtime, the class will be available via the plugin classloader

### 4. PluginBase Event Access

The PluginBase class likely provides access to the server instance and event bus. Common patterns:
- `getServer()` - Returns server instance
- `getServer().getEventBus()` - Access to event bus
- Or direct `getEventBus()` method on PluginBase

### 5. Command Execution

For executing commands when a vote is received:
- Use `getServer().executeCommand(command)` or similar
- Or `getServer().dispatchCommand(sender, command)`

## Assumptions to Validate

1. **VoteEvent package:** Assumed `com.hyvote.votifier.event.VoteEvent` - may need adjustment
2. **EventBus access:** Assumed via `getServer().getEventBus()` - verify in implementation
3. **Command execution:** Need to find correct API for running console commands
4. **Event subscription timing:** Register in `start()` method, unregister in `shutdown()`

## Implementation Approach

### Architecture
```
HytaleVoteListener.java
├── start() - Register event listener
├── shutdown() - Unregister event listener (if needed)
└── onVote(VoteEvent) - Handle incoming vote
    └── Execute test command: "say {player} voted!"
```

### Test Strategy
1. Build plugin and install alongside HytaleVotifier
2. Use votifier tester tool to send test votes
3. Verify "say" command executes with player name

## Risk Mitigation

- **Unknown API:** Start with minimal implementation, log vote details first
- **Compilation errors:** May need to add HytaleVotifier as system dependency in pom.xml
- **Event bus access:** Check PluginBase methods if getServer() doesn't work

## Conclusion

Sufficient information to proceed with plan. The implementation will:
1. Add HytaleVotifier as dependency in pom.xml
2. Create vote event handler method
3. Register handler in start(), unregister in shutdown()
4. Execute simple "say" command as proof of concept

---
*Discovery completed: 2026-01-13*
*Confidence: Medium (API assumptions need runtime validation)*
