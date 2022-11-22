package net.skyeshade.wol.abilities;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.skyeshade.wol.entities.BladeSlashProjectileEntity;
import net.skyeshade.wol.entities.EntityInit;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.mana.ManaDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.ManaCoreDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.ManaCoreExhaustionDataSyncS2CPacket;
import net.skyeshade.wol.sound.ModSounds;
import net.skyeshade.wol.stats.PlayerStatsProvider;
import net.skyeshade.wol.util.StatSystems;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class TimeStopAbility {
    public static List<Integer> timer = new ArrayList<>();

    static List<Integer> stopTimer = new ArrayList<>();


    static List<List<Entity>> entities = new ArrayList<>();
    static List<List<Entity>> entitiesNew = new ArrayList<>();


    static List<Entity> timeStoppingPlayer = new ArrayList<>();

    static List<List<UUID>> timeStartMissedPlayers = new ArrayList<>();
    static List<List<Entity>> bladeEntities = new ArrayList<>();
    static int radius1;
    public static List<Player> playerList = new ArrayList<>();

    static List<Level> levelList = new ArrayList<>();
    static ArrayList<ArrayList<Vec3>> positions = new ArrayList<>();
    static ArrayList<ArrayList<Vec3>> deltaMovement = new ArrayList<>();
    static ArrayList<ArrayList<Float>> xRot = new ArrayList<>();
    static ArrayList<ArrayList<Float>> yRot = new ArrayList<>();
    static ArrayList<ArrayList<Float>> yHeadRot = new ArrayList<>();

    static StatSystems statSystems = new StatSystems();


    static double slowValue = 1000;

    public void shootProjectile (LivingEntity entity, float speed) {
        if (!playerList.isEmpty()) {
            if (!entity.level.isClientSide()) {


                int valuesIndex = playerList.indexOf(entity);
                try {
                    if (timer.get(valuesIndex) > 1) {
                        if (!entity.level.isClientSide) {
                            if (timeStoppingPlayer.contains(entity)) {

                                BladeSlashProjectileEntity arrow = new BladeSlashProjectileEntity(EntityInit.BLADE_SLASH.get(), entity, entity.level);
                                arrow.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), -1.0F, 0.01f, 1.0F);
                                entity.level.addFreshEntity(arrow);

                                Vec3 arrowSpeed = new Vec3(arrow.getDeltaMovement().x * speed, arrow.getDeltaMovement().y * speed, arrow.getDeltaMovement().z * speed);


                                arrow.setNoGravity(true);

                                if (!bladeEntities.isEmpty()) {
                                    if (!bladeEntities.get(valuesIndex).contains(arrow)) {
                                        bladeEntities.get(valuesIndex).add(arrow);
                                    }

                                }
                                if (!entities.isEmpty()) {
                                    entities.get(valuesIndex).add(arrow);
                                    positions.get(valuesIndex).add(entities.get(valuesIndex).indexOf(arrow), arrow.position());
                                    deltaMovement.get(valuesIndex).add(entities.get(valuesIndex).indexOf(arrow), arrowSpeed);
                                    xRot.get(valuesIndex).add(entities.get(valuesIndex).indexOf(arrow), arrow.getXRot());
                                    yRot.get(valuesIndex).add(entities.get(valuesIndex).indexOf(arrow), arrow.getYRot());
                                    yHeadRot.get(valuesIndex).add(entities.get(valuesIndex).indexOf(arrow), arrow.getYHeadRot());
                                }

                                arrow.setDeltaMovement(arrow.getDeltaMovement().x / 10, arrow.getDeltaMovement().y / 10, arrow.getDeltaMovement().z / 10);
                            }
                        }
                    }
                }catch (IndexOutOfBoundsException ignored) {

                }
            }
        }
    }


    @SubscribeEvent
    public static void onServerTick (TickEvent.ServerTickEvent event) {
        if (!playerList.isEmpty()) {
            //System.out.println(playerList);

                for (Player player : playerList) {

                    if (player != null) {


                        if (!player.level.isClientSide()) {
                            //adds new entities around the caster position
                            int valuesIndex = playerList.indexOf(player);
                            player.getCapability(PlayerStatsProvider.PLAYER_MANA).ifPresent(mana -> {
                                if (timer.get(valuesIndex) > 40) {
                                    if (mana.getMana() >= 100) {
                                        mana.addMana(-100);
                                        statSystems.xpSystem(-100, (ServerPlayer) player);
                                        ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana()), ((ServerPlayer) player));

                                        player.getCapability(PlayerStatsProvider.PLAYER_MANACORE).ifPresent(manacore -> {
                                            manacore.addManaCore(10);
                                            ModMessages.sendToPlayer(new ManaCoreDataSyncS2CPacket(manacore.getManaCore()), ((ServerPlayer) player));
                                        });
                                        player.getCapability(PlayerStatsProvider.PLAYER_MANACORE_EXHAUSTION).ifPresent(manacore_exhaustion -> {
                                            manacore_exhaustion.addManaCoreExhaustion(1);
                                            ModMessages.sendToPlayer(new ManaCoreExhaustionDataSyncS2CPacket(manacore_exhaustion.getManaCoreExhaustion()), ((ServerPlayer) player));
                                        });
                                        timer.set(valuesIndex, 45);




                                    }
                                }
                            });




                            if (timer.get(valuesIndex) > 1) {
                                if (entitiesNew.isEmpty()) {
                                    entitiesNew.add(player.level.getEntities(player, AABB.ofSize(player.position(), radius1, radius1, radius1)));

                                }else try {
                                    entitiesNew.set(valuesIndex, player.level.getEntities(player, AABB.ofSize(player.position(), radius1, radius1, radius1)));
                                }catch (IndexOutOfBoundsException e) {
                                    entitiesNew.add(player.level.getEntities(player, AABB.ofSize(player.position(), radius1, radius1, radius1)));
                                }
                                for (Entity entity : entitiesNew.get(valuesIndex)) {
                                    if (!timeStoppingPlayer.contains(entity)) {
                                        for (Player player2 : levelList.get(valuesIndex).players()) {
                                            if (entity == player2) {
                                                //-----------------------------------------------test later if this works
                                                try {
                                                    timeStartMissedPlayers.get(valuesIndex).set(valuesIndex, entity.getUUID());
                                                } catch (IndexOutOfBoundsException e) {
                                                    List<UUID> missedPlayersTemp = new ArrayList<>();
                                                    timeStartMissedPlayers.add(missedPlayersTemp);
                                                    timeStartMissedPlayers.get(valuesIndex).add(entity.getUUID());
                                                }

                                            }
                                        }


                                        try {
                                            if (!entities.get(valuesIndex).contains(entity)) {
                                                entities.get(valuesIndex).add(entity);
                                                //System.out.println(entities.get(valuesIndex).size());
                                                deltaMovement.get(valuesIndex).add(entities.get(valuesIndex).indexOf(entity), entity.getDeltaMovement());
                                                for (List<Entity> entityLists : entities) {
                                                    if (entityLists != entities.get(valuesIndex)) {
                                                        for (Entity otherTimeStoppedEntities : entityLists) {
                                                            if (entity == otherTimeStoppedEntities) {
                                                                deltaMovement.get(valuesIndex).set(entities.get(valuesIndex).indexOf(entity), deltaMovement.get(entities.indexOf(entityLists)).get(entityLists.indexOf(otherTimeStoppedEntities)));
                                                            }
                                                        }
                                                    }

                                                }

                                                positions.get(valuesIndex).add(entities.get(valuesIndex).indexOf(entity), entity.position());

                                                //System.out.println(positions.get(valuesIndex).size());
                                                //System.out.println("positions are added " + positions.get(valuesIndex));


                                                xRot.get(valuesIndex).add(entities.get(valuesIndex).indexOf(entity), entity.getXRot());
                                                yRot.get(valuesIndex).add(entities.get(valuesIndex).indexOf(entity), entity.getYRot());
                                                yHeadRot.get(valuesIndex).add(entities.get(valuesIndex).indexOf(entity), entity.getYHeadRot());
                                            }


                                        } catch (IndexOutOfBoundsException e) {
                                            List<Entity> newEntity = new ArrayList<>();
                                            //newEntity.add(entitiesNew.get(valuesIndex).indexOf(entity), entity);

                                            entities.add(newEntity);

                                            List<Entity> blades = new ArrayList<>();
                                            bladeEntities.add(blades);

                                            ArrayList<Vec3> newPositions = new ArrayList<>();
                                            //newPositions.add(entities.get(valuesIndex).indexOf(entity), entity.position());

                                            ArrayList<Vec3> newDeltaMovement = new ArrayList<>();


                                            ArrayList<Float> newXRot = new ArrayList<>();
                                            //newXRot.add(entities.get(valuesIndex).indexOf(entity), entity.getXRot());

                                            ArrayList<Float> newYRot = new ArrayList<>();
                                            //newYRot.add(entities.get(valuesIndex).indexOf(entity), entity.getYRot());

                                            ArrayList<Float> newYHead = new ArrayList<>();
                                            //newYHead.add(entities.get(valuesIndex).indexOf(entity), entity.getYHeadRot());

                                            positions.add(newPositions);
                                            //System.out.println("position test thing: " + positions);
                                            deltaMovement.add(newDeltaMovement);

                                            xRot.add(newXRot);

                                            yRot.add(newYRot);

                                            yHeadRot.add(newYHead);
                                        }
                                    }

                                }
                                //sets entity stats every tick if the list of entities isnt empty (this is what freezes the entities)
                                if (!entities.isEmpty()) {
                                    if (!entities.get(valuesIndex).isEmpty()) {
                                        //try {
                                            for (Entity entity : entities.get(valuesIndex)) {
                                                //checks if the entities is not one of the ones that stopped time
                                                if (!timeStoppingPlayer.contains(entity)) {
                                                    //checks if the entity is a player, if so then sets their positions using a network packet, this is needed to sync the clients with server
                                                    for (Player player2 : levelList.get(valuesIndex).players()) {
                                                        if (entity == player2) {
                                                            ServerPlayer playerIn = (ServerPlayer) player2;
                                                            playerIn.connection.teleport(positions.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)).x, positions.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)).y, positions.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)).z, yRot.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)), xRot.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)));

                                                        }
                                                    }
                                                    //sets all entities positions on server side
                                                    //System.out.println("sets positions and all");
                                                    entity.setPos(positions.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)));
                                                    entity.setXRot(xRot.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)));
                                                    entity.setYRot(yRot.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)));
                                                    entity.setDeltaMovement(deltaMovement.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)).x / slowValue, deltaMovement.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)).y / slowValue, deltaMovement.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)).z / slowValue);
                                                    entity.setYHeadRot(yHeadRot.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)));
                                                    entity.resetFallDistance();
                                                    entity.setNoGravity(true);
                                                }
                                            }
                                        //} catch (Exception e) {
                                        //    System.out.println("Could Not Get Entities From List");
                                        //}

                                    }
                                }
                                timer.set(valuesIndex, timer.get(valuesIndex) - 1);

                                //on second to last tick, sets all entities original position and velocity (deltaMovement as of 1.18.2) on time freeze
                            } else if (timer.get(valuesIndex) > 0) {
                                if (!entities.isEmpty()) {
                                    for (Entity entity : entities.get(valuesIndex)) {
                                        //checks if the entities is not one of the ones that stopped time
                                        if (!timeStoppingPlayer.contains(entity)) {
                                            entity.setPos(positions.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)));
                                            entity.setDeltaMovement(deltaMovement.get(valuesIndex).get(entities.get(valuesIndex).indexOf(entity)));

                                            //makes sure the blade entities dont have gravity
                                            if (!bladeEntities.isEmpty()) {
                                                if (!bladeEntities.get(valuesIndex).contains(entity)) {
                                                    entity.setNoGravity(false);
                                                }
                                            } else {
                                                entity.setNoGravity(false);
                                            }

                                            //removes uuids of players who have their gravity turned on again
                                            if (!timeStartMissedPlayers.isEmpty()) {
                                                if (timeStartMissedPlayers.get(valuesIndex).contains(entity.getUUID())) {
                                                    if (entity.isNoGravity()) {
                                                        timeStartMissedPlayers.get(valuesIndex).remove(entity.getUUID());
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }



                                timer.set(valuesIndex, timer.get(valuesIndex) - 1);

                                //on last tick clears all lists of data to get ready for next use
                            }


                            //plays time resume on the last 2 seconds (and makes sure its only played once)
                            if (timer.get(valuesIndex) < 40 && stopTimer.get(valuesIndex) == 1) {

                                levelList.get(valuesIndex).playSound(null, player.blockPosition(), ModSounds.START_TIME.get(), SoundSource.PLAYERS, 10f, 1f);

                                stopTimer.set(valuesIndex, 0);
                            }

                            //makes sure players dont somehow miss getting unfrozen in time by whatever means they have to break my shitty code
                            if (!timeStartMissedPlayers.isEmpty()) {
                                if (!timeStartMissedPlayers.get(valuesIndex).isEmpty()) {
                                    for (Player eplayer : levelList.get(valuesIndex).players()) {
                                        if (eplayer.isNoGravity()) {
                                            if (timeStartMissedPlayers.get(valuesIndex).contains(eplayer.getUUID())) {
                                                eplayer.setNoGravity(false);
                                                timeStartMissedPlayers.get(valuesIndex).remove(eplayer.getUUID());
                                            }
                                        }
                                    }
                                }
                            }


                            if (timer.get(valuesIndex) == 0) {
                                if (!entities.isEmpty()) {
                                    bladeEntities.remove(valuesIndex);
                                    entities.remove(valuesIndex);
                                    entitiesNew.remove(valuesIndex);
                                    positions.remove(valuesIndex);
                                    deltaMovement.remove(valuesIndex);
                                    xRot.remove(valuesIndex);
                                    yRot.remove(valuesIndex);
                                    yHeadRot.remove(valuesIndex);


                                }
                                timer.remove(valuesIndex);
                                stopTimer.remove(valuesIndex);
                                timeStoppingPlayer.remove(valuesIndex);
                                if (playerList.size() == 1) {
                                    playerList.set(valuesIndex, null);
                                }else {
                                    playerList.remove(player);
                                }

                                try {
                                    levelList.remove(valuesIndex);
                                } catch (Exception e) {
                                    System.out.println("Could not remove level from levelList");
                                }

                            }

                        }
                    }

                }


        }else {
            bladeEntities.clear();
            entities.clear();
            entitiesNew.clear();
            positions.clear();
            deltaMovement.clear();
            xRot.clear();
            yRot.clear();
            yHeadRot.clear();
            playerList.clear();
            timer.clear();
            stopTimer.clear();
        }
        if (timer.isEmpty()) {
            if (!timeStoppingPlayer.isEmpty()) {
                timeStoppingPlayer.clear();
            }
        }
    }

    //makes sure players get their gravity back if they logged in and out during time frozen
    @SubscribeEvent
    public static void onPlayerLoggedIn (PlayerEvent.PlayerLoggedInEvent event) {
        try {
            if (playerList.contains(event.getEntity())) {
                int valuesIndex = playerList.indexOf(event.getEntity());
                if (stopTimer.get(valuesIndex) == 0) {
                    if (timeStartMissedPlayers.get(valuesIndex).contains(event.getEntity().getUUID())) {
                        event.getEntity().setNoGravity(false);
                        timeStartMissedPlayers.get(valuesIndex).remove(event.getEntity().getUUID());
                    }
                }
            }
        }catch (Exception e) {
            System.out.println(e);
        }
    }


    @SubscribeEvent
    public static void onPlayerLoggedOut (PlayerEvent.PlayerLoggedOutEvent event) {
        try {
            if (playerList.contains(event.getEntity())) {
                int valuesIndex = playerList.indexOf(event.getEntity());
                if (stopTimer.get(valuesIndex) != 0) {
                    if (!timeStartMissedPlayers.get(valuesIndex).contains(event.getEntity().getUUID()))
                        timeStartMissedPlayers.get(valuesIndex).add(event.getEntity().getUUID());
                }
            }
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    //start function for stopping time
    public void stopTime (int radius, Player caster, int ticks, Level plevel) {
        if (!caster.level.isClientSide()) {
            caster.setNoGravity(false);
            if (!playerList.isEmpty()) {
                if (playerList.get(0) == null) {
                    playerList.set(0, caster);
                }else {
                    playerList.add(caster);
                }
            }else {
                playerList.add(caster);
            }



            int valuesIndex = playerList.indexOf(caster);
            timeStoppingPlayer.add(caster);


            levelList.add(valuesIndex, plevel);
            radius1 = radius;

            stopTimer.add(valuesIndex, 1);
            timer.add(valuesIndex, ticks);
        }
    }

}
