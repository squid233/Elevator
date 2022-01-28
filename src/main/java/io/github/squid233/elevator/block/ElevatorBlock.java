package io.github.squid233.elevator.block;

import io.github.squid233.elevator.block.entity.ElevatorBlockEntity;
import io.github.squid233.elevator.util.FakeUseContext;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.block.Blocks.IRON_BLOCK;

/**
 * @author squid233
 * @since 0.1.0
 */
@SuppressWarnings("deprecation")
public class ElevatorBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final BooleanProperty DIRECTIONAL = BooleanProperty.of("directional");
    public static final BooleanProperty SHOW_ARROW = BooleanProperty.of("show_arrow");
    public static final BooleanProperty CAMO = BooleanProperty.of("camo");

    private final DyeColor dyeColor;

    public ElevatorBlock(DyeColor color) {
        super(FabricBlockSettings.copyOf(IRON_BLOCK)
            .mapColor(color)
            .dynamicBounds()
            .nonOpaque());
        dyeColor = color;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        if (state.get(CAMO))
            return BlockRenderType.INVISIBLE;
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, DIRECTIONAL, SHOW_ARROW, CAMO);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(DIRECTIONAL, false).with(CAMO, false);
    }

    @Override
    public ElevatorBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ElevatorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public ElevatorBlockEntity createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return createBlockEntity(pos, state);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return false;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient)
            return ActionResult.SUCCESS;

        var be = getElevatorBE(world, pos);
        if (be == null)
            return ActionResult.FAIL;

        var handBlock = Block.getBlockFromItem(player.getStackInHand(hand).getItem());
        var stateToApply = handBlock.getPlacementState(new FakeUseContext(player, hand, hit));
        if (be.setCamoAndUpdate(stateToApply))
            return ActionResult.SUCCESS;
        if (player.isSneaking() && be.getHeldState() != null) {
            be.setCamoAndUpdate(null);
            return ActionResult.SUCCESS;
        }
        var factory = state.createScreenHandlerFactory(world, pos);
        if (factory != null)
            player.openHandledScreen(factory);
        return ActionResult.SUCCESS;
    }


    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        var be = getElevatorBE(world, pos);
        if (be != null && be.getHeldState() != null)
            return be.getHeldState().getCollisionShape(world, pos, context);
        return super.getCollisionShape(state, world, pos, context);
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        var be = getElevatorBE(world, pos);
        if (be != null && be.getHeldState() != null)
            return be.getHeldState().getOutlineShape(world, pos, context);
        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    public float getSlipperiness() {
        return super.getSlipperiness();
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        var be = getElevatorBE(world, pos);
        if (be != null && be.getHeldState() != null) {
            be.getHeldState().onEntityCollision(world, pos, entity);
            return;
        }
        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public float getVelocityMultiplier() {
        return super.getVelocityMultiplier();
    }

    @Override
    public float getJumpVelocityMultiplier() {
        return super.getJumpVelocityMultiplier();
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            var be = getElevatorBE(world, pos);
            if (be != null && be.getHeldState() != null) {
                final var updatedState = be.getHeldState().getStateForNeighborUpdate(direction, neighborState, world, pos, neighborPos);
                if (updatedState != be.getHeldState())
                    be.setHeldState(updatedState);
            }
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        var be = getElevatorBE(world, pos);
        if (be != null && be.getHeldState() != null)
            return be.getHeldState().getWeakRedstonePower(world, pos, direction);
        return super.getWeakRedstonePower(state, world, pos, direction);
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        var be = getElevatorBE(world, pos);
        if (be != null && be.getHeldState() != null)
            return be.getHeldState().getStrongRedstonePower(world, pos, direction);
        return super.getStrongRedstonePower(state, world, pos, direction);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return false;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        var be = getElevatorBE(world, pos);
        if (be != null && be.getHeldState() != null)
            return be.getHeldState().getAmbientOcclusionLightLevel(world, pos);
        return super.getAmbientOcclusionLightLevel(state, world, pos);
    }

    @Override
    public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        var be = getElevatorBE(world, pos);
        if (be != null && be.getHeldState() != null)
            return be.getHeldState().getOpacity(world, pos);
        return world.getMaxLightLevel();
    }

    @Override
    public BlockSoundGroup getSoundGroup(BlockState state) {
        return super.getSoundGroup(state);
    }

    public DyeColor getColor() {
        return dyeColor;
    }

    private ElevatorBlockEntity getElevatorBE(BlockView world, BlockPos pos) {
        if (world == null || pos == null)
            return null;
        // Check if it exists and is valid
        if (world.getBlockEntity(pos) instanceof ElevatorBlockEntity be
            && be.getType().supports(world.getBlockState(pos)))
            return be;
        return null;
    }
}
