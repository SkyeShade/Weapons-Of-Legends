package net.skyeshade.wol.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.skyeshade.wol.stats.event.ModEvents;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class ExplosionUtil {
    //public static int ticks = 0;


    private static final HashMap<Integer, ImmutableSet<BlockPos>> EXPLOSION_CACHE = new HashMap<>();

    private static final HashMap<Integer, ImmutableSet<BlockPos>> SMALL_DAMAGE_CACHE = new HashMap<>();


    int staticTickCount = ModEvents.tickcount;
    int deltaTicks = ModEvents.tickcount - staticTickCount;

    /**
     * Returns a set of block positions used to offset from a center to form a sphere.
     *
     * @param radius radius of sphere
     * @param damage damage applied to entities in range
     * @param fire should put fire or not
     * @param world world
     * @param blockpos pos center of explosion
     * @param player player
     * @param dropBlocks if it should drop the blocks it breaks
     *
     * @return set of block positions
     */

    public static ImmutableSet<BlockPos> getExplosionBlockOffsets(int radius, float damage, Boolean fire, Level world, BlockPos blockpos, Player player, boolean dropBlocks) {
        //if (!EXPLOSION_CACHE.containsKey(radius) && !EXPLOSION_CACHE.containsKey(power) && !EXPLOSION_CACHE.containsKey(world) && !EXPLOSION_CACHE.containsKey(blockpos)) {










                    //System.out.println("testtick" + worldTicks + "loop: " +worldTicks);

                    HashSet<BlockPos> offsets = new HashSet<>();

                    HashSet<BlockPos> offsets2 = new HashSet<>();
                    Random rand = new Random();

                    int radiusMedium = radius/2;

                    int radiusExplosionCenter = Math.round(radiusMedium / 1.5F);


                    int smallDamageRadius = Math.round((radius/2f)*1.5f);

                    int smallDamageOuter = Math.round(smallDamageRadius *2);



                    long startSphereCalcTime = System.currentTimeMillis();
                    System.out.println("Started calculation of explosion block positions at a diameter of " + radius*2);


                    //medium circle of damage
                    for (int i = -radiusMedium + 1; i < radiusMedium; i++) {
                        for (int j = -radiusMedium + 1; j < radiusMedium; j++) {
                            for (int k = -radiusMedium + 1; k < radiusMedium; k++) {
                                double distance = Math.sqrt(i * i + j * j + k * k);

                                int rand_int1 = rand.nextInt(100);

                                if (rand_int1 >= 50) {
                                    if (distance <= radiusMedium) {
                                        if (distance >= radiusExplosionCenter) {
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
                    //center explosion
                    for (int i = -radiusExplosionCenter + 1; i < radiusExplosionCenter; i++) {
                        for (int j = -radiusExplosionCenter + 1; j < radiusExplosionCenter; j++) {
                            for (int k = -radiusExplosionCenter + 1; k < radiusExplosionCenter; k++) {
                                double distance = Math.sqrt(i * i + j * j + k * k);

                                if (distance <= radiusExplosionCenter) {

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









                    EXPLOSION_CACHE.put(radius, ImmutableSet.copyOf(offsets));

                    for (BlockPos ii : EXPLOSION_CACHE.get(radius)) {
                        //world.removeBlock(ii.offset(blockpos),true);'
                        if (dropBlocks)
                            world.destroyBlock(ii.offset(blockpos),true);
                        if (!world.getBlockState(ii.offset(blockpos)).is(Blocks.BEDROCK)) {
                            if (!world.getBlockState(ii.offset(blockpos)).is(Blocks.AIR)) {
                                for (int i = 0; i <= (radius / 5); i++) {
                                    int rand_int1 = rand.nextInt(100);
                                    int randx = rand.nextInt(-1, 1);
                                    int randy = rand.nextInt(-1, 1);
                                    int randz = rand.nextInt(-1, 1);
                                    world.setBlock(ii.offset(blockpos), Blocks.AIR.defaultBlockState(), 1 - 2 - 16 - 32);
                                    if (fire) {

                                        if (rand_int1 <= 25 * (radius/2) && (world.getBlockState(ii.offset(blockpos.offset(randx, randy, randz))).getMaterial().equals(Material.STONE)||world.getBlockState(ii.offset(blockpos.offset(randx, randy, randz))).getMaterial().equals(Material.DIRT)||world.getBlockState(ii.offset(blockpos.offset(randx, randy, randz))).getMaterial().equals(Material.SAND)||world.getBlockState(ii.offset(blockpos.offset(randx, randy, randz))).getMaterial().equals(Material.GRASS)) && radius >= 6) {
                                            world.setBlock(ii.offset(blockpos.offset(randx, randy, randz)), Blocks.MAGMA_BLOCK.defaultBlockState(), 1 - 2 - 16 - 32);
                                        }
                                        if (rand_int1 <= (radius / 40) && world.getBlockState(ii.offset(blockpos.offset(randx, randy, randz))).getMaterial().equals(Material.STONE) && radius >= 11) {
                                            world.setBlock(ii.offset(blockpos.offset(randx, randy, randz)), Blocks.LAVA.defaultBlockState(), 1 - 2 - 16 - 32);
                                        }
                                        if (rand_int1 >= 50) {
                                            world.setBlock(ii.offset(blockpos), Blocks.FIRE.defaultBlockState(), 1 - 2 - 16 - 32);
                                        }

                                    }
                                }
                                world.getLightEngine().checkBlock(ii.offset(blockpos));
                            }

                        }

                    }
                    //log time thing lolololol


                    offsets.clear();
                    EXPLOSION_CACHE.clear();


                    ////////grass ruining



        //medium circle of damage
        for (int i = -smallDamageOuter + 1; i < smallDamageOuter; i++) {
            for (int j = -smallDamageOuter + 1; j < smallDamageOuter; j++) {
                for (int k = -smallDamageOuter + 1; k < smallDamageOuter; k++) {
                    double distance = Math.sqrt(i * i + j * j + k * k);

                    int rand_int1 = rand.nextInt(100);


                    if (rand_int1 >= 50) {
                        if (distance <= smallDamageOuter) {
                            if (distance >= smallDamageRadius) {
                                if (j + blockpos.getY() >= 1 || j + blockpos.getY() <= 230) {
                                    BlockPos blocksRemove = new BlockPos(i, j, k);
                                    BlockState checkBlockstate = world.getBlockState(blocksRemove.offset(blockpos));
                                    if (checkBlockstate.is(Blocks.GRASS_BLOCK) || checkBlockstate.is(Blocks.DIRT) ||checkBlockstate.getMaterial().equals(Material.LEAVES) || checkBlockstate.getMaterial().equals(Material.PLANT) || checkBlockstate.getMaterial().equals(Material.GRASS)) {
                                        offsets2.add(new BlockPos(i, j, k));
                                    }else if (fire){
                                        if (rand_int1 >= 75 && world.getBlockState(blockpos.offset(i, 1+j, k)).is(Blocks.AIR)) {
                                            world.setBlock(blockpos.offset(i, 1+j, k), Blocks.FIRE.defaultBlockState(), 1 - 2 - 16 - 32);
                                        }
                                    }

                                }

                            }
                        }
                    }
                }
            }
        }
        //center explosion
        for (int i = -smallDamageRadius + 1; i < smallDamageRadius; i++) {
            for (int j = -smallDamageRadius + 1; j < smallDamageRadius; j++) {
                for (int k = -smallDamageRadius + 1; k < smallDamageRadius; k++) {
                    double distance = Math.sqrt(i * i + j * j + k * k);

                    if (distance <= smallDamageRadius) {

                        if (j + blockpos.getY() >= 1 || j + blockpos.getY() <= 230) {
                            int rand_int1 = rand.nextInt(100);

                            BlockPos blocksRemove = new BlockPos(i, j, k);
                            BlockState checkBlockstate = world.getBlockState(blocksRemove.offset(blockpos));
                            if (checkBlockstate.is(Blocks.GRASS_BLOCK) || checkBlockstate.is(Blocks.DIRT) || checkBlockstate.getMaterial().equals(Material.LEAVES) || checkBlockstate.getMaterial().equals(Material.PLANT) || checkBlockstate.getMaterial().equals(Material.GRASS)) {
                                    offsets2.add(blocksRemove);
                            }else if (fire){
                                if (rand_int1 >= 50 && world.getBlockState(blockpos.offset(i, 1+j, k)).is(Blocks.AIR)) {
                                    world.setBlock(blockpos.offset(i, 1+j, k), Blocks.FIRE.defaultBlockState(), 1 - 2 - 16 - 32);
                                }
                            }

                        }
                    }
                }
            }
        }









        SMALL_DAMAGE_CACHE.put(smallDamageRadius, ImmutableSet.copyOf(offsets2));


        for (BlockPos ii : SMALL_DAMAGE_CACHE.get(smallDamageRadius)) {
            //world.removeBlock(ii.offset(blockpos),true);'

            int rand_int1 = rand.nextInt(100);
            if (world.getBlockState(ii.offset(blockpos)).is(Blocks.GRASS_BLOCK)) {
                world.setBlock(ii.offset(blockpos),Blocks.DIRT.defaultBlockState(), 1-2-16-32);

            }else if (!world.getBlockState(ii.offset(blockpos)).is(Blocks.DIRT)){

                world.setBlock(ii.offset(blockpos),Blocks.AIR.defaultBlockState(), 1-2-16-32);

            }
            if (fire) {
                if (rand_int1 >= 75 && world.getBlockState(ii.offset(blockpos.offset(0, 1, 0))).is(Blocks.AIR)) {
                    world.setBlock(ii.offset(blockpos.offset(0,1,0)), Blocks.FIRE.defaultBlockState(), 1 - 2 - 16 - 32);
                }
            }
            world.getLightEngine().checkBlock(ii.offset(blockpos));


        }
        //log time thing lolololol


        offsets2.clear();
        SMALL_DAMAGE_CACHE.clear();

        long endSphereCalcTime = System.currentTimeMillis();
        System.out.println("Calculation of block positions took: " + (endSphereCalcTime - startSphereCalcTime) + "ms");


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
        Vec3 vec3 = new Vec3(blockpos.getX(), blockpos.getY(),blockpos.getZ());
        for (Entity entity : world.getEntities((Entity) null, AABB.ofSize(vec3, smallDamageOuter*2,smallDamageOuter*2,smallDamageOuter*2), entity -> true)) {

            if (Math.sqrt(entity.distanceToSqr(vec3)) <= radiusExplosionCenter) {
                entity.hurt(DamageSource.explosion((LivingEntity) player), damage);
            }else if (Math.sqrt(entity.distanceToSqr(vec3)) <= radiusMedium){
                entity.hurt(DamageSource.explosion((LivingEntity) player), damage);
            }else if (Math.sqrt(entity.distanceToSqr(vec3)) <= smallDamageRadius){
                entity.hurt(DamageSource.explosion((LivingEntity) player), damage*0.75f);
            }else if (Math.sqrt(entity.distanceToSqr(vec3)) <= smallDamageOuter){
                entity.hurt(DamageSource.explosion((LivingEntity) player), damage*0.25f);
            }
            if (fire) {
                entity.setSecondsOnFire(10);
            }


        }
        return EXPLOSION_CACHE.get(radius);




    }
}

