package sweda.cnpc_xmm_addon.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.event.QuestEvent;
import noppes.npcs.api.handler.data.IQuest;
import sweda.cnpc_xmm_addon.api.IXmmWaypoint;
import sweda.cnpc_xmm_addon.common.IPlayerWaypointHolder;
import sweda.cnpc_xmm_addon.common.QuestWaypointHolder;

public class QuestEventListener {

    @SubscribeEvent
    public void onQuestStarted(QuestEvent.QuestStartEvent event) {
        IPlayer player = event.player;
        IQuest quest = event.quest;

        if (!(quest instanceof QuestWaypointHolder holder)) return;
        boolean enabled = holder.isXmmWaypointEnabled();
        if (!enabled) return;
        IXmmWaypoint wp = holder.getWaypoint();
        if (wp == null) return;

        wp.applyTo(player);
    }

    @SubscribeEvent
    public void onQuestCompleted(QuestEvent.QuestCompletedEvent event) {
        IPlayer player = event.player;
        IQuest quest = event.quest;

        if (!(quest instanceof QuestWaypointHolder holder)) return;
        boolean enabled = holder.isXmmWaypointEnabled();
        if (!enabled) return;
        IXmmWaypoint wp = holder.getWaypoint();
        if (wp == null) return;

        if (player instanceof IPlayerWaypointHolder waypointHolder) {
            waypointHolder.removeWaypoint(wp);
        }
    }
}
