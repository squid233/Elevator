package io.github.squid233.elevator.client;

import io.github.squid233.elevator.network.NetworkHandler;
import net.fabricmc.api.ClientModInitializer;

/**
 * @author squid233
 * @since 0.1.0
 */
public class EModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NetworkHandler.init();
    }
}
