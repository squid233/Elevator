package io.github.squid233.elevator.block;

import io.github.squid233.elevator.ElevatorMod;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.block.MapColor.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public class EBlocks {
    public static final Block WHITE_ELEVATOR = elevator("white", WHITE);
    public static final Block ORANGE_ELEVATOR = elevator("orange", ORANGE);
    public static final Block MAGENTA_ELEVATOR = elevator("magenta", MAGENTA);
    public static final Block LIGHT_BLUE_ELEVATOR = elevator("light_blue", LIGHT_BLUE);
    public static final Block YELLOW_ELEVATOR = elevator("yellow", YELLOW);
    public static final Block LIME_ELEVATOR = elevator("lime", LIME);
    public static final Block PINK_ELEVATOR = elevator("pink", PINK);
    public static final Block GRAY_ELEVATOR = elevator("gray", GRAY);
    public static final Block LIGHT_GRAY_ELEVATOR = elevator("light_gray", LIGHT_GRAY);
    public static final Block CYAN_ELEVATOR = elevator("cyan", CYAN);
    public static final Block PURPLE_ELEVATOR = elevator("purple", PURPLE);
    public static final Block BLUE_ELEVATOR = elevator("blue", BLUE);
    public static final Block BROWN_ELEVATOR = elevator("brown", BROWN);
    public static final Block GREEN_ELEVATOR = elevator("green", GREEN);
    public static final Block RED_ELEVATOR = elevator("red", RED);
    public static final Block BLACK_ELEVATOR = elevator("black", BLACK);

    private static Block elevator(String color, MapColor mapColor) {
        return Registry.register(Registry.BLOCK,
            new Identifier(ElevatorMod.MOD_ID, color + "_elevator"),
            new ElevatorBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).mapColor(mapColor)));
    }

    public static void register() {
    }
}
