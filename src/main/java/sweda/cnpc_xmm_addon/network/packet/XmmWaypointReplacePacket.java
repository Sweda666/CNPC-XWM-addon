package sweda.cnpc_xmm_addon.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkEvent;
import sweda.cnpc_xmm_addon.client.XmmWaypointCompatibility;

import java.util.function.Supplier;

/** Replaces a waypoint only when the old waypoint is present on the client. */
public final class XmmWaypointReplacePacket {
    private final double oldX;
    private final double oldY;
    private final double oldZ;
    private final String oldDimensionId;
    private final String oldName;
    private final String oldInitials;
    private final int oldColor;
    private final int oldType;
    private final boolean oldTemp;
    private final boolean oldYIncluded;
    private final double newX;
    private final double newY;
    private final double newZ;
    private final String name;
    private final String initials;
    private final int color;
    private final int type;
    private final boolean temp;
    private final boolean yIncluded;
    private final String newDimensionId;

    public XmmWaypointReplacePacket(double oldX, double oldY, double oldZ, String oldDimensionId,
                                    String oldName, String oldInitials, int oldColor, int oldType,
                                    boolean oldTemp, boolean oldYIncluded, double newX, double newY, double newZ,
                                    String name, String initials, int color, int type, boolean temp,
                                    boolean yIncluded, String newDimensionId) {
        this.oldX = oldX;
        this.oldY = oldY;
        this.oldZ = oldZ;
        this.oldDimensionId = oldDimensionId;
        this.oldName = oldName;
        this.oldInitials = oldInitials;
        this.oldColor = oldColor;
        this.oldType = oldType;
        this.oldTemp = oldTemp;
        this.oldYIncluded = oldYIncluded;
        this.newX = newX;
        this.newY = newY;
        this.newZ = newZ;
        this.name = name;
        this.initials = initials;
        this.color = color;
        this.type = type;
        this.temp = temp;
        this.yIncluded = yIncluded;
        this.newDimensionId = newDimensionId;
    }

    public static void handle(XmmWaypointReplacePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClientPacket(msg))
        );
        ctx.get().setPacketHandled(true);
    }

    private static void handleClientPacket(XmmWaypointReplacePacket msg) {
        if (!ModList.get().isLoaded("xaerominimap")) {
            return;
        }
        if (XmmWaypointCompatibility.removeWaypoint(msg.oldX, msg.oldY, msg.oldZ, msg.oldDimensionId,
                msg.oldName, msg.oldInitials, msg.oldColor, msg.oldType, msg.oldTemp, msg.oldYIncluded)) {
            XmmWaypointCompatibility.addWaypoint(msg.newX, msg.newY, msg.newZ, msg.name, msg.initials,
                    msg.color, msg.type, msg.temp, msg.yIncluded, msg.newDimensionId);
        }
    }

    public static XmmWaypointReplacePacket decode(FriendlyByteBuf buf) {
        double oldX = buf.readDouble();
        double oldY = buf.readDouble();
        double oldZ = buf.readDouble();
        String oldDimensionId = readNullableString(buf);
        String oldName = buf.readUtf();
        String oldInitials = buf.readUtf();
        int oldColor = buf.readInt();
        int oldType = buf.readInt();
        boolean oldTemp = buf.readBoolean();
        boolean oldYIncluded = buf.readBoolean();
        double newX = buf.readDouble();
        double newY = buf.readDouble();
        double newZ = buf.readDouble();
        String name = buf.readUtf();
        String initials = buf.readUtf();
        int color = buf.readInt();
        int type = buf.readInt();
        boolean temp = buf.readBoolean();
        boolean yIncluded = buf.readBoolean();
        String newDimensionId = readNullableString(buf);
        return new XmmWaypointReplacePacket(oldX, oldY, oldZ, oldDimensionId, oldName, oldInitials, oldColor,
                oldType, oldTemp, oldYIncluded, newX, newY, newZ, name, initials, color, type, temp, yIncluded,
                newDimensionId);
    }

    public static void encode(XmmWaypointReplacePacket msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.oldX);
        buf.writeDouble(msg.oldY);
        buf.writeDouble(msg.oldZ);
        writeNullableString(buf, msg.oldDimensionId);
        buf.writeUtf(msg.oldName == null ? "" : msg.oldName);
        buf.writeUtf(msg.oldInitials == null ? "" : msg.oldInitials);
        buf.writeInt(msg.oldColor);
        buf.writeInt(msg.oldType);
        buf.writeBoolean(msg.oldTemp);
        buf.writeBoolean(msg.oldYIncluded);
        buf.writeDouble(msg.newX);
        buf.writeDouble(msg.newY);
        buf.writeDouble(msg.newZ);
        buf.writeUtf(msg.name == null ? "" : msg.name);
        buf.writeUtf(msg.initials == null ? "" : msg.initials);
        buf.writeInt(msg.color);
        buf.writeInt(msg.type);
        buf.writeBoolean(msg.temp);
        buf.writeBoolean(msg.yIncluded);
        writeNullableString(buf, msg.newDimensionId);
    }

    private static String readNullableString(FriendlyByteBuf buf) {
        String value = buf.readUtf(256);
        return value.isEmpty() ? null : value;
    }

    private static void writeNullableString(FriendlyByteBuf buf, String value) {
        buf.writeUtf(value == null ? "" : value);
    }
}
