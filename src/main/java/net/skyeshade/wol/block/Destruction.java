package net.skyeshade.wol.block;



import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.skyeshade.wol.client.ClientStatsData;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.destruction.DestructionActiveDataSyncS2CPacket;
import net.skyeshade.wol.stats.PlayerStatsProvider;
import org.jetbrains.annotations.NotNull;

public class Destruction extends BaseDestruction {
    public static final int MAX_AGE = 1500;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty UP = PipeBlock.UP;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((p_53467_) -> {
        return p_53467_.getKey() != Direction.DOWN;
    }).collect(Util.toMap());
    private static final VoxelShape UP_AABB = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private final Map<BlockState, VoxelShape> shapesCache;
    private static final int IGNITE_INSTANT = 60;
    private static final int IGNITE_EASY = 30;
    private static final int IGNITE_MEDIUM = 15;
    private static final int IGNITE_HARD = 5;
    private static final int BURN_INSTANT = 100;
    private static final int BURN_EASY = 60;
    private static final int BURN_MEDIUM = 20;
    private static final int BURN_HARD = 5;
    private final Object2IntMap<Block> igniteOdds = new Object2IntOpenHashMap<>();
    private final Object2IntMap<Block> burnOdds = new Object2IntOpenHashMap<>();

    public Destruction(BlockBehaviour.Properties p_53425_) {
        super(p_53425_, 10.0F);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)).setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)).setValue(UP, Boolean.valueOf(false)));
        this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream().filter((p_53497_) -> {
            return p_53497_.getValue(AGE) == 0;
        }).collect(Collectors.toMap(Function.identity(), Destruction::calculateShape)));
    }

    private static VoxelShape calculateShape(BlockState bState) {
        VoxelShape voxelshape = Shapes.empty();
        if (bState.getValue(UP)) {
            voxelshape = UP_AABB;
        }

        if (bState.getValue(NORTH)) {
            voxelshape = Shapes.or(voxelshape, NORTH_AABB);
        }

        if (bState.getValue(SOUTH)) {
            voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
        }

        if (bState.getValue(EAST)) {
            voxelshape = Shapes.or(voxelshape, EAST_AABB);
        }

        if (bState.getValue(WEST)) {
            voxelshape = Shapes.or(voxelshape, WEST_AABB);
        }

        return voxelshape.isEmpty() ? DOWN_AABB : voxelshape;
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return this.canSurvive(pState, pLevel, pCurrentPos) ? this.getStateWithAge(pLevel, pCurrentPos, pState.getValue(AGE)) : Blocks.AIR.defaultBlockState();
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.shapesCache.get(pState.setValue(AGE, Integer.valueOf(0)));
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.getStateForPlacement(pContext.getLevel(), pContext.getClickedPos());
    }

    protected BlockState getStateForPlacement(BlockGetter pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        BlockState blockstate = pLevel.getBlockState(blockpos);
        if (!this.canCatchFire(pLevel, pPos, Direction.UP) && !blockstate.isFaceSturdy(pLevel, blockpos, Direction.UP)) {
            BlockState blockstate1 = this.defaultBlockState();

            for(Direction direction : Direction.values()) {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(direction);
                if (booleanproperty != null) {
                    blockstate1 = blockstate1.setValue(booleanproperty, Boolean.valueOf(this.canCatchFire(pLevel, pPos.relative(direction), direction.getOpposite())));
                }
            }

            return blockstate1;
        } else {
            return this.defaultBlockState();
        }
    }

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        return pLevel.getBlockState(blockpos).isFaceSturdy(pLevel, blockpos, Direction.UP) || this.isValidFireLocation(pLevel, pPos);
    }
    ArrayList<Boolean> activeBooleanList = new ArrayList<>();
    ArrayList<Player> activeBooleanPlayersList = new ArrayList<>();
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        for (Player player : pLevel.players()) {
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                activeBooleanList.add(stats.getDestructionActive());
                if (stats.getDestructionActive()) {
                    activeBooleanPlayersList.add(player);
                }
            });
        }
        for (Player player : activeBooleanPlayersList) {
            if (Math.abs(pPos.getX()-player.getX()) < 5 && (pPos.getY()+64)-(player.getY()+64) < 5 && Math.abs(pPos.getZ()-player.getZ()) < 5) {
                pLevel.removeBlock(pPos, false);
                return;
            }
        }
        if (!activeBooleanList.contains(true)) {
            pLevel.removeBlock(pPos, false);
            activeBooleanList.clear();
            return;
        }
        activeBooleanList.clear();
        activeBooleanPlayersList.clear();


        pLevel.scheduleTick(pPos, this, getFireTickDelay(pLevel.random));
        if (!pState.canSurvive(pLevel, pPos)) {
            pLevel.removeBlock(pPos, false);
        }


        BlockState blockstate = pLevel.getBlockState(pPos.below());
        boolean flag = blockstate.isFireSource(pLevel, pPos, Direction.UP);
        int i = pState.getValue(AGE);
        if (!flag && pLevel.isRaining() && this.isNearRain(pLevel, pPos) && pRandom.nextFloat() < 0.2F + (float)i * 0.03F) {
            pLevel.removeBlock(pPos, false);
        } else {
            int j = Math.min(15, i + pRandom.nextInt(3) / 2);
            if (i != j) {
                pState = pState.setValue(AGE, Integer.valueOf(j));
                pLevel.setBlock(pPos, pState, 4);
            }

            if (!flag) {
                if (!this.isValidFireLocation(pLevel, pPos)) {
                    BlockPos blockpos = pPos.below();
                    if (!pLevel.getBlockState(blockpos).isFaceSturdy(pLevel, blockpos, Direction.UP) || i > 3) {
                        pLevel.removeBlock(pPos, false);
                    }

                    return;
                }

                if (i == 15 && pRandom.nextInt(4) == 0 && !this.canCatchFire(pLevel, pPos.below(), Direction.UP)) {
                    pLevel.removeBlock(pPos, false);
                    return;
                }
            }

            boolean flag1 = pLevel.isHumidAt(pPos);
            int k = flag1 ? -50 : 0;
            this.tryCatchFire(pLevel, pPos.east(), 300 + k, pRandom, i, Direction.WEST);
            this.tryCatchFire(pLevel, pPos.west(), 300 + k, pRandom, i, Direction.EAST);
            this.tryCatchFire(pLevel, pPos.below(), 100 + k, pRandom, i, Direction.UP);
            this.tryCatchFire(pLevel, pPos.above(), 300 + k, pRandom, i, Direction.DOWN);
            this.tryCatchFire(pLevel, pPos.north(), 300 + k, pRandom, i, Direction.SOUTH);
            this.tryCatchFire(pLevel, pPos.south(), 300 + k, pRandom, i, Direction.NORTH);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(int l = -1; l <= 1; ++l) {
                for(int i1 = -1; i1 <= 1; ++i1) {
                    for(int j1 = -1; j1 <= 4; ++j1) {
                        if (l != 0 || j1 != 0 || i1 != 0) {
                            int k1 = 100;
                            if (j1 > 1) {
                                k1 += (j1 - 1) * 100;
                            }

                            blockpos$mutableblockpos.setWithOffset(pPos, l, j1, i1);
                            int l1 = this.getIgniteOdds(pLevel, blockpos$mutableblockpos);
                            if (l1 > 0) {
                                int i2 = (l1 + 40 + pLevel.getDifficulty().getId() * 7) / (i + 30);
                                if (flag1) {
                                    i2 /= 2;
                                }

                                if (i2 > 0 && pRandom.nextInt(k1) <= i2 && (!pLevel.isRaining() || !this.isNearRain(pLevel, blockpos$mutableblockpos))) {
                                    int j2 = Math.min(15, i + pRandom.nextInt(5) / 4);
                                    pLevel.setBlock(blockpos$mutableblockpos, this.getStateWithAge(pLevel, blockpos$mutableblockpos, j2), 3);
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    protected boolean isNearRain(Level pLevel, BlockPos pPos) {
        return false;
    }

    @Deprecated //Forge: Use IForgeBlockState.getFlammability, Public for default implementation only.
    public int getBurnOdds(BlockState pState) {
        return pState.hasProperty(BlockStateProperties.WATERLOGGED) && pState.getValue(BlockStateProperties.WATERLOGGED) ? 0 : this.burnOdds.getInt(pState.getBlock());
    }

    @Deprecated //Forge: Use IForgeBlockState.getFireSpreadSpeed
    public int getIgniteOdds(BlockState pState) {
        return pState.hasProperty(BlockStateProperties.WATERLOGGED) && pState.getValue(BlockStateProperties.WATERLOGGED) ? 0 : this.igniteOdds.getInt(pState.getBlock());
    }

    private void tryCatchFire(Level level, BlockPos blockPos, int value, RandomSource rSource, int p_53436_, Direction face) {
        int i = 15;
        if (rSource.nextInt(value) < i) {
            BlockState blockstate = level.getBlockState(blockPos);
            if (rSource.nextInt(value + 10) < 5 && !level.isRainingAt(blockPos)) {
                int j = Math.min(value + rSource.nextInt(5) / 4, 15);
                level.setBlock(blockPos, this.getStateWithAge(level, blockPos, j), 3);
            } else {
                level.removeBlock(blockPos, false);
            }

            blockstate.onCaughtFire(level, blockPos, face, null);
        }

    }

    private BlockState getStateWithAge(LevelAccessor pLevel, BlockPos pPos, int pAge) {
        BlockState blockstate = getState(pLevel, pPos);
        return blockstate.is(this) ? blockstate.setValue(AGE, Integer.valueOf(pAge)) : blockstate;
    }

    private boolean isValidFireLocation(BlockGetter pLevel, BlockPos pPos) {
        for(Direction direction : Direction.values()) {
            if (this.canCatchFire(pLevel, pPos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    private int getIgniteOdds(LevelReader pLevel, BlockPos pPos) {
        if (!pLevel.isEmptyBlock(pPos)) {

            if (pLevel.getBlockState(pPos).is(Blocks.WATER)) {
                return 1000;
            } else {
                return 0;
            }

        } else {
            int i = 0;

            for(Direction direction : Direction.values()) {
                //BlockState blockstate = pLevel.getBlockState(pPos.relative(direction));
                i = 1000;
            }

            return i;
        }
    }

    @Deprecated //Forge: Use canCatchFire with more context
    protected boolean canBurn(BlockState pState) {
        return this.getIgniteOdds(pState) > 0;
    }

    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        pLevel.scheduleTick(pPos, this, getFireTickDelay(pLevel.random));
    }

    /**
     * Gets the delay before this block ticks again (without counting random ticks)
     */
    private static int getFireTickDelay(RandomSource pRandom) {
        return 20 + pRandom.nextInt(10);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE, NORTH, EAST, SOUTH, WEST, UP);
    }

    private void setFlammable(Block pBlock, int pEncouragement, int pFlammability) {
        if (pBlock == Blocks.AIR) throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
        this.igniteOdds.put(pBlock, pEncouragement);
        this.burnOdds.put(pBlock, pFlammability);
    }

    /**
     * Side sensitive version that calls the block function.
     *
     * @param world The current world
     * @param pos Block position
     * @param face The side the fire is coming from
     * @return True if the face can catch fire.
     */
    public boolean canCatchFire(BlockGetter world, BlockPos pos, Direction face) {

        if (!activeBooleanPlayersList.isEmpty()) {
            for (Player player : activeBooleanPlayersList) {
                if (Math.abs(pos.getX()-player.getX()) < 5 && (pos.getY()+64)-(player.getY()+64) < 5 && Math.abs(pos.getZ()-player.getZ()) < 5) {

                    return false;
                }else {
                    return !world.getBlockState(pos).isAir() && !world.getBlockState(pos).is(this);
                }
            }
        }

        return !world.getBlockState(pos).isAir() && !world.getBlockState(pos).is(this);
    }


}