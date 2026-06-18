package sweda.cnpc_xwm_addon;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Cnpc_xmm_addon.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // 在此处添加你的配置项
    static final ForgeConfigSpec SPEC = BUILDER.build();

    // 配置加载回调
    @SubscribeEvent
    static void onLoad(ModConfigEvent event) {
        // 在这里读取配置项赋值给静态变量
    }
}