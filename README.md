# CNPC-XMM-addon

CustomNPCs quest waypoint integration for Minecraft 1.20.1 Forge. Quest waypoints are stored with the quest and synchronized to Xaero's Minimap when a player starts or completes a quest.

## Requirements

- Minecraft 1.20.1
- Forge 47.4.x
- CustomNPCs 1.20.1
- Xaero's Minimap for 1.20.1

## Installation

1. Download the latest `cnpc_xmm_addon` JAR from Releases.
2. Put it in the server and client `mods` directories.
3. Start the game with the required dependencies installed.

## Command

Set a quest waypoint at the executing player's current position:

```text
/noppes quest waypoint set <questId>
```

Set it at explicit coordinates in the executing player's current dimension:

```text
/noppes quest waypoint set <questId> <x> <y> <z>
```

The command is added below CustomNPCs' native `/noppes quest` command and requires operator permission. The old `/cnpc_xmm` and `/xmm` command paths are not registered.

When an already-enabled quest waypoint is changed, each online player who has the exact old waypoint receives an update: the old point is removed and the new point is added. Players without that waypoint are left unchanged.

## Quest editor

The quest editor includes waypoint coordinates, name, initials, color, type, temporary status, 3D/Y display, dimension ID, and an enable toggle. Waypoint settings are saved with the quest.

## Building

Use Java 17 and run:

```text
./gradlew build
```

The output JAR is written to `build/libs/`.

## License

MIT
