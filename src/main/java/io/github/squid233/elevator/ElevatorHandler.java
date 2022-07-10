package io.github.squid233.elevator;

import io.github.squid233.elevator.config.EModConfigs;
import io.github.squid233.elevator.network.NetworkHandler;
import io.github.squid233.elevator.network.TeleportHandler;
import io.github.squid233.elevator.network.TeleportRequest;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static io.github.squid233.elevator.network.EModNetworkingConstants.TELEPORT_PACKET_ID;
import static java.lang.Math.abs;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ElevatorHandler {
    public static void tryTeleport(ClientPlayerEntity player, Direction facing) {
        var world = player.getWorld();

        var fromPos = getOriginElevator(player);
        if (fromPos == null)
            return;

        var toPos = fromPos.mutableCopy();
        var fromElevator = world.getBlockState(fromPos).getBlock();
        Block toElevator;

        while (true) {
            toPos.setY(toPos.getY() + facing.getOffsetY());
            if (world.isOutOfHeightLimit(toPos)
                || abs(toPos.getY() - fromPos.getY()) > EModConfigs.configurator.getRange())
                break;

            var elevator = TeleportHandler.getElevator(world.getBlockState(toPos));
            if (elevator != null && TeleportHandler.isBlocked(world, toPos)) {
                if (!EModConfigs.configurator.isSameColor()
                    || fromElevator == elevator) {
                    NetworkHandler.sendToServer(
                        TELEPORT_PACKET_ID,
                        new TeleportRequest(fromPos, toPos),
                        TeleportRequest::encode
                    );
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
