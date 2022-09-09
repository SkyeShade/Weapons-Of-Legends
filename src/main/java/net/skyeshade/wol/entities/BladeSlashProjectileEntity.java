package net.skyeshade.wol.entities;

import com.mojang.math.Vector3f;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.core.particles.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkHooks;
import net.skyeshade.wol.client.render.BladeSlashRenderer;
import net.skyeshade.wol.particles.BladeSlashParticles;
import org.apache.commons.compress.compressors.lz77support.LZ77Compressor;

public class BladeSlashProjectileEntity extends AbstractArrow {
    LivingEntity attacker;


    Vec3 vec3;

    public BladeSlashProjectileEntity(EntityType<BladeSlashProjectileEntity> entityType, Level world) {
        super(entityType, world);

    }

    public BladeSlashProjectileEntity(EntityType<BladeSlashProjectileEntity> entityType, double x, double y, double z, Level world) {
        super(entityType, x, y, z, world);
    }

    public BladeSlashProjectileEntity(EntityType<BladeSlashProjectileEntity> entityType, LivingEntity shooter, Level world) {

        super(entityType, shooter, world);
        attacker = shooter;
        //if (this.distanceTo(shooter) > 10) {
        //    this.discard();
        //}
    }



    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {

        super.onHitEntity(pResult);

        this.setBaseDamage(5);

        this.setKnockback(10);

        pResult.getEntity().hurt(DamageSource.MAGIC, 5);
        this.setSoundEvent(SoundEvents.PLAYER_ATTACK_CRIT);
        this.discard();


    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        //this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1.0f, false, Explosion.BlockInteraction.BREAK);
        if (!this.level.getBlockState(pResult.getBlockPos()).is(Blocks.BEDROCK))
            this.level.destroyBlock(pResult.getBlockPos(), true);
        this.discard();

    }


    @Override
    protected void tickDespawn() {
        if (this.tickCount > 300){
            //this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1.0f, true, Explosion.BlockInteraction.BREAK);
            this.discard();
        }
    }

    @Override
    public void setSoundEvent(SoundEvent pSoundEvent) {
        super.setSoundEvent(pSoundEvent);
        this.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 1.0F, 1.2F);
    }

    BladeSlashParticles bladeSlashParticles = new BladeSlashParticles();
    @Override
    public void tick() {





            //System.out.println("ticking");
            //ParticleOptions particleOptions = ParticleTypes.HEART;

            //this.level.addAlwaysVisibleParticle(particleOptions,true, this.getX(), this.getY(), this.getZ(), 1, 1, 1);

            vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;
            for (int i = 0; i < 4; ++i){

                for (int ii = 0; ii < 10; ++ii) {
                    Vector3f vector3f = new Vector3f(0.6f,0f,0f);
                    this.level.addParticle(new DustParticleOptions(vector3f,1.0f),
                            this.getX() + d5 * (double) i / 4.0D - 1.25f + (2.5f /ii),
                            this.getY() + d6 * (double) i / 4.0D + 1.25f,
                            this.getZ() + d1 * (double) i / 4.0D,
                            -d5, -d6 + 0.2D, -d1);
                }

            }
            //bladeSlashParticles.bladeParticleEffect(this, 200);


        super.tick();
    }





    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
