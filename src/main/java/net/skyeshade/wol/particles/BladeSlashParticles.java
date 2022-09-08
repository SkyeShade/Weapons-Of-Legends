package net.skyeshade.wol.particles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BladeSlashParticles {


    static Entity targetEntity;
    static int timer = 0;
    @SubscribeEvent
    public static void onServerTick (TickEvent.ServerTickEvent event) {

        if (timer > 1) {
            if(!targetEntity.level.isClientSide()){
                System.out.println("test");
                targetEntity.level.addAlwaysVisibleParticle(ParticleTypes.HEART,true,targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), 1, 1, 1);
            }




            timer--;
        }
    }



    public void bladeParticleEffect (Entity entity, int ticks) {

        targetEntity = entity;
        timer = ticks;
    }
}




