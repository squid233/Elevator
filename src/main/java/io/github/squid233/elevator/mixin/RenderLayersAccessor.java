package io.github.squid233.elevator.mixin;

import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author squid233
 * @since 0.2.0
 */
@Mixin(RenderLayers.class)
public interface RenderLayersAccessor {
    @Accessor
    static boolean isFancyGraphicsOrBetter() {
        throw new AssertionError();
    }
}
