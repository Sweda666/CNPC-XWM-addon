package sweda.cnpc_xmm_addon.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class XmmPacketHandler {

    public static SimpleChannel INSTANCE;

    public static <MSG>  void sendToPlayer(MSG message, ServerPlayer player) {
        if (INSTANCE != null && player != null) {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
        }
    }

    public static <MSG>  void sendToServer(MSG message) {
        if (INSTANCE != null) {
            INSTANCE.sendToServer(message);
        }
    }

}
