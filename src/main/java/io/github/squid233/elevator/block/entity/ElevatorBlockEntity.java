package io.github.squid233.elevator.block.entity;

import io.github.squid233.elevator.block.ElevatorBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static io.github.squid233.elevator.block.entity.EBlockEntityTypes.ELEVATOR_BLOCK_ENTITY;

/**
 * @author squid233
 * @since 0.2.0
 */
public class ElevatorBlockEntity
    extends BlockEntity
    implements ExtendedScreenHandlerFactory {
    @Nullable
    private BlockState heldState;

    public ElevatorBlockEntity(BlockPos pos, BlockState state) {
        super(ELEVATOR_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        var heldId = NbtHelper.toBlockState(nbt.getCompound("held_id"));
        heldState = isValidState(heldId) ? heldId : null;
        updateClient();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (heldState != null)
            nbt.put("held_id", NbtHelper.fromBlockState(heldState));
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbtWithId();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this, blockEntity -> createNbtWithId());
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("screen.elevator233.elevator");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ElevatorScreenHandler(syncId, pos, player);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void setHeldState(BlockState heldState) {
        this.heldState = heldState;
        markUpdated();
    }

    private void markUpdated() {
        markDirty();
        if (world != null) {
            world.setBlockState(pos,
                getCachedState().with(ElevatorBlock.CAMO,
                    heldState != null)
            );
            world.updateListeners(
                getPos(),
                getCachedState(),
                getCachedState(),
                Block.NOTIFY_ALL
            );
            world.updateNeighborsAlways(getPos(), getCachedState().getBlock());
            getCachedState().updateNeighbors(world, pos, 2);
            world.getChunkManager().getLightingProvider().checkBlock(getPos());
        }
    }

    @Nullable
    public BlockState getHeldState() {
        return heldState;
    }

    private void updateClient() {
        if (world != null && world.isClient) {
            world.updateListeners(
                getPos(),
                getCachedState(),
                getCachedState(),
                Block.NOTIFY_ALL
            );
            world.getChunkManager().getLightingProvider().checkBlock(getPos());
        }
    }

    public boolean setCamoAndUpdate(@Nullable BlockState newState) {
        if (heldState == newState)
            return false;
        if (!isValidState(newState))
            return false;
        var originalState = heldState;
        setHeldState(newState);
        if (getWorld() != null)
            if (newState == null) {
                if (originalState != null) {
                    var group = originalState.getSoundGroup();
                    getWorld().playSound(null,
                        getPos(),
                        group.getBreakSound(),
                        SoundCategory.BLOCKS,
                        group.volume,
                        group.pitch);
                }
            } else {
                var group = newState.getSoundGroup();
                getWorld().playSound(null,
                    getPos(),
                    group.getPlaceSound(),
                    SoundCategory.BLOCKS,
                    group.volume,
                    group.pitch);
            }
        return true;
    }

    public static boolean isValidState(BlockState state) {
        if (state == null)
            return true;
        if (state.isAir())
            return false;
        // Block entities can cause problems
        if (state.hasBlockEntity())
            return false;
        // Don't try to camouflage with itself
        if (state.getBlock() instanceof ElevatorBlock) {
            return false;
        }
        // Only normally rendered blocks (not chests, ...)
        if (state.getRenderType() != BlockRenderType.MODEL) {
            return false;
        }
        // Only blocks with a collision box
        return state.getMaterial().isSolid();
    }

    public static BlockEntityType<ElevatorBlockEntity> getType(Block... validBlocks) {
        return FabricBlockEntityTypeBuilder.create(ElevatorBlockEntity::new, validBlocks).build(null);
    }
}
