package sweda.cnpc_xmm_addon.client.gui;

import net.minecraft.client.resources.language.I18n;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;
import sweda.cnpc_xmm_addon.api.IXmmWaypoint;

import java.math.BigDecimal;

public class SubGuiQuestWayPoint extends GuiBasic implements ITextfieldListener {

    public final IXmmWaypoint targetWaypoint;
    public boolean xmmWaypointEnabled;

    private static final int FIELD_NAME = 1;
    private static final int FIELD_X = 2;
    private static final int FIELD_Y = 3;
    private static final int FIELD_Z = 4;
    private static final int FIELD_COLOR = 5;
    private static final int FIELD_INITIALS = 6;
    private static final int FIELD_TYPE = 7;
    private static final int FIELD_TEMP = 8;
    private static final int FIELD_Y_INCLUDED = 9;
    private static final int FIELD_DIMENSION = 10;

    // Color 选项：名称翻译键 -> 实际颜色值
    private static final String[] COLOR_NAMES = {
            "gui.xaero_black",
            "gui.xaero_dark_blue",
            "gui.xaero_dark_green",
            "gui.xaero_dark_aqua",
            "gui.xaero_dark_red",
            "gui.xaero_dark_purple",
            "gui.xaero_gold",
            "gui.xaero_gray",
            "gui.xaero_blue"
    };

    // Type 选项：名称翻译键 -> 实际类型值
    private static final String[] TYPE_NAMES = {
            "gui.xaero_normal",
            "gui.xaero_death",
            "gui.xaero_old_death",
            "gui.xaero_destination"
    };

    private int currentColorIndex;
    private int currentTypeIndex;

    public SubGuiQuestWayPoint(IXmmWaypoint waypoint) {
        this(waypoint, false);
    }

    public SubGuiQuestWayPoint(IXmmWaypoint waypoint, boolean enabled) {
        this.targetWaypoint = waypoint;
        this.setBackground("menubg.png");
        this.imageWidth = 280;
        this.imageHeight = 240;
        this.currentColorIndex = Math.max(0, Math.min(COLOR_NAMES.length - 1, waypoint.getColor()));
        this.currentTypeIndex = Math.max(0, Math.min(TYPE_NAMES.length - 1, waypoint.getType()));
        this.xmmWaypointEnabled = enabled;
    }

