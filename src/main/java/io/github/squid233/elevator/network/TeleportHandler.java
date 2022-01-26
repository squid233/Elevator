package io.github.squid233.elevator.network;

import io.github.squid233.elevator.block.ElevatorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldView;

import java.util.Arrays;

/**
 * @author squid233
 * @since 0.1.0
 */
public class TeleportHandler {
    public static void handle(TeleportRequest req, ServerPlayerEntity player) {
        if (isBadTeleportPacket(req, player))
            return;

        var world = player.getWorld();
        var toPos = req.to();
        var toState = world.getBlockState(req.to());

        var blockYOffset = toState.getCollisionShape(world, toPos).getMax(Direction.Axis.Y);
        player.teleport(world, player.getX(), toPos.getY() + blockYOffset, player.getZ(), player.getYaw(), player.getPitch());
        player.setVelocity(player.getVelocity().multiply(1, 0, 1));
        world.playSound(null, toPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1, 1);
    }

    private static boolean isBadTeleportPacket(TeleportRequest req, ServerPlayerEntity player) {
        if (player == null || !player.isAlive())
            return true;
        var world = player.getWorld();
        var fromPos = req.from();
        var toPos = req.to();
        if (!world.isChunkLoaded(fromPos) || !world.isChunkLoaded(toPos))
            return true;
        final var distanceSqr = player.squaredDistanceTo(Vec3d.ofCenter(fromPos.north()));
        if (distanceSqr > 4
            || fromPos.getX() != toPos.getX()
            || fromPos.getZ() != toPos.getZ()) return true;
        var fromElevator = getElevator(world.getBlockState(fromPos));
        var toElevator = getElevator(world.getBlockState(toPos));
        return fromElevator == null
            || toElevator == null
            || !isBlocked(world, toPos)
            || fromElevator.getDefaultMapColor() != toElevator.getDefaultMapColor();
    }

    public static boolean isBlocked(WorldView world, BlockPos target) {
        return validateTargets(world.getBlockState(target.up()), world.getBlockState(target.up(2)));
    }

    private static boolean validateTarget(BlockState state) {
        return !state.getMaterial().isSolid();
    }

    private static boolean validateTargets(BlockState... states) {
        return Arrays.stream(states).allMatch(TeleportHandler::validateTarget);
    }

    public static ElevatorBlock getElevator(BlockState state) {
        if (state.getBlock() instanceof ElevatorBlock elevator)
            return elevator;
        return null;
    }
}
