package net.skyeshade.wol.entities;

import com.mojang.math.Vector3f;
import net.minecraft.core.particles.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

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
        shooter.playSound(SoundEvents.PLAYER_ATTACK_SWEEP,1.0F, 0.2F);
        //if (this.distanceTo(shooter) > 10) {
        //    this.discard();
        //}
        this.playSound(SoundEvents.TRIDENT_THROW, 1.0F, 0.1F);
    }



    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {

        super.onHitEntity(pResult);



        pResult.getEntity().hurt(DamageSource.MAGIC, 10);
        this.playSound(SoundEvents.TRIDENT_HIT, 10.0F, 0.1F);


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
    public void setSoundEvent(@NotNull SoundEvent pSoundEvent) {

        this.playSound(SoundEvents.TRIDENT_HIT, 10.0F, 0.1F);
        super.setSoundEvent(pSoundEvent);
    }


    int randomNum = ThreadLocalRandom.current().nextInt(0, 360);
    @Override
    public void tick() {



            this.setPierceLevel((byte) 10);

            //System.out.println("ticking");
            //ParticleOptions particleOptions = ParticleTypes.HEART;

            //this.level.addAlwaysVisibleParticle(particleOptions,true, this.getX(), this.getY(), this.getZ(), 1, 1, 1);


            double dx = this.getXRot();
            double dy = this.getYRot();

            //System.out.println("Rotx: " +dx);

            //System.out.println("Roty: " +dy);

            Vector3f vector3f = new Vector3f(0.6f,0f,0f);





                for (float ii = 0.0f; ii < 4.0; ii= ii+0.4f) {

                    this.level.addParticle(new DustParticleOptions(vector3f,2.0f),
                            this.getX()+((this.getX()-1-this.getX())*Math.sin((dy+180)*Math.PI/180) - ((this.getZ()+ii-2.0f)-this.getZ())*Math.cos((dy+180)*Math.PI/180)),
                            this.getY()+((this.getX()-1-this.getX())*Math.sin((randomNum)*Math.PI/180) - ((this.getZ()+ii-2.0f)-this.getZ())*Math.cos((randomNum)*Math.PI/180)),
                            this.getZ()+((this.getX()-1-this.getX())*Math.cos((dy+180)*Math.PI/180) + ((this.getZ()+ii-2.0f)-this.getZ())*Math.sin((dy+180)*Math.PI/180)),
                            -this.getDeltaMovement().x*10, -this.getDeltaMovement().y*10, -this.getDeltaMovement().z*10);

                    //System.out.println(this.getX()+(this.getX()+1.25f/ii-this.getX())*Math.cos((dy+180)/Math.PI) - (this.getZ()-this.getZ())*Math.sin((dy+180)/Math.PI));
                }



            //bladeSlashParticles.bladeParticleEffect(this, 200);


        super.tick();
    }





    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
