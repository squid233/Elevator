package io.github.squid233.elevator.item;

import io.github.squid233.elevator.ElevatorMod;
import io.github.squid233.elevator.block.EBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static io.github.squid233.elevator.item.EItemGroups.MAIN_GROUP;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class EItems {
    public static final Item WHITE_ELEVATOR = elevator("white", EBlocks.WHITE_ELEVATOR);
    public static final Item ORANGE_ELEVATOR = elevator("orange", EBlocks.ORANGE_ELEVATOR);
    public static final Item MAGENTA_ELEVATOR = elevator("magenta", EBlocks.MAGENTA_ELEVATOR);
    public static final Item LIGHT_BLUE_ELEVATOR = elevator("light_blue", EBlocks.LIGHT_BLUE_ELEVATOR);
    public static final Item YELLOW_ELEVATOR = elevator("yellow", EBlocks.YELLOW_ELEVATOR);
    public static final Item LIME_ELEVATOR = elevator("lime", EBlocks.LIME_ELEVATOR);
    public static final Item PINK_ELEVATOR = elevator("pink", EBlocks.PINK_ELEVATOR);
    public static final Item GRAY_ELEVATOR = elevator("gray", EBlocks.GRAY_ELEVATOR);
    public static final Item LIGHT_GRAY_ELEVATOR = elevator("light_gray", EBlocks.LIGHT_GRAY_ELEVATOR);
    public static final Item CYAN_ELEVATOR = elevator("cyan", EBlocks.CYAN_ELEVATOR);
    public static final Item PURPLE_ELEVATOR = elevator("purple", EBlocks.PURPLE_ELEVATOR);
    public static final Item BLUE_ELEVATOR = elevator("blue", EBlocks.BLUE_ELEVATOR);
    public static final Item BROWN_ELEVATOR = elevator("brown", EBlocks.BROWN_ELEVATOR);
    public static final Item GREEN_ELEVATOR = elevator("green", EBlocks.GREEN_ELEVATOR);
    public static final Item RED_ELEVATOR = elevator("red", EBlocks.RED_ELEVATOR);
    public static final Item BLACK_ELEVATOR = elevator("black", EBlocks.BLACK_ELEVATOR);

    private static Item elevator(String color, Block block) {
        return Registry.register(Registry.ITEM,
            new Identifier(ElevatorMod.MOD_ID, color + "_elevator"),
            new BlockItem(block, new FabricItemSettings().group(MAIN_GROUP)));
    }

    public static void register() {
    }
}
