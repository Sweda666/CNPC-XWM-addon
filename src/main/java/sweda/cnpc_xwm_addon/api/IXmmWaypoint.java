package sweda.cnpc_xwm_addon.api;

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
}
