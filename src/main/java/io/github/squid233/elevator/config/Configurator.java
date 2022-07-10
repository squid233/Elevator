package io.github.squid233.elevator.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.util.math.MathHelper.clamp;

/**
 * @author squid233
 * @since 0.2.0
 */
public final class Configurator {
    public static final boolean DEF_SAME_COLOR = false;
    public static final boolean DEF_PRECISION_TARGET = true;
    public static final boolean DEF_RESET_PITCH_NORMAL = false;
    public static final boolean DEF_RESET_PITCH_DIRECTIONAL = true;
    public static final int DEF_RANGE = 384;
    public static final boolean DEF_USE_XP = false;
    public static final int DEF_XP_POINTS_AMOUNT = 1;
    /**
     * Should elevators have the same color in order to teleport ?
     */
    private boolean sameColor = DEF_SAME_COLOR;
    /**
     * Realign players to the center of elevator ?
     */
    private boolean precisionTarget = DEF_PRECISION_TARGET;
    /**
     * Reset pitch to 0 when teleporting to normal elevators ?
     */
    private boolean resetPitchNormal = DEF_RESET_PITCH_NORMAL;
    /**
     * Reset pitch to 0 when teleporting to directional elevators ?
     */
    private boolean resetPitchDirectional = DEF_RESET_PITCH_DIRECTIONAL;
    /**
     * Elevator range
     */
    private int range = DEF_RANGE;
    /**
     * Should teleporting require XP ?
     */
    private boolean useXP = DEF_USE_XP;
    /**
     * Amount of XP points to use when useXP is enabled
     * <p>
     * <b>Note</b> this is NOT experience levels
     * </p>
     */
    private int xpPointsAmount = DEF_XP_POINTS_AMOUNT;
    /**
     * The block identifiers. Add any block you want.
     */
    private final List<Identifier> blockIds = new ArrayList<>();

    public Configurator(boolean sameColor,
                        boolean precisionTarget,
                        boolean resetPitchNormal,
                        boolean resetPitchDirectional,
                        int range,
                        boolean useXP,
                        int xpPointsAmount,
                        List<Identifier> blockIds) {
        this.sameColor = sameColor;
        this.precisionTarget = precisionTarget;
        this.resetPitchNormal = resetPitchNormal;
        this.resetPitchDirectional = resetPitchDirectional;
        this.range = clamp(range, 3, 4064);
        this.useXP = useXP;
        this.xpPointsAmount = clamp(xpPointsAmount, 1, Integer.MAX_VALUE);
        this.blockIds.addAll(blockIds);
    }

    public Configurator(Configurator configurator) {
        sameColor = configurator.sameColor;
        precisionTarget = configurator.precisionTarget;
        resetPitchNormal = configurator.resetPitchNormal;
        resetPitchDirectional = configurator.resetPitchDirectional;
        range = clamp(configurator.range, 3, 4064);
        useXP = configurator.useXP;
        xpPointsAmount = clamp(configurator.xpPointsAmount, 1, Integer.MAX_VALUE);
        blockIds.addAll(configurator.blockIds);
    }

    public Configurator() {
    }

    public static class Serializer extends TypeAdapter<Configurator> {
        @Override
        public void write(JsonWriter out, Configurator value) throws IOException {
            out.beginObject()
                .name("sameColor").value(value.sameColor)
                .name("range").value(value.range)
                .name("precisionTarget").value(value.precisionTarget)
                .name("resetPitchNormal").value(value.resetPitchNormal)
                .name("resetPitchDirectional").value(value.resetPitchDirectional)
                .name("useXP").value(value.useXP)
                .name("xpPointsAmount").value(value.xpPointsAmount)
                .name("customElevators").beginArray();
            for (var id : value.blockIds)
                out.value(id.toString());
            out.endArray().endObject();
        }

        @Override
        public Configurator read(JsonReader in) throws IOException {
            var configurator = new Configurator();
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "sameColor" -> configurator.sameColor = in.nextBoolean();
                    case "range" -> configurator.range = in.nextInt();
                    case "precisionTarget" -> configurator.precisionTarget = in.nextBoolean();
                    case "resetPitchNormal" -> configurator.resetPitchNormal = in.nextBoolean();
                    case "resetPitchDirectional" -> configurator.resetPitchDirectional = in.nextBoolean();
                    case "useXP" -> configurator.useXP = in.nextBoolean();
                    case "xpPointsAmount" -> configurator.xpPointsAmount = in.nextInt();
                    case "customElevators" -> {
                        in.beginArray();
                        while (in.hasNext()) {
                            configurator.blockIds.add(new Identifier(in.nextString()));
                        }
                        in.endArray();
                    }
                }
            }
            in.endObject();
            return configurator;
        }
    }

    public boolean isSameColor() {
        return sameColor;
    }

    public void setSameColor(boolean sameColor) {
        this.sameColor = sameColor;
    }

    public boolean isPrecisionTarget() {
        return precisionTarget;
    }

    public void setPrecisionTarget(boolean precisionTarget) {
        this.precisionTarget = precisionTarget;
    }

    public boolean isResetPitchNormal() {
        return resetPitchNormal;
    }

    public void setResetPitchNormal(boolean resetPitchNormal) {
        this.resetPitchNormal = resetPitchNormal;
    }

    public boolean isResetPitchDirectional() {
        return resetPitchDirectional;
    }

    public void setResetPitchDirectional(boolean resetPitchDirectional) {
        this.resetPitchDirectional = resetPitchDirectional;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = clamp(range, 3, 4064);
    }

    public boolean isUseXP() {
        return useXP;
    }

    public void setUseXP(boolean useXP) {
        this.useXP = useXP;
    }

    public int getXpPointsAmount() {
        return xpPointsAmount;
    }

    public void setXpPointsAmount(int xpPointsAmount) {
        this.xpPointsAmount = clamp(xpPointsAmount, 1, Integer.MAX_VALUE);
    }

    public void addCustomElevator(Block block) {
        blockIds.add(Registry.BLOCK.getId(block));
    }

    public boolean hasCustomElevator(Block block) {
        return blockIds.contains(Registry.BLOCK.getId(block));
    }

    public void clearCustomElevator() {
        blockIds.clear();
    }
}
