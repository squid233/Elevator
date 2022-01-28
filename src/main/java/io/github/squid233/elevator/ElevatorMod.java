package io.github.squid233.elevator;

import io.github.squid233.elevator.block.EBlocks;
import io.github.squid233.elevator.block.entity.EBlockEntityTypes;
import io.github.squid233.elevator.block.entity.ESHTypes;
import io.github.squid233.elevator.config.EModConfigs;
import io.github.squid233.elevator.item.EItems;
import io.github.squid233.elevator.network.NetworkHandler;
import net.fabricmc.api.ModInitializer;
import org.apache.commons.io.IOExceptionList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author squid233
 * @since 0.1.0
 */
public class ElevatorMod implements ModInitializer {
    public static final String MOD_ID = "elevator233";
    public static final Logger LOGGER = LoggerFactory.getLogger("ElevatorMod");

    @Override
    public void onInitialize() {
        LOGGER.debug("Registering objects");
        EBlocks.register();
        EItems.register();
        EBlockEntityTypes.register();
        ESHTypes.register();
        NetworkHandler.init();
        LOGGER.debug("Registered all objects");
        EModConfigs.loadAllCfg();
        LOGGER.debug("Common side: done");
    }
}
