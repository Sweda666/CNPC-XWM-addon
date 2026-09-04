package sweda.cnpc_xmm_addon.mixin;

import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.gui.global.GuiQuestEdit;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweda.cnpc_xmm_addon.api.IXmmWaypoint;
import sweda.cnpc_xmm_addon.api.XmmWaypointWrapper;
import sweda.cnpc_xmm_addon.client.gui.SubGuiQuestWayPoint;
import sweda.cnpc_xmm_addon.common.QuestWaypointHolder;

@Mixin(GuiQuestEdit.class)
public abstract class GuiQuestEditMixin {

    @Shadow(remap = false)
    private Quest quest;

    @Inject(method = "init", at = @At("TAIL"))
    private void onQuestGuiInit(CallbackInfo ci) {
        GuiQuestEdit gui = (GuiQuestEdit) (Object) this;
        gui.addLabel(new GuiLabel(16, "advMode.xmm.waypoint", gui.guiLeft + 214, gui.guiTop + 74));
        gui.addButton(new GuiButtonNop(gui, 16, gui.guiLeft + 330, gui.guiTop + 69, 50, 20, "selectServer.edit"));
    }

    @Inject(method = "buttonEvent", at = @At("TAIL"), remap = false)
    private void onButtonEvent(GuiButtonNop guibutton, CallbackInfo ci) {
        GuiQuestEdit gui = (GuiQuestEdit) (Object) this;
        if (guibutton.id == 16) {
            QuestWaypointHolder holder = (QuestWaypointHolder) quest;
            IXmmWaypoint wp = holder.getWaypoint();
            if (wp == null) {
                wp = new XmmWaypointWrapper(0, 0, 0, "任务地点", 0);
                holder.setWaypoint(wp);
            }
            gui.setSubGui(new SubGuiQuestWayPoint(wp, holder.isXmmWaypointEnabled()));
        }
    }

    @Inject(method = "subGuiClosed", at = @At("TAIL"), remap = false)
    private void onSubGuiClosed(Screen subgui, CallbackInfo ci) {
        if (subgui instanceof SubGuiQuestWayPoint sub) {
            ((QuestWaypointHolder) this.quest).setWaypoint(sub.targetWaypoint);
            ((QuestWaypointHolder) this.quest).setXmmWaypointEnabled(sub.xmmWaypointEnabled);
        }
    }
}
