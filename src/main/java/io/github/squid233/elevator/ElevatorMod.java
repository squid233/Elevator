package io.github.squid233.elevator;

import io.github.squid233.elevator.block.EBlocks;
import io.github.squid233.elevator.item.EItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author squid233
 * @since 0.1.0
 */
public class ElevatorMod implements ModInitializer {
    public static final String MOD_ID = "elevator233";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.debug("Registering objects");
        EBlocks.register();
        EItems.register();
        LOGGER.debug("Registered all objects");
    }
}
