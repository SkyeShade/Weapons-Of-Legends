package net.skyeshade.wol.abilities;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.skyeshade.wol.entities.BladeSlashProjectileEntity;
import net.skyeshade.wol.entities.EntityInit;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;


public class TimeStopAbility {
    static int timer = 0;
    static List<Entity> entities;
    static List<Entity> entitiesNew;
    static int radius1;
    static Player player;
    static ArrayList<Vec3> positions = new ArrayList<Vec3>();
    static ArrayList<Vec3> deltaMovement = new ArrayList<Vec3>();
    static ArrayList<Float> xRot = new ArrayList<Float>();
    static ArrayList<Float> yRot = new ArrayList<Float>();
    static ArrayList<Float> yHeadRot = new ArrayList<Float>();
    static int bladeSlashNumber = 0;



    static int slowValue = 1000;

    public void shootProjectile (LivingEntity entity, float speed) {

        if (timer > 1) {
            bladeSlashNumber++;

            BladeSlashProjectileEntity arrow = new BladeSlashProjectileEntity(EntityInit.BLADE_SLASH.get(), entity, entity.level);
            arrow.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 0.01f, 1.0F);
            entity.level.addFreshEntity(arrow);

            Vec3 arrowSpeed = new Vec3(arrow.getDeltaMovement().x*speed, arrow.getDeltaMovement().y*speed, arrow.getDeltaMovement().z*speed);
            entities.add(arrow);
            positions.add(entities.indexOf(arrow), arrow.position());
            deltaMovement.add(entities.indexOf(arrow), arrowSpeed);
            xRot.add(entities.indexOf(arrow), arrow.getXRot());
            yRot.add(entities.indexOf(arrow), arrow.getYRot());
            yHeadRot.add(entities.indexOf(arrow), arrow.getYHeadRot());

            arrow.setDeltaMovement(arrow.getDeltaMovement().x/10,arrow.getDeltaMovement().y/10,arrow.getDeltaMovement().z/10);

        }
    }


    @SubscribeEvent
    public static void onServerTick (TickEvent.ServerTickEvent event) {
        if (player != null) {
            if (!player.level.isClientSide()) {
                if (timer > 1) {
                    entitiesNew = player.level.getEntities(player, AABB.ofSize(player.position(), radius1,radius1,radius1));
                    for (Entity entity : entitiesNew) {
                        if (!entities.contains(entity)) {
                            entities.add(entity);
                            positions.add(entities.indexOf(entity), entity.position());
                            deltaMovement.add(entities.indexOf(entity), entity.getDeltaMovement());
                            xRot.add(entities.indexOf(entity), entity.getXRot());
                            yRot.add(entities.indexOf(entity), entity.getYRot());
                            yHeadRot.add(entities.indexOf(entity), entity.getYHeadRot());
                        }
                    }
                    for (Entity entity : entities) {

                        entity.setPos(positions.get(entities.indexOf(entity)));
                        entity.setXRot(xRot.get(entities.indexOf(entity)));
                        entity.setYRot(yRot.get(entities.indexOf(entity)));


                        entity.setDeltaMovement(deltaMovement.get(entities.indexOf(entity)).x/slowValue,deltaMovement.get(entities.indexOf(entity)).y/slowValue,deltaMovement.get(entities.indexOf(entity)).z/slowValue);


                        entity.setYHeadRot(yHeadRot.get(entities.indexOf(entity)));
                        entity.resetFallDistance();
                        entity.setNoGravity(true);






                    }
                    System.out.println(timer);
                    timer--;
                }else if (timer > 0) {
                    for (Entity entity : entities) {
                        entity.setPos(positions.get(entities.indexOf(entity)));
                        entity.setDeltaMovement(deltaMovement.get(entities.indexOf(entity)));
                        entity.setNoGravity(false);



                    }
                    player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP,1.0F, 1.2F);
                    bladeSlashNumber--;
                    if (bladeSlashNumber < 1){
                        timer--;
                    }
                    entities.clear();
                    entitiesNew.clear();

                }
            }
        }
    }

    public void stopTime (int radius, Player caster, int ticks) {
        player = caster;



        entities = caster.level.getEntities(caster, AABB.ofSize(caster.position(), radius,radius,radius));
        radius1 = radius;
        for (Entity entity : entities) {
            positions.add(entities.indexOf(entity), entity.position());
            deltaMovement.add(entities.indexOf(entity), entity.getDeltaMovement());
            xRot.add(entities.indexOf(entity), entity.getXRot());
            yRot.add(entities.indexOf(entity), entity.getYRot());
            try {
                yHeadRot.add(entities.indexOf(entity), entity.getYHeadRot());
            }catch (Exception e) {
                System.out.println("Could Not Rotate yHeadRot");
            }

        }
        timer = ticks;
    }




}
