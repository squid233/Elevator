package io.github.squid233.elevator.block.entity;

import io.github.squid233.elevator.client.gui.ElevatorScreen;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.screen.ScreenHandlerType;

/**
 * {@link ScreenHandlerType}s
 *
 * @author squid233
 * @since 0.2.0
 */
public class ESHTypes {
    public static final ScreenHandlerType<ElevatorScreenHandler> ELEVATOR_SCREEN_HANDLER_TYPE =
        ElevatorScreenHandler.registerSHType();

    public static void register() {
    }

    public static void registerClient() {
        ScreenRegistry.register(ELEVATOR_SCREEN_HANDLER_TYPE, ElevatorScreen::new);
    }
}