    @Override
    public void init() {
        super.init();
        int baseX = this.guiLeft + 8;
        int baseY = this.guiTop + 20;

        addLabel(new GuiLabel(FIELD_NAME, "advMode.xmm.waypointname", baseX, baseY));
        String nameText = I18n.get(targetWaypoint.getName());
        GuiTextFieldNop nameField = new GuiTextFieldNop(
                FIELD_NAME,
                this,
                baseX,
                baseY + 15,
                264,
                20,
                nameText
        );
        nameField.setMaxLength(64);
        addTextField(nameField);

        addLabel(new GuiLabel(FIELD_X, "X", baseX, baseY + 50));
        GuiTextFieldNop xField = new GuiTextFieldNop(
                FIELD_X,
                this,
                baseX + 22,
                baseY + 42,
                70,
                20,
                formatCoordinate(targetWaypoint.getPreciseX())
        );
        addTextField(xField);

        addLabel(new GuiLabel(FIELD_Y, "Y", baseX, baseY + 82));
        GuiTextFieldNop yField = new GuiTextFieldNop(
                FIELD_Y,
                this,
                baseX + 22,
                baseY + 74,
                70,
                20,
                formatCoordinate(targetWaypoint.getPreciseY())
        );
        addTextField(yField);

        addLabel(new GuiLabel(FIELD_Z, "Z", baseX, baseY + 114));
        GuiTextFieldNop zField = new GuiTextFieldNop(
                FIELD_Z,
                this,
                baseX + 22,
                baseY + 106,
                70,
                20,
                formatCoordinate(targetWaypoint.getPreciseZ())
        );
        addTextField(zField);

        addLabel(new GuiLabel(FIELD_INITIALS, "adv.gui.waypointinitials", baseX, baseY + 146));
        GuiTextFieldNop initialsField = new GuiTextFieldNop(
                FIELD_INITIALS,
                this,
                baseX + 22,
                baseY + 138,
                70,
                20,
                targetWaypoint.getInitials()
        );
        initialsField.setMaxLength(2);
        addTextField(initialsField);

        addLabel(new GuiLabel(FIELD_COLOR, "adv.gui.waypointcolor", baseX + 100, baseY + 50));
        GuiButtonNop colorButton = new GuiButtonNop(
                this,
                FIELD_COLOR,
                baseX + 122,
                baseY + 42,
                70,
                20,
                I18n.get(COLOR_NAMES[currentColorIndex])
        );
        addButton(colorButton);

        addLabel(new GuiLabel(FIELD_TYPE, "adv.gui.waypointtype", baseX + 100, baseY + 82));
        GuiButtonNop typeButton = new GuiButtonNop(
                this,
                FIELD_TYPE,
                baseX + 122,
                baseY + 74,
                70,
                20,
                I18n.get(TYPE_NAMES[currentTypeIndex])
        );
        addButton(typeButton);

        addLabel(new GuiLabel(FIELD_TEMP, "adv.gui.waypointtemp", baseX + 100, baseY + 114));
        GuiButtonNop tempButton = new GuiButtonNop(
                this,
                FIELD_TEMP,
                baseX + 122,
                baseY + 106,
                70,
                20,
                String.valueOf(targetWaypoint.isTemp())
        );
        addButton(tempButton);

        addLabel(new GuiLabel(FIELD_Y_INCLUDED, "adv.gui.waypointyincluded", baseX + 100, baseY + 146));
        GuiButtonNop yIncludedButton = new GuiButtonNop(
                this,
                FIELD_Y_INCLUDED,
                baseX + 122,
                baseY + 138,
                70,
                20,
                String.valueOf(targetWaypoint.isYIncluded())
        );
        addButton(yIncludedButton);

        addLabel(new GuiLabel(FIELD_DIMENSION, "adv.gui.waypointdimension", baseX, baseY + 170));
        GuiTextFieldNop dimensionField = new GuiTextFieldNop(
                FIELD_DIMENSION,
                this,
                baseX,
                baseY + 185,
                264,
                20,
                targetWaypoint.getDimensionId() == null ? "" : targetWaypoint.getDimensionId()
        );
        dimensionField.setMaxLength(128);
        addTextField(dimensionField);

        addButton(new GuiButtonNop(this, 65, baseX + 204, baseY + 42, 56, 20, xmmWaypointEnabled ? "adv.gui.xmmWaypointEnabled" : "adv.gui.xmmWaypointDisabled"));
        addButton(new GuiButtonNop(this, 66, baseX + 204, baseY + 74, 56, 20, "adv.gui.back"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 65) {
            xmmWaypointEnabled = !xmmWaypointEnabled;
            guibutton.setDisplayText(xmmWaypointEnabled ? "adv.gui.xmmWaypointEnabled" : "adv.gui.xmmWaypointDisabled");
        } else if (guibutton.id == 66) {
            onClose();
        } else if (guibutton.id == FIELD_COLOR) {
            currentColorIndex = (currentColorIndex + 1) % COLOR_NAMES.length;
            guibutton.setDisplayText(I18n.get(COLOR_NAMES[currentColorIndex]));
            targetWaypoint.setColor(currentColorIndex);
        } else if (guibutton.id == FIELD_TYPE) {
            currentTypeIndex = (currentTypeIndex + 1) % TYPE_NAMES.length;
            guibutton.setDisplayText(I18n.get(TYPE_NAMES[currentTypeIndex]));
            targetWaypoint.setType(currentTypeIndex);
        } else if (guibutton.id == FIELD_TEMP) {
            Boolean temp = Boolean.parseBoolean(guibutton.getMessage().getString());
            temp = !temp;
            guibutton.setDisplayText(String.valueOf(temp));
            targetWaypoint.setTemp(temp);
        } else if (guibutton.id == FIELD_Y_INCLUDED) {
            Boolean inluded = Boolean.parseBoolean(guibutton.getMessage().getString());
            inluded = !inluded;
            guibutton.setDisplayText(String.valueOf(inluded));
            targetWaypoint.setYIncluded(inluded);
        }
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        String input = textfield.getValue().trim();
        try {
            switch (textfield.id) {
                case FIELD_NAME:
                    targetWaypoint.setName(input);
                    break;
                case FIELD_X:
                    targetWaypoint.setPreciseX(parseCoordinate(input));
                    break;
                case FIELD_Y:
                    targetWaypoint.setPreciseY(parseCoordinate(input));
                    break;
                case FIELD_Z:
                    targetWaypoint.setPreciseZ(parseCoordinate(input));
                    break;
                case FIELD_INITIALS:
                    targetWaypoint.setInitials(input);
                    break;
                case FIELD_DIMENSION:
                    targetWaypoint.setDimensionId(input.isEmpty() ? null : input);
                    break;
            }
        } catch (NumberFormatException e) {
        }
    }

    private static String formatCoordinate(double coordinate) {
        return BigDecimal.valueOf(coordinate).stripTrailingZeros().toPlainString();
    }

    private static double parseCoordinate(String input) {
        double coordinate = Double.parseDouble(input.replace(',', '.'));
        if (!Double.isFinite(coordinate)) {
            throw new NumberFormatException("Coordinate must be finite");
        }
        return coordinate;
    }
}
