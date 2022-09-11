package net.skyeshade.wol.abilities;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.skyeshade.wol.entities.BladeSlashProjectileEntity;
import net.skyeshade.wol.entities.EntityInit;
import net.skyeshade.wol.item.custom.TimeStopSwordItem;
import net.skyeshade.wol.sound.ModSounds;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class TimeStopAbility {
    static int timer = 0;

    static int stopTimer = 0;


    static List<Entity> entities = new ArrayList<>();
    static List<Entity> entitiesNew = new ArrayList<>();


    static List<Entity> timeStoppingPlayer = new ArrayList<>();

    static List<UUID> timeStartMissedPlayers = new ArrayList<>();
    static List<Entity> bladeEntities = new ArrayList<>();
    static int radius1;
    static Player player;

    static Level level;
    static ArrayList<Vec3> positions = new ArrayList<Vec3>();
    static ArrayList<Vec3> deltaMovement = new ArrayList<Vec3>();
    static ArrayList<Float> xRot = new ArrayList<Float>();
    static ArrayList<Float> yRot = new ArrayList<Float>();
    static ArrayList<Float> yHeadRot = new ArrayList<Float>();




    static int slowValue = 1000;

    public void shootProjectile (LivingEntity entity, float speed) {

        if (timer > 1) {
            if (!entity.level.isClientSide) {
                if (timeStoppingPlayer.contains(entity)) {

                    BladeSlashProjectileEntity arrow = new BladeSlashProjectileEntity(EntityInit.BLADE_SLASH.get(), entity, entity.level);
                    arrow.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), -1.0F, 0.01f, 1.0F);
                    entity.level.addFreshEntity(arrow);

                    Vec3 arrowSpeed = new Vec3(arrow.getDeltaMovement().x * speed, arrow.getDeltaMovement().y * speed, arrow.getDeltaMovement().z * speed);


                    arrow.setNoGravity(true);

                    bladeEntities.add(arrow);


                    entities.add(arrow);
                    positions.add(entities.indexOf(arrow), arrow.position());
                    deltaMovement.add(entities.indexOf(arrow), arrowSpeed);
                    xRot.add(entities.indexOf(arrow), arrow.getXRot());
                    yRot.add(entities.indexOf(arrow), arrow.getYRot());
                    yHeadRot.add(entities.indexOf(arrow), arrow.getYHeadRot());

                    arrow.setDeltaMovement(arrow.getDeltaMovement().x / 10, arrow.getDeltaMovement().y / 10, arrow.getDeltaMovement().z / 10);
                }
            }
        }
    }


    @SubscribeEvent
    public static void onServerTick (TickEvent.ServerTickEvent event) {
        if (player != null) {
            if (!player.level.isClientSide()) {
                //adds new entities around the caster position
                if (timer > 1) {
                    entitiesNew = player.level.getEntities(player, AABB.ofSize(player.position(), radius1,radius1,radius1));
                    for (Entity entity : entitiesNew) {
                        for (Player player2 : level.players()) {
                            if (entity == player2) {
                                if (!timeStartMissedPlayers.contains(entity.getUUID()))
                                    timeStartMissedPlayers.add(entity.getUUID());
                            }
                        }
                        try {
                            if (!entities.contains(entity)) {
                                entities.add(entity);
                                positions.add(entities.indexOf(entity), entity.position());
                                deltaMovement.add(entities.indexOf(entity), entity.getDeltaMovement());
                                xRot.add(entities.indexOf(entity), entity.getXRot());
                                yRot.add(entities.indexOf(entity), entity.getYRot());
                                yHeadRot.add(entities.indexOf(entity), entity.getYHeadRot());
                            }
                        }catch (Exception e) {
                        System.out.println("Potential Warming: Could Not Add [All] Entity Attributes From List To Temporary Storage");
                        }
                    }
                    //sets entity stats every tick if the list of entities isnt empty (this is what freezes the entities)
                    if (!entities.isEmpty()) {
                        try {
                            for (Entity entity : entities) {
                                //checks if the entities is not one of the ones that stopped time
                                if (!timeStoppingPlayer.contains(entity)) {
                                    //checks if the entity is a player, if so then sets their positions using a network packet, this is needed to sync the clients with server
                                    for (Player player2 : level.players()) {
                                        if (entity == player2) {
                                            ServerPlayer playerIn = (ServerPlayer) player2;
                                            playerIn.connection.teleport(positions.get(entities.indexOf(entity)).x, positions.get(entities.indexOf(entity)).y, positions.get(entities.indexOf(entity)).z, yRot.get(entities.indexOf(entity)), xRot.get(entities.indexOf(entity)));

                                        }
                                    }
                                    //sets all entities positions on server side
                                    entity.setPos(positions.get(entities.indexOf(entity)));
                                    entity.setXRot(xRot.get(entities.indexOf(entity)));
                                    entity.setYRot(yRot.get(entities.indexOf(entity)));
                                    entity.setDeltaMovement(deltaMovement.get(entities.indexOf(entity)).x / slowValue, deltaMovement.get(entities.indexOf(entity)).y / slowValue, deltaMovement.get(entities.indexOf(entity)).z / slowValue);
                                    entity.setYHeadRot(yHeadRot.get(entities.indexOf(entity)));
                                    entity.resetFallDistance();
                                    entity.setNoGravity(true);
                                }
                            }
                        }catch (Exception e) {
                            System.out.println("Could Not Get Entities From List");
                        }

                    }
                    timer--;

                //on second to last tick, sets all entities original position and velocity (deltaMovement as of 1.18.2) on time freeze
                }else if (timer > 0) {

                    for (Entity entity : entities) {
                        //checks if the entities is not one of the ones that stopped time
                        if (!timeStoppingPlayer.contains(entity)) {
                            entity.setPos(positions.get(entities.indexOf(entity)));
                            entity.setDeltaMovement(deltaMovement.get(entities.indexOf(entity)));

                            //makes sure the blade entities dont have gravity
                            if (!bladeEntities.contains(entity)) {
                                entity.setNoGravity(false);
                            }
                            //removes uuids of players who have their gravity turned on again
                            if (timeStartMissedPlayers.contains(entity.getUUID())) {
                                if (entity.isNoGravity()) {
                                    timeStartMissedPlayers.remove(entity.getUUID());
                                }
                            }
                        }

                    }
                    //clears list of all players who have stopped time
                    timeStoppingPlayer.clear();

                    timer--;

                //on last tick clears all lists of data to get ready for next use
                }else if(timer == 0){
                    bladeEntities.clear();
                    entities.clear();
                    entitiesNew.clear();
                    positions.clear();
                    deltaMovement.clear();
                    xRot.clear();
                    yRot.clear();
                    yHeadRot.clear();
                    timeStoppingPlayer.clear();
                }
                //plays time resume on the last 2 seconds (and makes sure its only played once)
                if (timer < 40 && stopTimer == 1) {

                    level.playSound(null, player.blockPosition(), ModSounds.START_TIME.get(),SoundSource.PLAYERS, 10f,1f);

                    stopTimer = 0;
                }

                //makes sure players dont somehow miss getting unfrozen in time by whatever means they have to break my shitty code
                if (!timeStartMissedPlayers.isEmpty()) {
                    for (Player eplayer : level.players()) {
                        if (eplayer.isNoGravity()) {
                            if (timeStartMissedPlayers.contains(eplayer.getUUID())) {
                                eplayer.setNoGravity(false);
                                timeStartMissedPlayers.remove(eplayer.getUUID());
                            }
                        }
                    }
                }
            }
        }
    }

    //makes sure players get their gravity back if they logged in and out during time frozen
    @SubscribeEvent
    public static void onPlayerLoggedIn (PlayerEvent.PlayerLoggedInEvent event) {
        if (stopTimer == 0){
            if (timeStartMissedPlayers.contains(event.getPlayer().getUUID())) {
                event.getPlayer().setNoGravity(false);
                timeStartMissedPlayers.remove(event.getPlayer().getUUID());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut (PlayerEvent.PlayerLoggedOutEvent event) {
        if (stopTimer != 0){
            if (!timeStartMissedPlayers.contains(event.getPlayer().getUUID()))
                timeStartMissedPlayers.add(event.getPlayer().getUUID());
        }
    }

    //start function for stopping time
    public void stopTime (int radius, Player caster, int ticks, net.minecraft.world.level.Level plevel) {

        caster.setNoGravity(false);

        timeStoppingPlayer.add(caster);

        player = caster;

        level = plevel;
        radius1 = radius;

        stopTimer = 1;
        timer = ticks;
    }




}
