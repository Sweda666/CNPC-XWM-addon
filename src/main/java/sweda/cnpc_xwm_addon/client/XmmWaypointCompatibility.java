package sweda.cnpc_xwm_addon.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.IXaeroMinimapClientPlayNetHandler;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.minimap.XaeroMinimap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class XmmWaypointCompatibility {
    public static void addWaypoint(int x, int y, int z, String name, String initials, int color, int type, boolean temp, boolean yIncluded) {
        IXaeroMinimapClientPlayNetHandler clientLevel = (IXaeroMinimapClientPlayNetHandler) (Minecraft.getInstance().player.connection);
        XaeroMinimapSession session = clientLevel.getXaero_minimapSession();
        WaypointsManager manager = session.getWaypointsManager();
        Waypoint wp = new Waypoint(x, y, z, name, initials, color, type, temp, yIncluded);
        manager.getWaypoints().getList().add(wp);
        try {
            XaeroMinimap.instance.getSettings().saveWaypoints(manager.getCurrentWorld());
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public static void addWaypoint(int x, int y, int z, String name, String initials, int color, int type, boolean temp) {
        addWaypoint(x, y, z, name, initials, color, type, temp, true);
    }

    public static void addWaypoint(int x, int y, int z, String name, String initials, int color, int type) {
        addWaypoint(x, y, z, name, initials, color, type, false, true);
    }

    public static void addWaypoint(int x, int y, int z, String name, String initials, int color) {
        addWaypoint(x, y, z, name, initials, color, 0, false, true);
    }

    public static void addWaypoint(int x, int y, int z, String name, int color) {
        addWaypoint(x, y, z, name, name.substring(0, 1), color, 0, false, true);
    }

    public static void addWaypoint(int x, int y, int z, String name) {
        addWaypoint(x, y, z, name, name.substring(0, 1), 0, 0, false, true);
    }

    public static void addWaypoint(int x, int y, int z) {
        addWaypoint(x, y, z, "adv.questposition", "adv.questposition".substring(0, 1), 0, 0, false, true);
    }

    public static ArrayList<Waypoint> getWaypoints(LocalPlayer player) {
        IXaeroMinimapClientPlayNetHandler clientLevel = (IXaeroMinimapClientPlayNetHandler) (player.connection);
        XaeroMinimapSession session = clientLevel.getXaero_minimapSession();
        WaypointsManager manager = session.getWaypointsManager();
        ArrayList<Waypoint> waypoints = manager.getWaypoints().getList();
        return waypoints;
    }

    private static Waypoint getWaypoint(ArrayList<Waypoint> waypoints, int x, int y, int z) {
        for (Waypoint waypoint : waypoints) {
            if (waypoint.getX() == x && waypoint.getY() == y && waypoint.getZ() == z) {
                return waypoint;
            }
        }
        return null;
    }

    private static Waypoint getWaypoint(int x, int y, int z) {
        ArrayList<Waypoint> waypoints = getWaypoints(Minecraft.getInstance().player);
        for (Waypoint waypoint : waypoints) {
            if (waypoint.getX() == x && waypoint.getY() == y && waypoint.getZ() == z) {
                return waypoint;
            }
        }
        return null;
    }

    private static Waypoint getWaypoint(ArrayList<Waypoint> waypoints, String name) {
        for (Waypoint waypoint : waypoints) {
            if (Objects.equals(waypoint.getName(), name)) {
                return waypoint;
            }
        }
        return null;
    }

    private static Waypoint getWaypoint(String name) {
        ArrayList<Waypoint> waypoints = getWaypoints(Minecraft.getInstance().player);
        for (Waypoint waypoint : waypoints) {
            if (Objects.equals(waypoint.getName(), name)) {
                return waypoint;
            }
        }
        return null;
    }

    public static boolean removeWaypoint(int index) {
        IXaeroMinimapClientPlayNetHandler clientLevel = (IXaeroMinimapClientPlayNetHandler) (Minecraft.getInstance().player.connection);
        XaeroMinimapSession session = clientLevel.getXaero_minimapSession();
        WaypointsManager manager = session.getWaypointsManager();
        ArrayList<Waypoint> waypoints = manager.getWaypoints().getList();
        waypoints.remove(index);
        try {
            XaeroMinimap.instance.getSettings().saveWaypoints(manager.getCurrentWorld());
        } catch (IOException error) {
            error.printStackTrace();
        }
        return false;
    }

    public static boolean removeWaypoint(int x, int y, int z) {
        IXaeroMinimapClientPlayNetHandler clientLevel = (IXaeroMinimapClientPlayNetHandler) (Minecraft.getInstance().player.connection);
        XaeroMinimapSession session = clientLevel.getXaero_minimapSession();
        WaypointsManager manager = session.getWaypointsManager();
        ArrayList<Waypoint> waypoints = manager.getWaypoints().getList();
        for (Waypoint waypoint : waypoints) {
            if (waypoint.getX() == x && waypoint.getY() == y && waypoint.getZ() == z) {
                int index = waypoints.indexOf(waypoint);
                waypoints.remove(index);
                try {
                    XaeroMinimap.instance.getSettings().saveWaypoints(manager.getCurrentWorld());
                } catch (IOException error) {
                    error.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    private static boolean removeWaypoint(String name) {
        IXaeroMinimapClientPlayNetHandler clientLevel = (IXaeroMinimapClientPlayNetHandler) (Minecraft.getInstance().player.connection);
        XaeroMinimapSession session = clientLevel.getXaero_minimapSession();
        WaypointsManager manager = session.getWaypointsManager();
        ArrayList<Waypoint> waypoints = manager.getWaypoints().getList();
        for (Waypoint waypoint : waypoints) {
            if (waypoint.getName() == name) {
                int index = waypoints.indexOf(waypoint);
                waypoints.remove(index);
                try {
                    XaeroMinimap.instance.getSettings().saveWaypoints(manager.getCurrentWorld());
                } catch (IOException error) {
                    error.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }
}