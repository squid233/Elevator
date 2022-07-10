package io.github.squid233.elevator.network;

import io.github.squid233.elevator.config.EModConfigs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldView;

import java.util.Arrays;

import static java.lang.Math.round;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class TeleportHandler {
    public static void handle(TeleportRequest req, ServerPlayerEntity player) {
        if (isBadTeleportPacket(req, player))
            return;

        if (EModConfigs.configurator.isUseXP() && !player.isCreative()) {
            int xpCost = EModConfigs.configurator.getXpPointsAmount();
            if (getPlayerExperienceProgress(player) - xpCost >= 0 || player.experienceLevel > 0) {
                player.addExperience(-xpCost);
            } else {
                player.sendSystemMessage(
                    new TranslatableText("elevator233.message.missing_xp")
                        .formatted(Formatting.RED),
                    player.getUuid()
                );
                return;
            }
        }

        var world = player.getWorld();
        var toPos = req.to();
        var toState = world.getBlockState(req.to());

        final float yaw = player.getYaw();
        final float pitch = (EModConfigs.configurator.isResetPitchDirectional()
            || EModConfigs.configurator.isResetPitchNormal())
            ? 0
            : player.getPitch();

        final double toX, toZ;
        if (EModConfigs.configurator.isPrecisionTarget()) {
            toX = toPos.getX() + 0.5;
            toZ = toPos.getZ() + 0.5;
        } else {
            toX = player.getX();
            toZ = player.getZ();
        }

        var blockYOffset = toState.getCollisionShape(world, toPos).getMax(Direction.Axis.Y);
        player.teleport(world, toX, toPos.getY() + blockYOffset, toZ, yaw, pitch);
        player.setVelocity(player.getVelocity().multiply(1, 0, 1));
        world.playSound(null,
            toPos,
            SoundEvents.ENTITY_ENDERMAN_TELEPORT,
            SoundCategory.BLOCKS,
            1,
            1);
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
            || (EModConfigs.configurator.isSameColor()
            && fromElevator != toElevator);
    }

    private static int getPlayerExperienceProgress(PlayerEntity player) {
        return round(player.experienceProgress * player.getNextLevelExperience());
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

    public static Block getElevator(BlockState state) {
        var block = state.getBlock();
        if (EModConfigs.configurator.hasCustomElevator(block))
            return block;
        return null;
    }
}
