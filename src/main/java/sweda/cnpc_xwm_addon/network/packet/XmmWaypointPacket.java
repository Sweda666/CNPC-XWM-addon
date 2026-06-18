package sweda.cnpc_xwm_addon.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkEvent;
import sweda.cnpc_xwm_addon.client.XmmWaypointCompatibility;

import java.util.function.Supplier;

public class XmmWaypointPacket {
    public static String invalid = "invalid";

    private final int x;
    private final int y;
    private final int z;
    private final String name;
    private final String initials;
    private final int color;
    private final int type;
    private final boolean temp;
    private final boolean yIncluded;

    public XmmWaypointPacket(int x, int y, int z, String name, String initials, int color, int type, boolean temp, boolean yIncluded) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        this.initials = initials;
        this.color = color;
        this.type = type;
        this.temp = temp;
        this.yIncluded = yIncluded;
    }

    public static void handle(XmmWaypointPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(msg, ctx))
        );
        ctx.get().setPacketHandled(true);
    }

    private static void handlePacket(XmmWaypointPacket msg, Supplier<NetworkEvent.Context> ctx) {
        int x = msg.x;
        int y = msg.y;
        int z = msg.z;
        String name = msg.name;
        String initials = msg.initials;
        int color = msg.color;
        int type = msg.type;
        boolean temp = msg.temp;
        boolean yIncluded = msg.yIncluded;

        if (name.isEmpty()) name = "adv.questposition";
        if (name.equals(invalid)) return;

        if (!ModList.get().isLoaded("xaerominimap")) return;
        XmmWaypointCompatibility.addWaypoint(x, y, z, name, initials, color, type, temp, yIncluded);
    }

    public static XmmWaypointPacket decode(FriendlyByteBuf buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        String name = buf.readUtf();
        String initials = buf.readUtf();
        int color = buf.readInt();
        int type = buf.readInt();
        boolean temp = buf.readBoolean();
        boolean yIncluded = buf.readBoolean();

        return new XmmWaypointPacket(x, y, z, name, initials, color, type, temp, yIncluded);
    }

    public static void encode(XmmWaypointPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.x);
        buf.writeInt(msg.y);
        buf.writeInt(msg.z);
        buf.writeUtf(msg.name);
        buf.writeUtf(msg.initials);
        buf.writeInt(msg.color);
        buf.writeInt(msg.type);
        buf.writeBoolean(msg.temp);
        buf.writeBoolean(msg.yIncluded);
    }
}