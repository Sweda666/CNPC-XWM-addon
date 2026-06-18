package sweda.cnpc_xwm_addon.mixin;

import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.PlayerWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import sweda.cnpc_xwm_addon.api.IXmmWaypoint;
import sweda.cnpc_xwm_addon.api.XmmWaypointWrapper;
import sweda.cnpc_xwm_addon.common.IPlayerWaypointHolder;
import sweda.cnpc_xwm_addon.network.XWPacketHandler;
import sweda.cnpc_xwm_addon.network.packet.XmmWaypointPacket;
import sweda.cnpc_xwm_addon.network.packet.XmmWaypointRemovePacket;
import sweda.cnpc_xwm_addon.network.packet.XmmWaypointsGetPacket;
import xaero.common.minimap.waypoints.Waypoint;

import java.util.ArrayList;

@Mixin(PlayerWrapper.class)
public abstract class PlayerWrapperMixin implements IPlayerWaypointHolder {

    @Shadow
    public abstract ServerPlayer getMCEntity();

    @Unique
    public void addWaypoint(int x, int y, int z, String name, String initials, int color, int type, boolean temp, boolean yIncluded) {
        ServerPlayer player = this.getMCEntity();
        XmmWaypointPacket packet = new XmmWaypointPacket(x, y, z, name, initials, color, type, temp, yIncluded);
        XWPacketHandler.sendToPlayer(packet, player);
    }

    @Unique
    public void addWaypoint(int x, int y, int z, String name, String initials, int color, int type, boolean temp) {
        addWaypoint(x, y, z, name, initials, color, type, temp, true);
    }

    @Unique
    public void addWaypoint(int x, int y, int z, String name, String initials, int color, int type) {
        addWaypoint(x, y, z, name, initials, color, type, false, true);
    }

    @Unique
    public void addWaypoint(int x, int y, int z, String name, String initials, int color) {
        addWaypoint(x, y, z, name, initials, color, 0, false, true);
    }

    @Unique
    public void addWaypoint(int x, int y, int z, String name, int color) {
        addWaypoint(x, y, z, name, name.substring(0, 1), color, 0, false, true);
    }

    @Unique
    public void addWaypoint(int x, int y, int z, String name) {
        addWaypoint(x, y, z, name, 0);
    }

    @Unique
    public void addWaypoint(int x, int y, int z) {
        addWaypoint(x, y, z, "adv.questposition", 0);
    }

    @Unique
    public ArrayList<Waypoint> getWaypoints() {
        ServerPlayer player = this.getMCEntity();
        XWPacketHandler.sendToPlayer(new XmmWaypointsGetPacket(), player);
        return XmmWaypointsGetPacket.WAYPOINT_CACHE;
    }

    @Unique
    public void removeWaypoint(IXmmWaypoint wp) {
        ServerPlayer player = this.getMCEntity();
        XmmWaypointRemovePacket packet = new XmmWaypointRemovePacket(wp.getX(), wp.getY(), wp.getZ());
        XWPacketHandler.sendToPlayer(packet, player);
    }

    @Unique
    public void removeWaypoint(int x, int y, int z) {
        ServerPlayer player = this.getMCEntity();
        XmmWaypointRemovePacket packet = new XmmWaypointRemovePacket(x, y, z);
        XWPacketHandler.sendToPlayer(packet, player);
    }

    @Unique
    public void clearWaypoints() {
        ServerPlayer player = this.getMCEntity();
        IPlayer cnpcplayer = (IPlayer) NpcAPI.Instance().getIEntity(player);
        ArrayList<Waypoint> wps = ((IPlayerWaypointHolder) cnpcplayer).getWaypoints();
        for (Waypoint waypoint : wps) {
            IXmmWaypoint wp = new XmmWaypointWrapper(waypoint.getX(), waypoint.getY(), waypoint.getZ(), waypoint.getName(), waypoint.getColor());
            removeWaypoint(wp);
        }
    }
}