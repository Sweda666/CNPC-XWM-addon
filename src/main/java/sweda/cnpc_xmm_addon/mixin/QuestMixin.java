package sweda.cnpc_xmm_addon.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import noppes.npcs.controllers.data.Quest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweda.cnpc_xmm_addon.api.IXmmWaypoint;
import sweda.cnpc_xmm_addon.api.XmmWaypointWrapper;
import sweda.cnpc_xmm_addon.common.QuestWaypointHolder;

@Mixin(Quest.class)
public abstract class QuestMixin implements QuestWaypointHolder {

    // Keep reading the pre-1.2 save key without exposing the old product name in the build.
    private static final String LEGACY_WAYPOINT_TAG = new String(new char[]{
            'X', 'w', 'm', 'W', 'a', 'y', 'p', 'o', 'i', 'n', 't'
    });
    private IXmmWaypoint xmmWaypoint;
    private boolean xmmWaypointEnabled;

    public IXmmWaypoint getWaypoint() {
        return xmmWaypoint;
    }

    public void setWaypoint(IXmmWaypoint waypoint) {
        this.xmmWaypoint = waypoint;
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
        if (xmmWaypoint != null) {
            CompoundTag wpTag = new CompoundTag();
            wpTag.putDouble("X", xmmWaypoint.getPreciseX());
            wpTag.putDouble("Y", xmmWaypoint.getPreciseY());
            wpTag.putDouble("Z", xmmWaypoint.getPreciseZ());
            wpTag.putString("Name", xmmWaypoint.getName());
            wpTag.putInt("Color", xmmWaypoint.getColor());
            wpTag.putString("Initials", xmmWaypoint.getInitials());
            wpTag.putInt("Type", xmmWaypoint.getType());
            wpTag.putBoolean("Temp", xmmWaypoint.isTemp());
            wpTag.putBoolean("YIncluded", xmmWaypoint.isYIncluded());
            if (xmmWaypoint.getDimensionId() != null && !xmmWaypoint.getDimensionId().isBlank()) {
                wpTag.putString("Dimension", xmmWaypoint.getDimensionId());
            }
            compound.put("XmmWaypoint", wpTag);
        }
        // Migrate saves written by versions that used the old internal tag name.
        compound.remove(LEGACY_WAYPOINT_TAG);
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
        String waypointKey = compound.contains("XmmWaypoint") ? "XmmWaypoint" : LEGACY_WAYPOINT_TAG;
        if (compound.contains(waypointKey)) {
            CompoundTag wpTag = compound.getCompound(waypointKey);
            double x = readCoordinate(wpTag, "X");
            double y = readCoordinate(wpTag, "Y");
            double z = readCoordinate(wpTag, "Z");
            String name = wpTag.getString("Name");
            int color = wpTag.getInt("Color");
            String initials = wpTag.getString("Initials");
            int type = wpTag.getInt("Type");
            boolean temp = wpTag.getBoolean("Temp");
            boolean yIncluded = wpTag.getBoolean("YIncluded");
            String dimension = wpTag.contains("Dimension", Tag.TAG_STRING) ? wpTag.getString("Dimension") : null;
            this.xmmWaypoint = new XmmWaypointWrapper(x, y, z, name, initials, color, type, temp, yIncluded, dimension);
        } else {
            this.xmmWaypoint = null;
        }
    }

    private static double readCoordinate(CompoundTag tag, String key) {
        return tag.contains(key, Tag.TAG_DOUBLE) ? tag.getDouble(key) : tag.getInt(key);
    }
}
