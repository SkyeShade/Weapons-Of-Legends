package net.skyeshade.wol.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.skyeshade.wol.stats.event.ModEvents;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class ExplosionUtil {
    //public static int ticks = 0;


    private static final HashMap<Integer, ImmutableSet<BlockPos>> EXPLOSION_CACHE = new HashMap<>();


    int staticTickCount = ModEvents.tickcount;
    int deltaTicks = ModEvents.tickcount - staticTickCount;

    /**
     * Returns a set of block positions used to offset from a center to form a sphere.
     *
     * @param radius radius of sphere
     * @param power how strong blocks it can break
     * @param world world
     * @param blockpos pos center of explosion
     * @param player player
     *
     * @return set of block positions
     */

    public static ImmutableSet<BlockPos> getExplosionBlockOffsets(int radius, float power, float damage, Boolean fire, Level world, BlockPos blockpos, Player player, boolean dropBlocks) {
        //if (!EXPLOSION_CACHE.containsKey(radius) && !EXPLOSION_CACHE.containsKey(power) && !EXPLOSION_CACHE.containsKey(world) && !EXPLOSION_CACHE.containsKey(blockpos)) {










                    //System.out.println("testtick" + worldTicks + "loop: " +worldTicks);

                    HashSet<BlockPos> offsets = new HashSet<>();

                    Random rand = new Random();

                    int radiusExplanding = radius;

                    int radiusExplosionCenter = Math.round(radiusExplanding / 1.5F);



                    long startSphereCalcTime = System.currentTimeMillis();
                    System.out.println("Started calculation of explosion block positions at a diameter of " + radius*2);

                    for (int i = -radiusExplanding + 1; i < radiusExplanding; i++) {
                        for (int j = -radiusExplanding + 1; j < radiusExplanding; j++) {
                            for (int k = -radiusExplanding + 1; k < radiusExplanding; k++) {
                                double distance = Math.sqrt(i * i + j * j + k * k);

                                int rand_int1 = rand.nextInt(100);

                                if (rand_int1 >= 50) {
                                    if (distance <= radiusExplanding) {
                                        if (distance >= radiusExplanding-100) {
                                            if (j + blockpos.getY() >= 1 || j + blockpos.getY() <= 230) {
                                                BlockPos blocksRemove = new BlockPos(i, j, k);
                                                if (!world.getBlockState(blocksRemove.offset(blockpos)).is(Blocks.BEDROCK)) {
                                                    if (!world.getBlockState(blocksRemove.offset(blockpos)).is(Blocks.AIR)) {
                                                        offsets.add(new BlockPos(i, j, k));
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                    for (int i = -radiusExplosionCenter + 1; i < radiusExplosionCenter; i++) {
                        for (int j = -radiusExplosionCenter + 1; j < radiusExplosionCenter; j++) {
                            for (int k = -radiusExplosionCenter + 1; k < radiusExplosionCenter; k++) {
                                double distance = Math.sqrt(i * i + j * j + k * k);

                                if (distance <= radiusExplosionCenter) {
                                    if (distance >= radiusExplosionCenter-100) {
                                        if (j + blockpos.getY() >= 1 || j + blockpos.getY() <= 230) {

                                            BlockPos blocksRemove = new BlockPos(i, j, k);
                                            if (!world.getBlockState(blocksRemove.offset(blockpos)).is(Blocks.BEDROCK)) {
                                                if (!world.getBlockState(blocksRemove.offset(blockpos)).is(Blocks.AIR)) {
                                                    offsets.add(blocksRemove);
                                                }
                                            }
                                        }

                                    }

                                }
                            }
                        }
                    }

                    long endSphereCalcTime = System.currentTimeMillis();
                    System.out.println("Calculation of block positions took: " + (endSphereCalcTime - startSphereCalcTime) + "ms");

                    long startCache = System.currentTimeMillis();
                    EXPLOSION_CACHE.put(radius, ImmutableSet.copyOf(offsets));
                    long endCache = System.currentTimeMillis();
                    System.out.println("Caching of block positions: " + (endCache - startCache) + "ms");
                    //}
                    System.out.println("Started placement of block positions");
                    long startSpherePlaceTime = System.currentTimeMillis();
                    for (BlockPos ii : EXPLOSION_CACHE.get(radius)) {
                        //world.removeBlock(ii.offset(blockpos),true);'
                        if (dropBlocks)
                            world.destroyBlock(ii.offset(blockpos),true);
                        if (!world.getBlockState(ii.offset(blockpos)).is(Blocks.BEDROCK)) {
                            if (!world.getBlockState(ii.offset(blockpos)).is(Blocks.AIR)) {
                                world.setBlock(ii.offset(blockpos),Blocks.AIR.defaultBlockState(), 1-2-16-32);
                            }
                        }

                    }
                    //log time thing lolololol
                    long endSpherePlaceTime = System.currentTimeMillis();
                    System.out.println("Placement of block positions took: " + (endSpherePlaceTime - startSpherePlaceTime) + "ms");


                    EXPLOSION_CACHE.clear();






            //if ()

            //TickCounter.ticks(worldTicks);
            //ExplosionUtil.ticks++;




        // damage all entities within radius for loop
        /*for (Entity iii : world.getEntities(null, player.getBoundingBox().expand(radius), entity -> true)) {

            iii.damage(DamageSource.explosion(player), damage);

            //set entities within radius on fire if true
            if (fire = true){
                iii.setFireTicks(200);
            }
        }

        tickTracker.clear();*/
        return EXPLOSION_CACHE.get(radius);




    }
}

