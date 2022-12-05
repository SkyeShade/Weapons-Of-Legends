package net.skyeshade.wol.entities.spells.casting.fire;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.skyeshade.wol.entities.EntityInit;
import net.skyeshade.wol.entities.spells.fireelement.FireBallEntity;
import net.skyeshade.wol.util.SpellBaseStatVariables;

public class CastFireBall {

    //Casts fireball
    //
    //
    //



    public static void castFireBall (ServerPlayer player) {
        FireBallEntity fireball = new FireBallEntity(EntityInit.FIREBALL.get(), player,player.level,1);

        /*double spawnX = player.getX()+2*player.getLookAngle().x;
        double spawnY = player.getY()+2*player.getLookAngle().y;
        double spawnZ = player.getZ()+2*player.getLookAngle().z;*/
        double dx = player.getXRot();
        double dy = player.getYRot();

        double sin = Math.sin(dy * Math.PI / 180);

        double cos = Math.cos(dy * Math.PI / 180);
        double x = player.getX()-cos - sin;
        double y = player.getY()-Math.sin(dx * Math.PI / 180);
        double z = player.getZ()-sin + cos;




        fireball.setPos(x,y+1.5f,z);




        player.level.addFreshEntity(fireball);
        fireball.setNoGravity(true);
        fireball.setDeltaMovement(0,0,0);





    }


    public static void castingTiming (FireBallEntity fireBallEntity, Player player, int timer) {

        //TODO: make the fireball move in a proper sphere around the caster


        double dx = player.getXRot();
        double dy = player.getYRot();

        double sin = Math.sin(dy * Math.PI / 180);

        double cos = Math.cos(dy * Math.PI / 180);
        double x = player.getX()-cos - sin;
        double y = player.getY()-Math.sin(dx * Math.PI / 180);
        double z = player.getZ()-sin + cos;




        if (timer < SpellBaseStatVariables.getSpellBaseStats(1,3)) {









            //fireBallEntity.setPos(x,y+1.5f,z);

            fireBallEntity.setPos(x,y+1.5f,z);
            fireBallEntity.moveTo(x,y+1.5f,z);
            fireBallEntity.syncPacketPositionCodec(x,y+1.5f,z);
            fireBallEntity.setPosRaw(x,y+1.5f,z);

            fireBallEntity.setDeltaMovement(player.getDeltaMovement());
            fireBallEntity.lerpTo(x,y+1.5f,z,fireBallEntity.getRotationVector().x,fireBallEntity.getRotationVector().y,0,false);
            fireBallEntity.lerpMotion(player.getDeltaMovement().x,player.getDeltaMovement().y,player.getDeltaMovement().z);

        }else if (timer == SpellBaseStatVariables.getSpellBaseStats(1,3)) {
            double spawnX = player.getX()+2*player.getLookAngle().x;
            double spawnY = player.getY()+2*player.getLookAngle().y;
            double spawnZ = player.getZ()+2*player.getLookAngle().z;

            fireBallEntity.moveTo(spawnX,spawnY+1.5f,spawnZ);
            fireBallEntity.setDeltaMovement(0,0,0);

            float f = -Mth.sin(player.getYRot() * ((float)Math.PI / 180F)) * Mth.cos(player.getXRot() * ((float)Math.PI / 180F));
            float f1 = -Mth.sin((player.getXRot() + -1.0F) * ((float)Math.PI / 180F));
            float f2 = Mth.cos(player.getYRot() * ((float)Math.PI / 180F)) * Mth.cos(player.getXRot() * ((float)Math.PI / 180F));

            fireBallEntity.shoot((double)f, (double)f1, (double)f2, 2.01f, 0.1F);

            fireBallEntity.setNoGravity(false);

        }

    }
}
