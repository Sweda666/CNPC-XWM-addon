package sweda.cnpc_xmm_addon.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Quest;
import sweda.cnpc_xmm_addon.api.IXmmWaypoint;
import sweda.cnpc_xmm_addon.api.XmmWaypointWrapper;
import sweda.cnpc_xmm_addon.common.QuestWaypointHolder;
import sweda.cnpc_xmm_addon.network.XmmPacketHandler;
import sweda.cnpc_xmm_addon.network.packet.XmmWaypointReplacePacket;

/** Commands for assigning a quest waypoint from an in-world position. */
public final class XmmCommand {
    private XmmCommand() {
    }

    /** Adds the waypoint branch to CustomNPCs' existing /noppes quest command. */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandNode<CommandSourceStack> noppes = dispatcher.getRoot().getChild("noppes");
        if (noppes == null) {
            dispatcher.register(Commands.literal("noppes")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.literal("quest").then(waypointCommand())));
            return;
        }

        CommandNode<CommandSourceStack> quest = noppes.getChild("quest");
        if (quest == null) {
            noppes.addChild(Commands.literal("quest").then(waypointCommand()).build());
        } else {
            quest.addChild(waypointCommand().build());
        }
    }

    private static LiteralArgumentBuilder<CommandSourceStack> waypointCommand() {
        return Commands.literal("waypoint").then(setCommand("set"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> setCommand(String literal) {
        return Commands.literal(literal)
                .then(Commands.argument("quest", IntegerArgumentType.integer(0))
                        .executes(context -> setWaypoint(context, null))
                        .then(Commands.argument("position", Vec3Argument.vec3())
                                .executes(context -> setWaypoint(context,
                                        Vec3Argument.getVec3(context, "position")))));
    }

    private static int setWaypoint(CommandContext<CommandSourceStack> context, Vec3 position)
            throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        int questId = IntegerArgumentType.getInteger(context, "quest");
        if (QuestController.instance == null || QuestController.instance.quests == null) {
            context.getSource().sendFailure(Component.literal("CustomNPCs quest controller is not available"));
            return 0;
        }

        Quest quest = QuestController.instance.quests.get(questId);
        if (quest == null) {
            context.getSource().sendFailure(Component.literal("Unknown quest ID: " + questId));
            return 0;
        }

        if (!(quest instanceof QuestWaypointHolder holder)) {
            context.getSource().sendFailure(Component.literal("Quest waypoint support is not available"));
            return 0;
        }
        IXmmWaypoint waypoint = holder.getWaypoint();
        IXmmWaypoint previousWaypoint = waypoint == null ? null : copyOf(waypoint);
        boolean wasEnabled = holder.isXmmWaypointEnabled();
        String dimensionId = player.level().dimension().location().toString();
        Vec3 targetPosition = position == null ? player.position() : position;
        if (waypoint == null) {
            String name = quest.title == null || quest.title.isBlank() ? "adv.questposition" : quest.title;
            String initials = name.isEmpty() ? "?" : name.substring(0, 1);
            waypoint = new XmmWaypointWrapper(targetPosition.x, targetPosition.y, targetPosition.z, name, initials,
                    0, 0, false, true, dimensionId);
            holder.setWaypoint(waypoint);
        } else {
            waypoint.setPreciseX(targetPosition.x);
            waypoint.setPreciseY(targetPosition.y);
            waypoint.setPreciseZ(targetPosition.z);
            waypoint.setDimensionId(dimensionId);
        }
        holder.setXmmWaypointEnabled(true);
        quest.save();

        if (previousWaypoint != null && wasEnabled) {
            XmmWaypointReplacePacket packet = new XmmWaypointReplacePacket(
                    previousWaypoint.getPreciseX(), previousWaypoint.getPreciseY(), previousWaypoint.getPreciseZ(),
                    previousWaypoint.getDimensionId(), previousWaypoint.getName(), previousWaypoint.getInitials(),
                    previousWaypoint.getColor(), previousWaypoint.getType(), previousWaypoint.isTemp(),
                    previousWaypoint.isYIncluded(), waypoint.getPreciseX(), waypoint.getPreciseY(),
                    waypoint.getPreciseZ(), waypoint.getName(), waypoint.getInitials(), waypoint.getColor(),
                    waypoint.getType(), waypoint.isTemp(), waypoint.isYIncluded(), waypoint.getDimensionId());
            if (player.getServer() != null) {
                for (ServerPlayer onlinePlayer : player.getServer().getPlayerList().getPlayers()) {
                    XmmPacketHandler.sendToPlayer(packet, onlinePlayer);
                }
            }
        }

        context.getSource().sendSuccess(() -> Component.translatable("cnpc_xmm.command.waypoint_set", questId,
                format(targetPosition.x), format(targetPosition.y), format(targetPosition.z), dimensionId), true);
        return 1;
    }

    private static IXmmWaypoint copyOf(IXmmWaypoint waypoint) {
        return new XmmWaypointWrapper(waypoint.getPreciseX(), waypoint.getPreciseY(), waypoint.getPreciseZ(),
                waypoint.getName(), waypoint.getInitials(), waypoint.getColor(), waypoint.getType(),
                waypoint.isTemp(), waypoint.isYIncluded(), waypoint.getDimensionId());
    }

    private static String format(double coordinate) {
        return String.format(java.util.Locale.ROOT, "%.3f", coordinate);
    }
}
