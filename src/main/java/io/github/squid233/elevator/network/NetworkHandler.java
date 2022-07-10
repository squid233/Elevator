package io.github.squid233.elevator.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static io.github.squid233.elevator.network.EModNetworkingConstants.TELEPORT_PACKET_ID;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class NetworkHandler {
    public static void init() {
        register(
            TELEPORT_PACKET_ID,
            TeleportRequest::decode,
            TeleportHandler::handle
        );
    }

    public static <T> void sendToServer(Identifier id,
                                        T msg,
                                        BiConsumer<T, PacketByteBuf> encoding) {
        var buf = PacketByteBufs.create();
        encoding.accept(msg, buf);
        ClientPlayNetworking.send(id, buf);
    }

    public static <T> void register(Identifier id,
                                    Function<PacketByteBuf, T> decoding,
                                    BiConsumer<T, ServerPlayerEntity> handling) {
        ServerPlayNetworking.registerGlobalReceiver(
            id,
            (server, player, handler, buf, responseSender) -> {
                T msg = decoding.apply(buf);
                server.execute(() -> handling.accept(msg, player));
            }
        );
    }
}
