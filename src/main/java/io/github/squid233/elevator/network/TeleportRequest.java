package io.github.squid233.elevator.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

/**
 * @author squid233
 * @since 0.1.0
 */
public record TeleportRequest(BlockPos from, BlockPos to) {
    public static TeleportRequest decode(PacketByteBuf buf) {
        return new TeleportRequest(buf.readBlockPos(), buf.readBlockPos());
    }

    public static void encode(TeleportRequest req, PacketByteBuf buf) {
        buf.writeBlockPos(req.from).writeBlockPos(req.to);
    }
}
