package sweda.cnpc_xwm_addon.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import xaero.common.minimap.waypoints.Waypoint;
import sweda.cnpc_xwm_addon.client.XmmWaypointCompatibility;

import java.util.ArrayList;
import java.util.function.Supplier;

public class XmmWaypointsGetPacket {
    public static String invalid = "invalid";

    public static final ArrayList<Waypoint> WAYPOINT_CACHE = new ArrayList<>();

    public XmmWaypointsGetPacket() {}

    public static XmmWaypointsGetPacket decode(FriendlyByteBuf buf) {
        return new XmmWaypointsGetPacket();
    }

    public static void encode(XmmWaypointsGetPacket msg, FriendlyByteBuf buf) {

    }

    public static void handle(XmmWaypointsGetPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClientPacket(msg, ctx))
        );
        ctx.get().setPacketHandled(true);
    }

    private static void handleClientPacket(XmmWaypointsGetPacket msg, Supplier<NetworkEvent.Context> ctx) {
        if (!ModList.get().isLoaded("xaerominimap")) {
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        ArrayList<Waypoint> waypointList = XmmWaypointCompatibility.getWaypoints(player);
        WAYPOINT_CACHE.clear();
        WAYPOINT_CACHE.addAll(waypointList);
    }
}