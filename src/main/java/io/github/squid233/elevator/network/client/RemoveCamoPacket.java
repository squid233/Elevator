package io.github.squid233.elevator.network.client;

import io.github.squid233.elevator.block.entity.ElevatorBlockEntity;
import io.github.squid233.elevator.network.NetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author squid233
 * @since 0.2.0
 */
public record RemoveCamoPacket(BlockPos pos) {
    public static void encode(RemoveCamoPacket msg, PacketByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static RemoveCamoPacket decode(PacketByteBuf buf) {
        return new RemoveCamoPacket(buf.readBlockPos());
    }

    public static void handle(RemoveCamoPacket msg, ServerPlayerEntity player) {
        if (NetworkHandler.isBadClientPacket(player, msg.pos))
            return;
        if (player.getEntityWorld().getBlockEntity(msg.pos) instanceof ElevatorBlockEntity be) {
            be.setCamoAndUpdate(null);
        }
    }
}
