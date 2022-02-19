package io.github.squid233.elevator.block.entity;

import io.github.squid233.elevator.client.gui.ElevatorScreen;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

/**
 * {@link ScreenHandlerType}s
 *
 * @author squid233
 * @since 0.2.0
 */
public class ESHTypes {
    public static final Identifier ELEVATOR_SCREEN_HANDLER_TYPE_ID = ElevatorScreenHandler.createSHType();
    public static ScreenHandlerType<ElevatorScreenHandler> ELEVATOR_SCREEN_HANDLER_TYPE;

    public static void register() {
        ELEVATOR_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(
            ELEVATOR_SCREEN_HANDLER_TYPE_ID,
            ElevatorScreenHandler::new
        );
    }

    public static void registerClient() {
        ScreenRegistry.register(ELEVATOR_SCREEN_HANDLER_TYPE, ElevatorScreen::new);
    }
}
