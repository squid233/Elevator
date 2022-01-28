package io.github.squid233.elevator.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.squid233.elevator.ElevatorHandler.tryTeleport;

/**
 * @author squid233
 * @since 0.1.0
 */
@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    private static boolean lastSneaking;

    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "onKey", at = @At("TAIL"))
    private void onKey(long window,
                       int key,
                       int scancode,
                       int action,
                       int modifiers,
                       CallbackInfo ci) {
        if (client.currentScreen == null || client.currentScreen.passEvents) {
            onInput();
        }
    }

    private void onInput() {
        var player = client.player;
        if (player == null || player.isSpectator() || !player.isAlive() || player.input == null)
            return;

        var sneaking = player.input.sneaking;
        if (lastSneaking != sneaking) {
            lastSneaking = sneaking;
            if (sneaking)
                tryTeleport(player, Direction.DOWN);
        }
    }
}
