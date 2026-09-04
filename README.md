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

## Screenshots

<img width="1616" height="756" alt="Quest waypoint editor" src="https://github.com/user-attachments/assets/c2de497a-8a77-4e48-842e-b1276cb3c629" />
<img width="1920" height="1146" alt="Quest waypoint settings" src="https://github.com/user-attachments/assets/cf1e87a7-851d-4d87-b431-5f6d48568df3" />
<img width="1920" height="1146" alt="Waypoint configuration" src="https://github.com/user-attachments/assets/78d52b32-2a7b-46a0-8aff-34da3aea29ff" />
<img width="1920" height="1146" alt="In-game waypoint" src="https://github.com/user-attachments/assets/6eaf6e6c-42a4-45a1-86ae-bada1aaf20e7" />
