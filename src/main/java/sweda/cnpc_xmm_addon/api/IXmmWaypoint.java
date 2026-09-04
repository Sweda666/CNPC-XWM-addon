package sweda.cnpc_xmm_addon.api;

import noppes.npcs.api.entity.IPlayer;

public interface IXmmWaypoint {
    void applyTo(IPlayer player);

    int getX();

    int getY();

    int getZ();

    String getName();

    int getColor();

    String getInitials();

    int getType();

    boolean isTemp();

    boolean isYIncluded();

    int setX(int x);

    int setY(int y);

    int setZ(int z);

    String setName(String name);

    int setColor(int color);

    String setInitials(String initials);

    int setType(int type);

    boolean setTemp(boolean temp);

    boolean setYIncluded(boolean yIncluded);

    /**
     * Returns the coordinate before it is converted to Xaero's integer marker format.
     */
    default double getPreciseX() {
        return getX();
    }

    default double getPreciseY() {
        return getY();
    }

    default double getPreciseZ() {
        return getZ();
    }

    /**
     * Sets a coordinate without losing its fractional part.
     */
    default void setPreciseX(double x) {
        setX((int) Math.round(x));
    }

    default void setPreciseY(double y) {
        setY((int) Math.round(y));
    }

    default void setPreciseZ(double z) {
        setZ((int) Math.round(z));
    }

    /**
     * Minecraft dimension ID, for example minecraft:overworld.
     * A null value means the player's current dimension when the waypoint is applied.
     */
    default String getDimensionId() {
        return null;
    }

    default void setDimensionId(String dimensionId) {
    }
}
