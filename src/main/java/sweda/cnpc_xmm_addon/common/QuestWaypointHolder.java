package sweda.cnpc_xmm_addon.common;

import sweda.cnpc_xmm_addon.api.IXmmWaypoint;

public interface QuestWaypointHolder {
    IXmmWaypoint getWaypoint();

    void setWaypoint(IXmmWaypoint waypoint);

    void setXmmWaypointEnabled(boolean enabled);

    boolean isXmmWaypointEnabled();
}