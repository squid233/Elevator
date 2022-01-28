package io.github.squid233.elevator.block;

import io.github.squid233.elevator.ElevatorMod;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.util.DyeColor.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public class EBlocks {
    public static final ElevatorBlock WHITE_ELEVATOR = elevator(WHITE);
    public static final ElevatorBlock ORANGE_ELEVATOR = elevator(ORANGE);
    public static final ElevatorBlock MAGENTA_ELEVATOR = elevator(MAGENTA);
    public static final ElevatorBlock LIGHT_BLUE_ELEVATOR = elevator(LIGHT_BLUE);
    public static final ElevatorBlock YELLOW_ELEVATOR = elevator(YELLOW);
    public static final ElevatorBlock LIME_ELEVATOR = elevator(LIME);
    public static final ElevatorBlock PINK_ELEVATOR = elevator(PINK);
    public static final ElevatorBlock GRAY_ELEVATOR = elevator(GRAY);
    public static final ElevatorBlock LIGHT_GRAY_ELEVATOR = elevator(LIGHT_GRAY);
    public static final ElevatorBlock CYAN_ELEVATOR = elevator(CYAN);
    public static final ElevatorBlock PURPLE_ELEVATOR = elevator(PURPLE);
    public static final ElevatorBlock BLUE_ELEVATOR = elevator(BLUE);
    public static final ElevatorBlock BROWN_ELEVATOR = elevator(BROWN);
    public static final ElevatorBlock GREEN_ELEVATOR = elevator(GREEN);
    public static final ElevatorBlock RED_ELEVATOR = elevator(RED);
    public static final ElevatorBlock BLACK_ELEVATOR = elevator(BLACK);
    public static final ArrowBlock ARROW = register("arrow", new ArrowBlock(FabricBlockSettings.of(Material.AIR)));
    public static final ElevatorBlock[] ELEVATOR_BLOCKS_ARRAY = {
        WHITE_ELEVATOR,
        ORANGE_ELEVATOR,
        MAGENTA_ELEVATOR,
        LIGHT_BLUE_ELEVATOR,
        YELLOW_ELEVATOR,
        LIME_ELEVATOR,
        PINK_ELEVATOR,
        GRAY_ELEVATOR,
        LIGHT_GRAY_ELEVATOR,
        CYAN_ELEVATOR,
        PURPLE_ELEVATOR,
        BLUE_ELEVATOR,
        BROWN_ELEVATOR,
        GREEN_ELEVATOR,
        RED_ELEVATOR,
        BLACK_ELEVATOR
    };

    private static ElevatorBlock elevator(DyeColor color) {
        return register(color + "_elevator", new ElevatorBlock(color));
    }

    private static <T extends Block> T register(String id, T block) {
        return Registry.register(Registry.BLOCK,
            new Identifier(ElevatorMod.MOD_ID, id),
            block);
    }

    public static void register() {
    }
}
