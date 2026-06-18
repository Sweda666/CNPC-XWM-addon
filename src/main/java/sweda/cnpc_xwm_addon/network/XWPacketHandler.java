package sweda.cnpc_xwm_addon.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class XWPacketHandler {

    public static SimpleChannel INSTANCE;

    public static <MSG>  void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG>  void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

}
