package io.github.squid233.elevator;

import io.github.squid233.elevator.block.ElevatorBlock;
import io.github.squid233.elevator.network.EModNetworkingConstants;
import io.github.squid233.elevator.network.TeleportHandler;
import io.github.squid233.elevator.network.TeleportRequest;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * @author squid233
 * @since 0.1.0
 */
public class ElevatorHandler {
    public static void tryTeleport(ClientPlayerEntity player, Direction facing) {
        var world = player.getWorld();

        var fromPos = getOriginElevator(player);
        if (fromPos == null)
            return;

        var toPos = fromPos.mutableCopy();
        var fromElevator = (ElevatorBlock) world.getBlockState(fromPos).getBlock();
        ElevatorBlock toElevator;

        while (true) {
            toPos.setY(toPos.getY() + facing.getOffsetY());
            if (world.isOutOfHeightLimit(toPos))
                break;

            var elevator = TeleportHandler.getElevator(world.getBlockState(toPos));
            if (elevator != null && TeleportHandler.isBlocked(world, toPos)) {
                if (fromElevator.getDefaultMapColor() == elevator.getDefaultMapColor()) {
                    var buf = PacketByteBufs.create();
                    TeleportRequest.encode(new TeleportRequest(fromPos, toPos), buf);
                    ClientPlayNetworking.send(EModNetworkingConstants.TELEPORT_PACKET_ID, buf);
                    break;
                }
            }
        }
    }

    private static BlockPos getOriginElevator(ClientPlayerEntity player) {
        var world = player.getEntityWorld();
        var pos = new BlockPos(player.getPos());
        for (int i = 0; i < 3; i++) {
            if (TeleportHandler.getElevator(world.getBlockState(pos)) != null
                && TeleportHandler.isBlocked(world, pos))
                return pos;
            pos = pos.down();
        }
        return null;
    }
}
