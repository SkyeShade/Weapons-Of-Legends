package net.skyeshade.wol.entities.spells.fireelement;

import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.skyeshade.wol.BaseBladeSlashProjectile;
import net.skyeshade.wol.entities.spells.fireelement.FireBall;
import net.skyeshade.wol.entities.BladeSlashProjectileEntity;

public class FireBall extends Projectile {
    private int life;
    protected boolean inGround;

    private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(FireBall.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> PIERCE_LEVEL = SynchedEntityData.defineId(FireBall.class, EntityDataSerializers.BYTE);

    public FireBall(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected FireBall(EntityType<? extends FireBall> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        this(pEntityType, pLevel);
        this.setPos(pX, pY, pZ);
    }




    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
        this.life = 0;
    }
    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        this.setPos(pX, pY, pZ);
        this.setRot(pYaw, pPitch);
    }
    @Override
    public void lerpMotion(double pX, double pY, double pZ) {
        super.lerpMotion(pX, pY, pZ);
        this.life = 0;
    }

    protected void tickDespawn() {
        ++this.life;
        if (this.life >= 200) {
            this.discard();
        }

    }

    @Override
    protected void defineSynchedData() {}


    public void move(MoverType pType, Vec3 pPos) {
        super.move(pType, pPos);
        //this.setDeltaMovement(0, +100*(vec3.y+1), 0);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);

        if (!this.level.getBlockState(pResult.getBlockPos()).is(Blocks.BEDROCK) && !this.level.getBlockState(pResult.getBlockPos()).is(Blocks.END_PORTAL_FRAME) && !this.level.getBlockState(pResult.getBlockPos()).is(Blocks.END_GATEWAY) && !this.level.getBlockState(pResult.getBlockPos()).is(Blocks.END_PORTAL))
            this.level.destroyBlock(pResult.getBlockPos(), true);
        this.discard();

    }

    float x = 1.0f;
    float y = 1.0f;
    float z = 1.0f;
    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            this.tickDespawn();
        }
        /*BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level.getBlockState(blockpos);
        if (!blockstate.isAir()) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level, blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3 vec31 = this.position();

                for(AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vec31)) {
                        this.inGround = true;

                        break;
                    }
                }
            }
        }*/
        Vec3 vec3 = new Vec3(1.0D, 1.0D, 1.0D);
        super.tick();

        //this.setDeltaMovement(this.getDeltaMovement().x*10+vec3.x, this.getDeltaMovement().y*10+vec3.y, this.getDeltaMovement().z*10+vec3.z);




        this.setPos(this.getX()+x, this.getY()+y,this.getZ()+z);

        if (x > 0)
            x = x-0.01f;
        y = y-0.02f;
        if (z > 0)
            z = z-0.01f;


        BlockPos blockPos = new BlockPos(this.getBlockX(),this.getBlockY(),this.getBlockZ());
        if (!this.getLevel().getBlockState(blockPos).isAir()) {
            System.out.println("e");

            this.getLevel().explode(this,this.getX(),this.getY(),this.getZ(),10.0f,true, Explosion.BlockInteraction.BREAK);
            this.discard();
        }
        //this.setNoGravity(true);
        //System.out.println("e");

    }



    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
