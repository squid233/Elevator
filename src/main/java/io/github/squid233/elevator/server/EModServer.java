package io.github.squid233.elevator.server;

import io.github.squid233.elevator.network.NetworkHandler;
import net.fabricmc.api.DedicatedServerModInitializer;

/**
 * @author squid233
 * @since 0.1.0
 */
public class EModServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        NetworkHandler.init();
    }
}
