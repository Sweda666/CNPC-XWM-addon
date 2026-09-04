package sweda.cnpc_xmm_addon.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkEvent;
import sweda.cnpc_xmm_addon.client.XmmWaypointCompatibility;

import java.util.function.Supplier;

public class XmmWaypointRemovePacket {
    public static String invalid = "invalid";

    private final double x;
    private final double y;
    private final double z;
    private final String dimensionId;

    public XmmWaypointRemovePacket(int x, int y, int z) {
        this(x, y, z, null);
    }

    public XmmWaypointRemovePacket(double x, double y, double z, String dimensionId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimensionId = dimensionId;
    }

    public static void handle(XmmWaypointRemovePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(msg, ctx))
        );
        ctx.get().setPacketHandled(true);
    }

    private static void handlePacket(XmmWaypointRemovePacket msg, Supplier<NetworkEvent.Context> ctx) {
        double x = msg.x;
        double y = msg.y;
        double z = msg.z;

        if (!ModList.get().isLoaded("xaerominimap")) return;
        XmmWaypointCompatibility.removeWaypoint(x, y, z, msg.dimensionId);
    }

    public static XmmWaypointRemovePacket decode(FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        String dimensionId = buf.readUtf(256);
        return new XmmWaypointRemovePacket(x, y, z, dimensionId.isEmpty() ? null : dimensionId);
    }

    public static void encode(XmmWaypointRemovePacket msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
        buf.writeUtf(msg.dimensionId == null ? "" : msg.dimensionId);
    }
}
