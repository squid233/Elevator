package io.github.squid233.elevator.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

/**
 * @author squid233
 * @since 0.2.0
 */
public class FakeUseContext extends ItemPlacementContext {
    public FakeUseContext(PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        super(new ItemUsageContext(player, hand, hitResult));
    }

    @Override
    public BlockPos getBlockPos() {
        return getHitResult().getBlockPos();
    }
}
