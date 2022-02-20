package io.github.squid233.elevator.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.squid233.elevator.ElevatorHandler.tryTeleport;

/**
 * @author squid233
 * @since 0.1.0
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType,
                                World world) {
        super(entityType, world);
    }

    @Inject(method = "jump", at = @At("RETURN"))
    public void jump(CallbackInfo ci) {
        if (world.isClient && (Object) this instanceof ClientPlayerEntity e) {
            tryTeleport(e, Direction.UP);
        }
    }
}
