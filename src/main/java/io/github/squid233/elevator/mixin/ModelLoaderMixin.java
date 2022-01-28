package io.github.squid233.elevator.mixin;

import io.github.squid233.elevator.ElevatorMod;
import io.github.squid233.elevator.block.ArrowBlock;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author squid233
 * @since 0.2.0
 */
@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
    @Inject(method = "loadModelFromJson", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"), cancellable = true)
    public void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
        if (!ElevatorMod.MOD_ID.equals(id.getNamespace())) return;
        var str = id.toString();
        if (!str.contains("elevator233:block/arrow_")) return;
        var model = JsonUnbakedModel.deserialize(ArrowBlock.createModel(Direction.byName(str.substring(24))));
        model.id = str;
        cir.setReturnValue(model);
    }
}
