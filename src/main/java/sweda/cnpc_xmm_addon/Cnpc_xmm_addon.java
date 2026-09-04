package sweda.cnpc_xmm_addon;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import sweda.cnpc_xmm_addon.event.QuestEventListener;
import sweda.cnpc_xmm_addon.command.XmmCommand;
import sweda.cnpc_xmm_addon.network.XmmPacketHandler;
import sweda.cnpc_xmm_addon.network.packet.XmmWaypointPacket;
import sweda.cnpc_xmm_addon.network.packet.XmmWaypointRemovePacket;
import sweda.cnpc_xmm_addon.network.packet.XmmWaypointReplacePacket;
import sweda.cnpc_xmm_addon.network.packet.XmmWaypointsGetPacket;

@Mod(Cnpc_xmm_addon.MODID)
public class Cnpc_xmm_addon {
    public static final String MODID = "cnpc_xmm_addon";
    public static final String VERSION = "1.2.2";

    public Cnpc_xmm_addon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onLoadComplete);
        // CustomNPCs creates /noppes during the same event; merge our branch afterwards.
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::registerCommands);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        XmmCommand.register(event.getDispatcher());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            XmmPacketHandler.INSTANCE = NetworkRegistry.newSimpleChannel(
                    ResourceLocation.fromNamespaceAndPath(Cnpc_xmm_addon.MODID, "main"),
                    () -> Cnpc_xmm_addon.VERSION,
                    Cnpc_xmm_addon.VERSION::equals,
                    Cnpc_xmm_addon.VERSION::equals
            );
            XmmPacketHandler.INSTANCE.registerMessage(
                    0,
                    XmmWaypointPacket.class,
                    XmmWaypointPacket::encode,
                    XmmWaypointPacket::decode,
                    XmmWaypointPacket::handle
            );
            XmmPacketHandler.INSTANCE.registerMessage(
                    1,
                    XmmWaypointsGetPacket.class,
                    XmmWaypointsGetPacket::encode,
                    XmmWaypointsGetPacket::decode,
                    XmmWaypointsGetPacket::handle
            );
            XmmPacketHandler.INSTANCE.registerMessage(
                    2,
                    XmmWaypointRemovePacket.class,
                    XmmWaypointRemovePacket::encode,
                    XmmWaypointRemovePacket::decode,
                    XmmWaypointRemovePacket::handle
            );
            XmmPacketHandler.INSTANCE.registerMessage(
                    3,
                    XmmWaypointReplacePacket.class,
                    XmmWaypointReplacePacket::encode,
                    XmmWaypointReplacePacket::decode,
                    XmmWaypointReplacePacket::handle
            );
        });
    }

    public void onLoadComplete(net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent event) {
        try {
            var api = noppes.npcs.api.NpcAPI.Instance();
            if (api != null) {
                api.events().register(new QuestEventListener());
                System.out.println("=== [成功] 已向 CNPC 注册 QuestEventListener ===");
            } else {
                System.err.println("=== [失败] NpcAPI 实例为空 ===");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
