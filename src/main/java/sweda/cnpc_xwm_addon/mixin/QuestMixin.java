package sweda.cnpc_xwm_addon.mixin;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.controllers.data.Quest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweda.cnpc_xwm_addon.api.IXmmWaypoint;
import sweda.cnpc_xwm_addon.api.XmmWaypointWrapper;
import sweda.cnpc_xwm_addon.common.QuestWaypointHolder;

@Mixin(Quest.class)
public abstract class QuestMixin implements QuestWaypointHolder {

    private IXmmWaypoint xwmWaypoint;
    private boolean xmmWaypointEnabled;

    public IXmmWaypoint getWaypoint() {
        return xwmWaypoint;
    }

    public void setWaypoint(IXmmWaypoint waypoint) {
        this.xwmWaypoint = waypoint;
    }

    public boolean isXmmWaypointEnabled() {
        return xmmWaypointEnabled;
    }

    public void setXmmWaypointEnabled(boolean enabled) {
        this.xmmWaypointEnabled = enabled;
    }

    @Inject(method = "writeToNBTPartial(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", at = @At("TAIL"), remap = false)
    private void onWriteToNBT(CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir) {
        compound.putBoolean("XmmWaypointEnabled", xmmWaypointEnabled);
        if (xwmWaypoint != null) {
            CompoundTag wpTag = new CompoundTag();
            wpTag.putInt("X", xwmWaypoint.getX());
            wpTag.putInt("Y", xwmWaypoint.getY());
            wpTag.putInt("Z", xwmWaypoint.getZ());
            wpTag.putString("Name", xwmWaypoint.getName());
            wpTag.putInt("Color", xwmWaypoint.getColor());
            wpTag.putString("Initials", xwmWaypoint.getInitials());
            wpTag.putInt("Type", xwmWaypoint.getType());
            wpTag.putBoolean("Temp", xwmWaypoint.isTemp());
            wpTag.putBoolean("YIncluded", xwmWaypoint.isYIncluded());
            compound.put("XwmWaypoint", wpTag);
        }
    }

    @Inject(method = "readNBT(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"), remap = false)
    private void onReadFromNBT(CompoundTag compound, CallbackInfo ci) {
        this.xmmWaypointEnabled = compound.getBoolean("XmmWaypointEnabled");
        loadWaypoint(compound);
    }

    @Inject(method = "readNBTPartial(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"), remap = false)
    private void onReadFromNBTPartial(CompoundTag compound, CallbackInfo ci) {
        this.xmmWaypointEnabled = compound.getBoolean("XmmWaypointEnabled");
        loadWaypoint(compound);
    }

    private void loadWaypoint(CompoundTag compound) {
        if (compound.contains("XwmWaypoint")) {
            CompoundTag wpTag = compound.getCompound("XwmWaypoint");
            int x = wpTag.getInt("X");
            int y = wpTag.getInt("Y");
            int z = wpTag.getInt("Z");
            String name = wpTag.getString("Name");
            int color = wpTag.getInt("Color");
            String initials = wpTag.getString("Initials");
            int type = wpTag.getInt("Type");
            boolean temp = wpTag.getBoolean("Temp");
            boolean yIncluded = wpTag.getBoolean("YIncluded");
            this.xwmWaypoint = new XmmWaypointWrapper(x, y, z, name, initials, color, type, temp, yIncluded);
        } else {
            this.xwmWaypoint = null;
        }
    }
}