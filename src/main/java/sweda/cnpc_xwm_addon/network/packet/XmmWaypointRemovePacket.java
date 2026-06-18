package sweda.cnpc_xwm_addon.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkEvent;
import sweda.cnpc_xwm_addon.client.XmmWaypointCompatibility;

import java.util.function.Supplier;

public class XmmWaypointRemovePacket {
    public static String invalid = "invalid";

    private final int x;
    private final int y;
    private final int z;

    public XmmWaypointRemovePacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // 原有 handle 方法签名、逻辑 完全保留
    public static void handle(XmmWaypointRemovePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(msg, ctx))
        );
        ctx.get().setPacketHandled(true);
    }

    // 原有私有处理方法 完全保留
    private static void handlePacket(XmmWaypointRemovePacket msg, Supplier<NetworkEvent.Context> ctx) {
        int x = msg.x;
        int y = msg.y;
        int z = msg.z;

        if (!ModList.get().isLoaded("xaerominimap")) return;
        XmmWaypointCompatibility.removeWaypoint(x, y, z);
    }

    // 原有 decode 静态解码 保留
    public static XmmWaypointRemovePacket decode(FriendlyByteBuf buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        return new XmmWaypointRemovePacket(x, y, z);
    }

    // 原有 encode 静态编码 保留
    public static void encode(XmmWaypointRemovePacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.x);
        buf.writeInt(msg.y);
        buf.writeInt(msg.z);
    }
}