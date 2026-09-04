package sweda.cnpc_xmm_addon.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.IXaeroMinimapClientPlayNetHandler;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointSet;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.minimap.XaeroMinimap;

import java.io.IOException;
import java.util.ArrayList;

/** Client-side bridge to Xaero's waypoint storage. */
public final class XmmWaypointCompatibility {
    private XmmWaypointCompatibility() {
    }

    public static void addWaypoint(double x, double y, double z, String name, String initials, int color,
                                   int type, boolean temp, boolean yIncluded, String dimensionId) {
        WaypointsManager manager = getManager();
        if (manager == null) {
            return;
        }

        WaypointWorld world = resolveWorld(manager, dimensionId);
        if (world == null) {
            return;
        }

        WaypointSet set = world.getCurrentSet();
        if (set == null) {
            world.addSet("Default");
            set = world.getCurrentSet();
        }
        if (set == null) {
            return;
        }

        Waypoint waypoint = new Waypoint(toXaeroCoordinate(x), toXaeroCoordinate(y), toXaeroCoordinate(z),
                name, initials, color, type, temp, yIncluded);
        set.getList().add(waypoint);
        saveWaypoints(world);
    }

    public static void addWaypoint(int x, int y, int z, String name, String initials, int color,
                                   int type, boolean temp, boolean yIncluded) {
        addWaypoint(x, y, z, name, initials, color, type, temp, yIncluded, null);
    }

    public static void addWaypoint(double x, double y, double z, String name, String initials, int color,
                                   int type, boolean temp, boolean yIncluded) {
        addWaypoint(x, y, z, name, initials, color, type, temp, yIncluded, null);
    }

    public static void addWaypoint(int x, int y, int z, String name, String initials, int color, int type) {
        addWaypoint(x, y, z, name, initials, color, type, false, true, null);
    }

    public static void addWaypoint(int x, int y, int z, String name, String initials, int color) {
        addWaypoint(x, y, z, name, initials, color, 0, false, true, null);
    }

    public static void addWaypoint(int x, int y, int z, String name, int color) {
        addWaypoint(x, y, z, name, initialsFor(name), color, 0, false, true, null);
    }

    public static void addWaypoint(int x, int y, int z, String name) {
        addWaypoint(x, y, z, name, initialsFor(name), 0, 0, false, true, null);
    }

    public static void addWaypoint(int x, int y, int z) {
        addWaypoint(x, y, z, "adv.questposition", initialsFor("adv.questposition"), 0, 0, false, true, null);
    }

    public static ArrayList<Waypoint> getWaypoints(LocalPlayer player) {
        if (player == null || !(player.connection instanceof IXaeroMinimapClientPlayNetHandler clientLevel)) {
            return new ArrayList<>();
        }
        XaeroMinimapSession session = clientLevel.getXaero_minimapSession();
        if (session == null) {
            return new ArrayList<>();
        }
        WaypointSet set = session.getWaypointsManager().getWaypoints();
        return set == null ? new ArrayList<>() : set.getList();
    }

    public static boolean removeWaypoint(int index) {
        WaypointsManager manager = getManager();
        if (manager == null) {
            return false;
        }
        WaypointSet set = manager.getWaypoints();
        if (set == null || index < 0 || index >= set.getList().size()) {
            return false;
        }
        set.getList().remove(index);
        saveWaypoints(manager.getCurrentWorld());
        return true;
    }

    public static boolean removeWaypoint(int x, int y, int z) {
        return removeWaypoint((double) x, y, z, null);
    }

    public static boolean removeWaypoint(double x, double y, double z, String dimensionId) {
        WaypointsManager manager = getManager();
        if (manager == null) {
            return false;
        }
        WaypointWorld world = resolveWorld(manager, dimensionId);
        if (world == null || world.getCurrentSet() == null) {
            return false;
        }
        int waypointX = toXaeroCoordinate(x);
        int waypointY = toXaeroCoordinate(y);
        int waypointZ = toXaeroCoordinate(z);
        ArrayList<Waypoint> waypoints = world.getCurrentSet().getList();
        for (int index = 0; index < waypoints.size(); index++) {
            Waypoint waypoint = waypoints.get(index);
            if (waypoint.getX() == waypointX && waypoint.getY() == waypointY && waypoint.getZ() == waypointZ) {
                waypoints.remove(index);
                saveWaypoints(world);
                return true;
            }
        }
        return false;
    }

    public static boolean removeWaypoint(double x, double y, double z, String dimensionId, String name,
                                         String initials, int color, int type, boolean temp, boolean yIncluded) {
        WaypointsManager manager = getManager();
        if (manager == null) {
            return false;
        }
        WaypointWorld world = resolveWorld(manager, dimensionId);
        if (world == null || world.getCurrentSet() == null) {
            return false;
        }
        int waypointX = toXaeroCoordinate(x);
        int waypointY = toXaeroCoordinate(y);
        int waypointZ = toXaeroCoordinate(z);
        ArrayList<Waypoint> waypoints = world.getCurrentSet().getList();
        for (int index = 0; index < waypoints.size(); index++) {
            Waypoint waypoint = waypoints.get(index);
            if (waypoint.getX() == waypointX && waypoint.getY() == waypointY && waypoint.getZ() == waypointZ
                    && java.util.Objects.equals(waypoint.getName(), name)
                    && java.util.Objects.equals(waypoint.getInitials(), initials)
                    && waypoint.getColor() == color && waypoint.getWaypointType() == type
                    && waypoint.isTemporary() == temp && waypoint.isYIncluded() == yIncluded) {
                waypoints.remove(index);
                saveWaypoints(world);
                return true;
            }
        }
        return false;
    }

    private static WaypointsManager getManager() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || !(player.connection instanceof IXaeroMinimapClientPlayNetHandler clientLevel)) {
            return null;
        }
        XaeroMinimapSession session = clientLevel.getXaero_minimapSession();
        return session == null ? null : session.getWaypointsManager();
    }

    private static WaypointWorld resolveWorld(WaypointsManager manager, String dimensionId) {
        WaypointWorld currentWorld = manager.getCurrentWorld();
        if (dimensionId == null || dimensionId.isBlank()) {
            return currentWorld;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && dimensionId.equals(minecraft.level.dimension().location().toString())) {
            return currentWorld;
        }

        ResourceLocation location = ResourceLocation.tryParse(dimensionId);
        if (location == null) {
            return currentWorld;
        }

        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, location);
        String dimensionDirectory = manager.getDimensionDirectoryName(dimensionKey);
        String containerId = manager.getCurrentContainerID();
        if (containerId == null && currentWorld != null && currentWorld.getContainer() != null) {
            containerId = currentWorld.getContainer().getKey();
        }
        if (containerId == null || dimensionDirectory == null) {
            return currentWorld;
        }
        return manager.getWorld(containerId, dimensionDirectory);
    }

    private static int toXaeroCoordinate(double coordinate) {
        long rounded = Math.round(coordinate);
        return (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, rounded));
    }

    private static String initialsFor(String name) {
        return name == null || name.isEmpty() ? "?" : name.substring(0, 1);
    }

    private static void saveWaypoints(WaypointWorld world) {
        if (world == null || XaeroMinimap.instance == null) {
            return;
        }
        try {
            XaeroMinimap.instance.getSettings().saveWaypoints(world);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
