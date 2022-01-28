package io.github.squid233.elevator.util;

import io.github.squid233.elevator.block.ElevatorBlock;
import io.github.squid233.elevator.mixin.RenderLayersAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.render.RenderLayer;

/**
 * @author squid233
 * @since 0.2.0
 */
public class BlockRenderLayers {
    public static boolean canRenderInLayer(BlockState state, RenderLayer layer) {
        var block = state.getBlock();
        if (block instanceof LeavesBlock)
            return RenderLayersAccessor.isFancyGraphicsOrBetter() ? layer == RenderLayer.getCutoutMipped() : layer == RenderLayer.getSolid();
        if (block instanceof ElevatorBlock)
            return true;
        return layer == RenderLayer.getSolid();
    }
}
