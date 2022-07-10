package io.github.squid233.elevator.network;

import io.github.squid233.elevator.ElevatorMod;
import net.minecraft.util.Identifier;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class EModNetworkingConstants {
    public static final Identifier TELEPORT_PACKET_ID =
        new Identifier(ElevatorMod.MOD_ID, "teleport");
    public static final Identifier SET_FACING_PACKET_ID =
        new Identifier(ElevatorMod.MOD_ID, "set_facing");
    public static final Identifier SET_DIRECTIONAL_PACKET_ID =
        new Identifier(ElevatorMod.MOD_ID, "set_directional");
    public static final Identifier SET_ARROW_PACKET_ID =
        new Identifier(ElevatorMod.MOD_ID, "set_arrow");
    public static final Identifier REMOVE_CAMO_PACKET_ID =
        new Identifier(ElevatorMod.MOD_ID, "remove_camo");
}
