package io.github.squid233.elevator.client;

import io.github.squid233.elevator.ElevatorMod;
import io.github.squid233.elevator.block.EBlocks;
import io.github.squid233.elevator.block.ElevatorBlock;
import io.github.squid233.elevator.block.entity.ESHTypes;
import io.github.squid233.elevator.block.entity.ElevatorBlockEntity;
import io.github.squid233.elevator.client.render.ElevatorBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;

import static io.github.squid233.elevator.block.EBlocks.ELEVATOR_BLOCKS_ARRAY;
import static io.github.squid233.elevator.block.entity.EBlockEntityTypes.ELEVATOR_BLOCK_ENTITY;

/**
 * @author squid233
 * @since 0.1.0
 */
public class EModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ESHTypes.registerClient();
        BlockEntityRendererRegistry.register(ELEVATOR_BLOCK_ENTITY, ElevatorBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(EBlocks.ARROW, RenderLayer.getCutoutMipped());
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                if (world == null || pos == null)
                    return -1;
                if (state.getBlock() instanceof ElevatorBlock
                    && world.getBlockEntity(pos) instanceof ElevatorBlockEntity be)
                    if (be.getHeldState() != null)
                        return MinecraftClient.getInstance()
                            .getBlockColors()
                            .getColor(be.getHeldState(),
                                world,
                                pos,
                                tintIndex);
                return -1;
            },
            ELEVATOR_BLOCKS_ARRAY);
        ElevatorMod.LOGGER.debug("Client side: done");
    }
}
