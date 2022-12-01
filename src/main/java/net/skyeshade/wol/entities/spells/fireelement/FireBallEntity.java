package net.skyeshade.wol.entities.spells.fireelement;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.skyeshade.wol.BaseBladeSlashProjectile;
import net.skyeshade.wol.entities.spells.BaseSpellProjectile;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.spellstatupdate.SpellPowerLevelDataSyncS2CPacket;
import net.skyeshade.wol.sound.ModSounds;
import net.skyeshade.wol.stats.PlayerStatsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class FireBallEntity extends BaseSpellProjectile {


    LivingEntity attacker;

    Vec3 currentDeltamovement;



    int ticks;
    public FireBallEntity(EntityType<FireBallEntity> entityType, Level world) {
        super(entityType, world);

    }

    public FireBallEntity(EntityType<FireBallEntity> entityType, double x, double y, double z, Level world) {
        super(entityType, x, y, z, world);
    }

    public FireBallEntity(EntityType<FireBallEntity> entityType, LivingEntity shooter, Level world) {

        super(entityType, shooter, world);
        attacker = shooter;

        this.playSound(SoundEvents.FIRECHARGE_USE,1.0F, 0.1F);
        //this.shouldRenderAtSqrDistance(100000);

       // this.shouldRenderAtSqrDistance(10000);

        //if (this.distanceTo(shooter) > 10) {
        //    this.discard();
        //}


        //currentDeltamovement = this.getDeltaMovement();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        double d0 = this.getBoundingBox().getSize() * 10.0D;
        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 *= 64.0D * getViewScale();
        return true;
    }



    protected void playSoundOnHit() {
        this.playSound(ModSounds.DISTANT_EXPLOSION.get(), 40.0F, 0.1F);
        //this.playSound(ModSounds.EXPLOSION.get(), 4.0F, 1.0F);
    }
    @Override
    protected void onHitEntity(EntityHitResult pResult) {

        super.onHitEntity(pResult);

        //this.setSoundEvent(SoundEvents.TRIDENT_HIT);
        playSoundOnHit();
        pResult.getEntity().hurt(DamageSource.MAGIC, 20);

        this.getLevel().explode(this, this.getX(),this.getY(),this.getZ(), 1+power/10, true, Explosion.BlockInteraction.BREAK);

        this.discard();


    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        //this.playSound(SoundEvents.FIRECHARGE_USE,1.0F, 0.1F);
        playSoundOnHit();

        if (!this.level.getBlockState(pResult.getBlockPos()).is(Blocks.BEDROCK) && !this.level.getBlockState(pResult.getBlockPos()).is(Blocks.END_PORTAL_FRAME) && !this.level.getBlockState(pResult.getBlockPos()).is(Blocks.END_GATEWAY) && !this.level.getBlockState(pResult.getBlockPos()).is(Blocks.END_PORTAL))
            this.level.destroyBlock(pResult.getBlockPos(), true);

        this.getLevel().explode(this, this.getX(),this.getY(),this.getZ(), 1+power/10, true, Explosion.BlockInteraction.BREAK);
        this.discard();

    }




    @Override
    protected void tickDespawn() {
        if (this.tickCount > 300){
            //this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1.0f, true, Explosion.BlockInteraction.BREAK);
            this.discard();
        }
    }





    int randomNum = ThreadLocalRandom.current().nextInt(0, 360);
    @Override
    public void tick() {

        //power = powerFromPlayer;

        ticks++;
        if (this.isInWater()) {
            if (currentDeltamovement == null) {
                currentDeltamovement = this.getDeltaMovement();
            }else {
                this.setDeltaMovement(currentDeltamovement.x * 300.0F, currentDeltamovement.y * 300.0F, currentDeltamovement.z * 300.0F);
            }
        }
        this.playSound(SoundEvents.FIRE_AMBIENT, 1.0F, 2F);


        for (float i = 0; i < 4; i++) {
            int rX = ThreadLocalRandom.current().nextInt(-1, 2);
            int rY = ThreadLocalRandom.current().nextInt(-1, 2);
            int rZ = ThreadLocalRandom.current().nextInt(-1, 2);
            this.level.addAlwaysVisibleParticle(ParticleTypes.FLAME, this.getX()+rX *0.25D, this.getY()+rY*0.25D , this.getZ()+rZ*0.25D, (this.getDeltaMovement().x+rX)/20, (this.getDeltaMovement().y+rY)/20, (this.getDeltaMovement().z+rZ)/20);


        }

        super.tick();
    }





    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
