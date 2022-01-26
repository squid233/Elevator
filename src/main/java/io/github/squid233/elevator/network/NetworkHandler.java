package io.github.squid233.elevator.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

/**
 * @author squid233
 * @since 0.1.0
 */
public class NetworkHandler {
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(
            EModNetworkingConstants.TELEPORT_PACKET_ID,
            (server, player, handler, buf, responseSender) -> {
                var req = TeleportRequest.decode(buf);
                server.execute(() -> TeleportHandler.handle(req, player));
            }
        );
    }
}
