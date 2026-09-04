package sweda.cnpc_xmm_addon.api;

import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.api.entity.IPlayer;
import sweda.cnpc_xmm_addon.network.XmmPacketHandler;
import sweda.cnpc_xmm_addon.network.packet.XmmWaypointPacket;

public class XmmWaypointWrapper implements IXmmWaypoint {
    private double x;
    private double y;
    private double z;
    private String name;
    private String initials;
    private int color;
    private int type;
    private boolean temp;
    private boolean yIncluded;
    private String dimensionId;

    public XmmWaypointWrapper(int x, int y, int z, String name, String initials, int color, int type, boolean temp, boolean yIncluded) {
        this(x, y, z, name, initials, color, type, temp, yIncluded, null);
    }

    public XmmWaypointWrapper(double x, double y, double z, String name, String initials, int color, int type, boolean temp, boolean yIncluded, String dimensionId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        this.initials = initials;
        this.color = color;
        this.type = type;
        this.temp = temp;
        this.yIncluded = yIncluded;
        this.dimensionId = dimensionId;
    }

    public XmmWaypointWrapper(int x, int y, int z, String name, int color) {
        this(x, y, z, name, initialsFor(name), color, 0, false, true, null);
    }

    public XmmWaypointWrapper(double x, double y, double z, String name, int color) {
        this(x, y, z, name, initialsFor(name), color, 0, false, true, null);
    }

    public XmmWaypointWrapper(double x, double y, double z, String name, String initials, int color) {
        this(x, y, z, name, initials, color, 0, false, true, null);
    }

    public XmmWaypointWrapper(double x, double y, double z, String name, int color, String dimensionId) {
        this(x, y, z, name, initialsFor(name), color, 0, false, true, dimensionId);
    }

    public XmmWaypointWrapper(double x, double y, double z, String name, String initials, int color, String dimensionId) {
        this(x, y, z, name, initials, color, 0, false, true, dimensionId);
    }

    private static String initialsFor(String name) {
        if (name == null || name.isEmpty()) {
            return "?";
        }
        return name.substring(0, 1);
    }

    public double getPreciseX() {
        return this.x;
    }

    public double getPreciseY() {
        return this.y;
    }

    public double getPreciseZ() {
        return this.z;
    }

    public void setPreciseX(double x) {
        this.x = x;
    }

    public void setPreciseY(double y) {
        this.y = y;
    }

    public void setPreciseZ(double z) {
        this.z = z;
    }

    public void applyTo(IPlayer player) {
        ServerPlayer splayer = player.getMCEntity();
        String targetDimension = this.dimensionId;
        if (targetDimension == null || targetDimension.isBlank()) {
            targetDimension = splayer.level().dimension().location().toString();
        }
        XmmWaypointPacket packet = new XmmWaypointPacket(this.x, this.y, this.z, this.name, this.initials, this.color, this.type, this.temp, this.yIncluded, targetDimension);
        XmmPacketHandler.sendToPlayer(packet, splayer);
    }

    public int getX() {
        return (int) Math.round(this.x);
    }

    public int getY() {
        return (int) Math.round(this.y);
    }

    public int getZ() {
        return (int) Math.round(this.z);
    }

    public String getName() {
        return this.name;
    }

    public int getColor() {
        return this.color;
    }

    public String getInitials() {
        return this.initials;
    }

    public int getType() {
        return this.type;
    }

    public boolean isTemp() {
        return this.temp;
    }

    public boolean isYIncluded() {
        return this.yIncluded;
    }

    public int setX(int x) {
        this.x = x;
        return x;
    }

    public int setY(int y) {
        this.y = y;
        return y;
    }

    public int setZ(int z) {
        this.z = z;
        return z;
    }

    public String setName(String name) {
        return this.name = name;
    }

    public int setColor(int color) {
        return this.color = color;
    }

    public String setInitials(String initials) {
        return this.initials = initials;
    }

    public int setType(int type) {
        return this.type = type;
    }

    public boolean setTemp(boolean temp) {
        return this.temp = temp;
    }

    public boolean setYIncluded(boolean yIncluded) {
        return this.yIncluded = yIncluded;
    }

    public String getDimensionId() {
        return this.dimensionId;
    }

    public void setDimensionId(String dimensionId) {
        this.dimensionId = dimensionId;
    }
}
