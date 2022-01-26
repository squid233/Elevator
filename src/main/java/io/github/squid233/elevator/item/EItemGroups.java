package io.github.squid233.elevator.item;

import io.github.squid233.elevator.ElevatorMod;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * @author squid233
 * @since 0.1.0
 */
public class EItemGroups {
    public static final ItemGroup MAIN_GROUP = FabricItemGroupBuilder.build(
        new Identifier(ElevatorMod.MOD_ID, "main"),
        () -> new ItemStack(EItems.WHITE_ELEVATOR)
    );
}
