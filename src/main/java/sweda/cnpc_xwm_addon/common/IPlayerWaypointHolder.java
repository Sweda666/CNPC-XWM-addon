package sweda.cnpc_xwm_addon.common;

import sweda.cnpc_xwm_addon.api.IXmmWaypoint;
import xaero.common.minimap.waypoints.Waypoint;

import java.util.ArrayList;

public interface IPlayerWaypointHolder {
    ArrayList<Waypoint> getWaypoints();

    void removeWaypoint(IXmmWaypoint waypoint);

    void clearWaypoints();
}
