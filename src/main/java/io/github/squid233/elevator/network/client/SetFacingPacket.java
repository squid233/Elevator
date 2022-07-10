package io.github.squid233.elevator.network.client;

import io.github.squid233.elevator.block.ElevatorBlock;
import io.github.squid233.elevator.network.NetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * @author squid233
 * @since 0.2.0
 */
public record SetFacingPacket(Direction direction, BlockPos pos) {
    public static void encode(SetFacingPacket msg, PacketByteBuf buf) {
        buf.writeEnumConstant(msg.direction);
        buf.writeBlockPos(msg.pos);
    }

    public static SetFacingPacket decode(PacketByteBuf buf) {
        return new SetFacingPacket(buf.readEnumConstant(Direction.class), buf.readBlockPos());
    }

    public static void handle(SetFacingPacket msg, ServerPlayerEntity player) {
        if (NetworkHandler.isBadClientPacket(player, msg.pos))
            return;
        var world = player.getEntityWorld();
        var state = world.getBlockState(msg.pos);
        if (state.getBlock() instanceof ElevatorBlock) {
            world.setBlockState(msg.pos, state.with(ElevatorBlock.FACING, msg.direction));
        }
    }
}
