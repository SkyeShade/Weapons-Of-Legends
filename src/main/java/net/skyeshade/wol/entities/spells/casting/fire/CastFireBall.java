package net.skyeshade.wol.entities.spells.casting.fire;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.skyeshade.wol.entities.EntityInit;
import net.skyeshade.wol.entities.spells.fireelement.FireBallEntity;

public class CastFireBall {

    //Casts fireball
    //
    //
    //

    public static void castFireBall (ServerPlayer player) {
        FireBallEntity fireball = new FireBallEntity(EntityInit.FIREBALL.get(), player,player.level);

        double spawnX = player.getX()+2*player.getLookAngle().x;
        double spawnY = player.getY()+2*player.getLookAngle().y;
        double spawnZ = player.getZ()+2*player.getLookAngle().z;
        fireball.setPos(spawnX,spawnY+1.5f,spawnZ);
        float f = -Mth.sin(player.getYRot() * ((float)Math.PI / 180F)) * Mth.cos(player.getXRot() * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((player.getXRot() + -1.0F) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(player.getYRot() * ((float)Math.PI / 180F)) * Mth.cos(player.getXRot() * ((float)Math.PI / 180F));

        fireball.shoot((double)f, (double)f1, (double)f2, 5.01f, 0.1F);
        player.level.addFreshEntity(fireball);
    }
}
