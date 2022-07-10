package io.github.squid233.elevator;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.squid233.elevator.network.NetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static io.github.squid233.elevator.config.Configurator.*;
import static io.github.squid233.elevator.config.EModConfigs.*;
import static net.minecraft.command.argument.BlockStateArgumentType.blockState;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ElevatorMod implements ModInitializer {
    public static final String MOD_ID = "elevator233";
    public static final Logger LOGGER = LoggerFactory.getLogger("ElevatorMod");

    @Override
    public void onInitialize() {
        LOGGER.debug("Registering objects");
        NetworkHandler.init();
        LOGGER.debug("Registered all objects");
        loadAllCfg();
        registerCmd();
        LOGGER.debug("Common side: done");
    }

    private static void registerCmd() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
                var set = literal("set");
                var reset = literal("reset");
                var cfg = literal("config");

                setCfg(set, reset, "sameColor", DEF_SAME_COLOR, configurator::setSameColor);
                setCfg(set, reset, "range", DEF_RANGE, 3, 4064, configurator::setRange);
                setCfg(set, reset, "precisionTarget", DEF_PRECISION_TARGET, configurator::setPrecisionTarget);
                setCfg(set, reset, "resetPitchNormal", DEF_RESET_PITCH_NORMAL, configurator::setResetPitchNormal);
                setCfg(set, reset, "resetPitchDirectional", DEF_RESET_PITCH_DIRECTIONAL, configurator::setResetPitchDirectional);
                setCfg(set, reset, "useXP", DEF_USE_XP, configurator::setUseXP);
                setCfg(set, reset, "xpPointsAmount", DEF_XP_POINTS_AMOUNT, 1, Integer.MAX_VALUE, configurator::setXpPointsAmount);
                set.then(literal("customElevator").then(argument("block", blockState()).executes(ctx ->
                    setCfg(ctx.getArgument("block", BlockStateArgument.class), arg -> configurator.addCustomElevator(arg.getBlockState().getBlock())))));
                reset.then(literal("customElevator").executes(ctx ->
                    setCfg(null, t -> configurator.clearCustomElevator())));

                cfg.then(set);
                cfg.then(reset);
                cfg.then(literal("reload").executes(ctx -> {
                    loadAllCfg();
                    return SINGLE_SUCCESS;
                }));
                dispatcher.register(
                    literal("elevator233").then(cfg)
                );
            }
        );
    }

    private static String toBoolStr(String s) {
        final var ca = s.toCharArray();
        if (ca[0] >= 'a' && ca[0] <= 'z')
            ca[0] -= 32;
        return new String(ca);
    }

    private static void setCfg(LiteralArgumentBuilder<ServerCommandSource> set,
                               LiteralArgumentBuilder<ServerCommandSource> reset,
                               String cmd,
                               int defValue,
                               int min,
                               int max,
                               Consumer<Integer> consumer) {
        set.then(literal(cmd).then(argument(cmd, integer(min, max)).executes(ctx ->
            setCfg(ctx.getArgument(cmd, int.class), consumer))));
        reset.then(literal(cmd).executes(ctx ->
            setCfg(defValue, consumer)));
    }

    private static void setCfg(LiteralArgumentBuilder<ServerCommandSource> set,
                               LiteralArgumentBuilder<ServerCommandSource> reset,
                               String cmd,
                               boolean defValue,
                               Consumer<Boolean> consumer) {
        var boolCmd = "is" + toBoolStr(cmd);
        set.then(literal(cmd).then(argument(boolCmd, bool()).executes(ctx ->
            setCfg(ctx.getArgument(boolCmd, boolean.class), consumer))));
        reset.then(literal(cmd).executes(ctx ->
            setCfg(defValue, consumer)));
    }

    private static <T> int setCfg(T value,
                                  Consumer<T> consumer) {
        consumer.accept(value);
        writeAllCfg(StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        return SINGLE_SUCCESS;
    }
}
