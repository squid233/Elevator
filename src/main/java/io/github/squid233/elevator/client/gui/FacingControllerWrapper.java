package io.github.squid233.elevator.client.gui;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author squid233
 * @since 0.2.0
 */
final class FacingControllerWrapper {
    private final HashSet<FacingButton> bakedButtons = new HashSet<>();
    private final ArrayList<Point> slots = new ArrayList<>();

    FacingControllerWrapper(int x, int y, BlockPos blockPos, Direction playerFacing) {
        initSlots(x, y);
        initButtons(playerFacing, blockPos);
    }

    private void initSlots(int x, int y) {
        slots.add(new Point(x + 20, y)); // UP
        slots.add(new Point(x + 40, y + 20)); // RIGHT
        slots.add(new Point(x + 20, y + 40)); // BOTTOM
        slots.add(new Point(x, y + 20)); //LEFT
    }

    private void initButtons(Direction playerFacing, BlockPos pos) {
        Collections.rotate(slots, playerFacing.getHorizontal()); // Modifies list
        bakedButtons.add(new FacingButton(slots.get(0), Direction.SOUTH, pos));
        bakedButtons.add(new FacingButton(slots.get(1), Direction.WEST, pos));
        bakedButtons.add(new FacingButton(slots.get(2), Direction.NORTH, pos));
        bakedButtons.add(new FacingButton(slots.get(3), Direction.EAST, pos));
    }

    HashSet<FacingButton> getButtons() {
        return bakedButtons;
    }
}
