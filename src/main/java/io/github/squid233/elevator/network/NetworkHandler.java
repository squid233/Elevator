package io.github.squid233.elevator.network;

import io.github.squid233.elevator.block.entity.ElevatorScreenHandler;
import io.github.squid233.elevator.network.client.RemoveCamoPacket;
import io.github.squid233.elevator.network.client.SetArrowPacket;
import io.github.squid233.elevator.network.client.SetDirectionalPacket;
import io.github.squid233.elevator.network.client.SetFacingPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static io.github.squid233.elevator.network.EModNetworkingConstants.*;

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
        register(
            SET_FACING_PACKET_ID,
            SetFacingPacket::decode,
            SetFacingPacket::handle
        );
        register(
            SET_DIRECTIONAL_PACKET_ID,
            SetDirectionalPacket::decode,
            SetDirectionalPacket::handle
        );
        register(
            SET_ARROW_PACKET_ID,
            SetArrowPacket::decode,
            SetArrowPacket::handle
        );
        register(
            REMOVE_CAMO_PACKET_ID,
            RemoveCamoPacket::decode,
            RemoveCamoPacket::handle
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

    public static boolean isBadClientPacket(ServerPlayerEntity player, BlockPos pos) {
        if (player == null || player.isDead() || player.isRemoved())
            return true;
        var world = player.getEntityWorld();
        if (!world.isChunkLoaded(pos))
            return true;
        if (!(player.currentScreenHandler instanceof ElevatorScreenHandler handler))
            return true;
        return !handler.getPos().equals(pos);
    }
}
