package sweda.cnpc_xwm_addon.mixin;

import noppes.npcs.api.wrapper.WrapperNpcAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import sweda.cnpc_xwm_addon.api.IXmmWaypoint;
import sweda.cnpc_xwm_addon.api.XmmWaypointWrapper;
import xaero.common.minimap.waypoints.Waypoint;

import static com.ibm.icu.impl.ValidIdentifiers.Datatype.x;

@Mixin(WrapperNpcAPI.class)
public class WrapperNpcAPIMixin{
    @Unique
    public IXmmWaypoint createXmmWaypoint(int x, int y, int z, String name, int color){
        return new XmmWaypointWrapper(x,y,z,name,color);
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
