package io.github.squid233.elevator.client.render;

import io.github.squid233.elevator.block.EBlocks;
import io.github.squid233.elevator.block.entity.ElevatorBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Random;

import static io.github.squid233.elevator.block.ElevatorBlock.DIRECTIONAL;
import static io.github.squid233.elevator.block.ElevatorBlock.SHOW_ARROW;
import static io.github.squid233.elevator.util.BlockRenderLayers.canRenderInLayer;
import static net.minecraft.block.HorizontalFacingBlock.FACING;
import static net.minecraft.client.render.RenderLayer.getBlockLayers;
import static net.minecraft.client.render.RenderLayer.getCutoutMipped;
import static net.minecraft.client.render.RenderLayers.getBlockLayer;

/**
 * @author squid233
 * @since 0.2.0
 */
public record ElevatorBlockEntityRenderer(Context context) implements BlockEntityRenderer<ElevatorBlockEntity> {
    @Override
    public void render(ElevatorBlockEntity entity,
                       float tickDelta,
                       MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers,
                       int light,
                       int overlay) {
        matrices.push();

        var renderMgr = context.getRenderManager();
        var state = entity.getCachedState();
        var world = entity.getWorld();
        var pos = entity.getPos();
        if (world != null) {
            if (state.get(DIRECTIONAL) && state.get(SHOW_ARROW))
                renderMgr.renderBlock(EBlocks.ARROW.getDefaultState()
                        .with(FACING, state.get(FACING)),
                    pos,
                    world,
                    matrices,
                    vertexConsumers.getBuffer(getCutoutMipped()),
                    false,
                    new Random());

            var heldState = entity.getHeldState();
            for (var layer : getBlockLayers()) {
                if (heldState != null
                    && canRenderInLayer(heldState, layer)) {
                    renderMgr.renderBlock(heldState,
                        pos,
                        world,
                        matrices,
                        vertexConsumers.getBuffer(getBlockLayer(heldState)),
                        true,
                        new Random());
                    break;
                }
            }
        }

        matrices.pop();
    }
}
