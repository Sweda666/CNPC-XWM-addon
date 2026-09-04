package sweda.cnpc_xmm_addon.mixin;

import noppes.npcs.api.wrapper.WrapperNpcAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import sweda.cnpc_xmm_addon.api.IXmmWaypoint;
import sweda.cnpc_xmm_addon.api.XmmWaypointWrapper;
import xaero.common.minimap.waypoints.Waypoint;

@Mixin(WrapperNpcAPI.class)
public class WrapperNpcAPIMixin{
    @Unique
    public IXmmWaypoint createXmmWaypoint(int x, int y, int z, String name, int color){
        return new XmmWaypointWrapper(x,y,z,name,color);
    }

    @Unique
    public IXmmWaypoint createXmmWaypoint(double x, double y, double z, String name, int color){
        return new XmmWaypointWrapper(x, y, z, name, color);
    }

    @Unique
    public IXmmWaypoint createXmmWaypoint(double x, double y, double z, String name, int color, String dimensionId){
        return new XmmWaypointWrapper(x, y, z, name, color, dimensionId);
    }
    @Unique
    public IXmmWaypoint getIXmmWaypoint(Waypoint waypoint){
        int x = waypoint.getX();
        int y = waypoint.getY();
        int z = waypoint.getZ();
        String name = waypoint.getName();
        int color = waypoint.getColor();
        return new XmmWaypointWrapper(x,y,z,name,color);
    }
}
