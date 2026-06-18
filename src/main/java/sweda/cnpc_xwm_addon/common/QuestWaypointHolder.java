package sweda.cnpc_xwm_addon.common;

import sweda.cnpc_xwm_addon.api.IXmmWaypoint;

public interface QuestWaypointHolder {
    IXmmWaypoint getWaypoint();

    void setWaypoint(IXmmWaypoint waypoint);

    void setXmmWaypointEnabled(boolean enabled);

    boolean isXmmWaypointEnabled();
}