package io.github.squid233.elevator.network.client;

import io.github.squid233.elevator.block.ElevatorBlock;
import io.github.squid233.elevator.network.NetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author squid233
 * @since 0.2.0
 */
public record SetArrowPacket(boolean value, BlockPos pos) {
    public static void encode(SetArrowPacket msg, PacketByteBuf buf) {
        buf.writeBoolean(msg.value);
        buf.writeBlockPos(msg.pos);
    }

    public static SetArrowPacket decode(PacketByteBuf buf) {
        return new SetArrowPacket(buf.readBoolean(), buf.readBlockPos());
    }

    public static void handle(SetArrowPacket msg, ServerPlayerEntity player) {
        if (NetworkHandler.isBadClientPacket(player, msg.pos))
            return;
        var world = player.getWorld();
        var state = world.getBlockState(msg.pos);
        if (state.getBlock() instanceof ElevatorBlock) {
            world.setBlockState(msg.pos, state.with(ElevatorBlock.SHOW_ARROW, msg.value));
        }
    }
}
