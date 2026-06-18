package sweda.cnpc_xwm_addon.api;

import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.api.entity.IPlayer;
import sweda.cnpc_xwm_addon.network.XWPacketHandler;
import sweda.cnpc_xwm_addon.network.packet.XmmWaypointPacket;

public class XmmWaypointWrapper implements IXmmWaypoint {
    private int x;
    private int y;
    private int z;
    private String name;
    private String initials;
    private int color;
    private int type;
    private boolean temp;
    private boolean yIncluded;

    public XmmWaypointWrapper(int x, int y, int z, String name, String initials, int color, int type, boolean temp, boolean yIncluded) {
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

    public XmmWaypointWrapper(int x, int y, int z, String name, int color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        this.initials = name.substring(0, 1);
        this.color = color;
        this.type = 0;
        this.temp = false;
        this.yIncluded = true;
    }

    public void applyTo(IPlayer player) {
        ServerPlayer splayer = player.getMCEntity();
        XmmWaypointPacket packet = new XmmWaypointPacket(this.x, this.y, this.z, this.name, this.initials, this.color, this.type, this.temp, this.yIncluded);
        XWPacketHandler.sendToPlayer(packet, splayer);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
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
        return this.x = x;
    }

    public int setY(int y) {
        return this.y = y;
    }

    public int setZ(int z) {
        return this.z = z;
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
}
