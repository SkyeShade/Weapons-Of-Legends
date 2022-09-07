package net.skyeshade.wol.abilities;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;


public class TimeStopAbility {

    static int timer = 0;
    static List<Entity> entities;

    static Player player;


    static ArrayList<Vec3> positions = new ArrayList<Vec3>();
    static ArrayList<Vec3> deltaMovement = new ArrayList<Vec3>();
    static ArrayList<Float> xRot = new ArrayList<Float>();
    static ArrayList<Float> yRot = new ArrayList<Float>();
    static ArrayList<Float> yHeadRot = new ArrayList<Float>();


    @SubscribeEvent
    public static void onServerTick (TickEvent.ServerTickEvent event) {
        if (player != null) {
            if (!player.level.isClientSide) {


                if (timer > 1) {
                    for (Entity entity : entities) {


                        entity.setPos(positions.get(entities.indexOf(entity)));
                        entity.setXRot(xRot.get(entities.indexOf(entity)));
                        entity.setYRot(yRot.get(entities.indexOf(entity)));

                        entity.setYHeadRot(yHeadRot.get(entities.indexOf(entity)));

                        entity.resetFallDistance();

                        




                    }
                    System.out.println(timer);
                    timer--;
                }else if (timer > 0) {
                    for (Entity entity : entities) {

                        entity.setPos(positions.get(entities.indexOf(entity)));
                        entity.setDeltaMovement(deltaMovement.get(entities.indexOf(entity)));




                    }

                    timer--;
                }
            }
        }


    }

    public void stopTime (int radius, Player caster, int ticks) {

        player = caster;
        entities = caster.level.getEntities(caster, AABB.ofSize(caster.position(), radius,radius,radius));

        for (Entity entity : entities) {

            positions.add(entities.indexOf(entity), entity.position());
            deltaMovement.add(entities.indexOf(entity), entity.getDeltaMovement());
            xRot.add(entities.indexOf(entity), entity.getXRot());
            yRot.add(entities.indexOf(entity), entity.getYRot());

            yHeadRot.add(entities.indexOf(entity), entity.getYHeadRot());




        }


        timer = ticks;
        /*
        for (Entity entity : entities) {


            previousEntities = entities;

            if (previousEntities != null && previousEntities.contains(entity))
                entity.setPos(previousEntities.get(previousEntities.indexOf(entity)).position());
            entity.setDeltaMovement(0d,0d,0d);



            System.out.println(entity);
        }
        */

    }










}
