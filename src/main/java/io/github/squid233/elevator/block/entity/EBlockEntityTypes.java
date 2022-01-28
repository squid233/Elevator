package io.github.squid233.elevator.block.entity;

import io.github.squid233.elevator.ElevatorMod;
import io.github.squid233.elevator.block.EBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author squid233
 * @since 0.2.0
 */
public class EBlockEntityTypes {
    public static final BlockEntityType<ElevatorBlockEntity> ELEVATOR_BLOCK_ENTITY = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        new Identifier(ElevatorMod.MOD_ID, "elevator_block_entity"),
        ElevatorBlockEntity.getType(
            EBlocks.ELEVATOR_BLOCKS_ARRAY
        )
    );

    public static void register() {
    }
}
