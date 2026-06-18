package sweda.cnpc_xwm_addon.common;

import xaero.hud.minimap.waypoint.set.WaypointSet;

@Deprecated
public interface WaypointAccessor {
    void setText(String text);
    void setX(int x);
    void setY(int y);
    void setZ(int z);
}