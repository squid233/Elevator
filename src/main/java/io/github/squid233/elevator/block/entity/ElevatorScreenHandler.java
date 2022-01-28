package io.github.squid233.elevator.block.entity;

import io.github.squid233.elevator.ElevatorMod;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static io.github.squid233.elevator.block.entity.ESHTypes.ELEVATOR_SCREEN_HANDLER_TYPE;

/**
 * @author squid233
 * @since 0.2.0
 */
public class ElevatorScreenHandler extends ScreenHandler {
    private final Direction playerFacing;
    private ElevatorBlockEntity elevatorBE;
    private final BlockPos pos;

    public ElevatorScreenHandler(int syncId, BlockPos pos, PlayerEntity player) {
        super(ELEVATOR_SCREEN_HANDLER_TYPE, syncId);
        var blockEntity = player.world.getBlockEntity(pos);
        if (blockEntity instanceof ElevatorBlockEntity)
            elevatorBE = (ElevatorBlockEntity) blockEntity;
        playerFacing = player.getHorizontalFacing();
        this.pos = pos;
    }

    public ElevatorScreenHandler(int syncId, PlayerInventory inv, PacketByteBuf buf) {
        this(syncId, buf.readBlockPos(), inv.player);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(ScreenHandlerContext.create(player.world, elevatorBE.getPos()), player, elevatorBE.getCachedState().getBlock());
    }

    public BlockPos getPos() {
        return pos;
    }

    public ElevatorBlockEntity getBlockEntity() {
        return elevatorBE;
    }

    public Direction getPlayerFacing() {
        return playerFacing;
    }

    public static ScreenHandlerType<ElevatorScreenHandler> registerSHType() {
        return ScreenHandlerRegistry.registerExtended(
            new Identifier(ElevatorMod.MOD_ID, "elevator_screen_handler"),
            ElevatorScreenHandler::new
        );
    }
}
