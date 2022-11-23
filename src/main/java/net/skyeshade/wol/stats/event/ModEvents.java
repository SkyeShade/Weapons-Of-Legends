package net.skyeshade.wol.stats.event;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.abilities.TimeStopAbility;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.destruction.DestructionActiveDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.mana.ManaDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.mana.MaxManaDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.*;
import net.skyeshade.wol.networking.packet.menutoggle.MenuStatTabToggleDataSyncS2CPacket;
import net.skyeshade.wol.stats.PlayerStats;
import net.skyeshade.wol.stats.PlayerStatsProvider;
import net.skyeshade.wol.util.StatSystems;

@Mod.EventBusSubscriber(modid = WOL.MOD_ID)
public class ModEvents {
    static StatSystems statSystems = new StatSystems();
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerStatsProvider.PLAYER_MANA).isPresent()) {
                event.addCapability(new ResourceLocation(WOL.MOD_ID, "properties"), new PlayerStatsProvider());
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MANA).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MANA).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MAXMANA).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MAXMANA).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            //manacore stuff
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MANABARRIER).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MANABARRIER).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MAXMANABARRIER).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MAXMANABARRIER).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MANABARRIERALIVE).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MANABARRIERALIVE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MANABARRIERREVIVE).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MANABARRIERREVIVE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_DESTRUCTION_ACTIVE).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_DESTRUCTION_ACTIVE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MANACORE_LEVEL).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MANACORE_LEVEL).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MANACORE_XP).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MANACORE_XP).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MANATOXP).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MANATOXP).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MENUSTATTABTOGGLE).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MENUSTATTABTOGGLE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });


            event.getOriginal().invalidateCaps();

        }
    }


    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {

        event.register(PlayerStats.class);

    }
    static int tickcount;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if(event.side.isServer() && event.phase == TickEvent.Phase.START) {
            //long startTime = System.currentTimeMillis();
            tickcount++;
            TimeStopAbility.timeStopTick();
            //System.out.println("did ticking "+tickcount);

            for (Player player : event.getServer().getPlayerList().getPlayers()) {




                player.getCapability(PlayerStatsProvider.PLAYER_MAXMANA).ifPresent(maxmana -> {
                    player.getCapability(PlayerStatsProvider.PLAYER_MANAREGENBUFFER).ifPresent(manaregenbuffer -> {
                        player.getCapability(PlayerStatsProvider.PLAYER_MANA).ifPresent(mana -> {
                            manaregenbuffer.addManaRegenBuffer(((float) maxmana.getMaxMana() / (float) statSystems.secondsForBaseManaRegen) / 20.0F);
                            if (manaregenbuffer.getManaRegenBuffer() >= 1.0f) {
                                mana.addMana((int) manaregenbuffer.getManaRegenBuffer());
                                manaregenbuffer.setManaRegenBuffer(manaregenbuffer.getManaRegenBuffer() - (int) manaregenbuffer.getManaRegenBuffer());
                                ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana()), ((ServerPlayer) player));
                            }
                            //System.out.println(mana.getMana());


                            if (mana.getMana() > 0) {
                                player.getCapability(PlayerStatsProvider.PLAYER_MAXMANABARRIER).ifPresent(maxmanabarrier -> {
                                    player.getCapability(PlayerStatsProvider.PLAYER_MANABARRIERREGENBUFFER).ifPresent(manabarrierregenbuffer -> {
                                        player.getCapability(PlayerStatsProvider.PLAYER_MANABARRIER).ifPresent(manabarrier -> {
                                            if (manabarrier.getManaBarrierAlive()) {
                                                if (manabarrier.getManaBarrier() != maxmanabarrier.getMaxManaBarrier()) {
                                                    manabarrierregenbuffer.addManaBarrierRegenBuffer(((float) maxmanabarrier.getMaxManaBarrier() / (float) statSystems.secondsForBaseManaBarrierRegen) / 20.0F);
                                                    if (manabarrierregenbuffer.getManaBarrierRegenBuffer() >= 1.0f) {
                                                        manabarrier.addManaBarrier((int) manabarrierregenbuffer.getManaBarrierRegenBuffer());
                                                        mana.addMana(-statSystems.manaBarrierRegenCost);
                                                        statSystems.xpSystem(-statSystems.manaBarrierRegenCost, (ServerPlayer) player);
                                                        manabarrierregenbuffer.setManaBarrierRegenBuffer(manabarrierregenbuffer.getManaBarrierRegenBuffer() - (int) manabarrierregenbuffer.getManaBarrierRegenBuffer());
                                                        ModMessages.sendToPlayer(new ManaBarrierDataSyncS2CPacket(manabarrier.getManaBarrier()), ((ServerPlayer) player));
                                                    }
                                                }
                                            }else if (manabarrier.getManaBarrierRevive() < statSystems.secondsForBaseManaBarrierRevive){
                                                if (tickcount % 20 == 0) {
                                                    manabarrier.addManaBarrierRevive(1);
                                                }
                                            } else if (manabarrier.getManaBarrierRevive() == statSystems.secondsForBaseManaBarrierRevive){
                                                manabarrier.setManaBarrierAlive(true);
                                                manabarrier.setManaBarrierRevive(0);
                                                ModMessages.sendToPlayer(new ManaBarrierAliveDataSyncS2CPacket(manabarrier.getManaBarrierAlive()), ((ServerPlayer)player));
                                            }
                                            //System.out.println(mana.getMana());
                                        });
                                    });
                                });
                            }
                            if (!mana.getManaBarrierAlive()) {
                                mana.setManaBarrier(0);
                                ModMessages.sendToPlayer(new ManaBarrierDataSyncS2CPacket(mana.getManaBarrier()), ((ServerPlayer)player));

                            }
                        });
                    });
                });

            }

            //long stopTime = System.currentTimeMillis();
            //System.out.println("xp calculation took: "+ (stopTime-startTime) + " ms");
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            StatSystems statSystems = new StatSystems();
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerStatsProvider.PLAYER_MANA).ifPresent(mana -> {
                    ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana()), player);
                });
                player.getCapability(PlayerStatsProvider.PLAYER_MAXMANA).ifPresent(max_mana -> {
                    ModMessages.sendToPlayer(new MaxManaDataSyncS2CPacket(max_mana.getMaxMana()), player);
                });


                player.getCapability(PlayerStatsProvider.PLAYER_MAXMANA).ifPresent(max_mana -> {
                    if (max_mana.getMaxMana() <= statSystems.maxManaRewardPerLevel[0]) {
                        max_mana.setMaxMana(statSystems.maxManaRewardPerLevel[0]);
                        ModMessages.sendToPlayer(new MaxManaDataSyncS2CPacket(max_mana.getMaxMana()), ((ServerPlayer) player));
                    }
                });
                //manacore stuff

                player.getCapability(PlayerStatsProvider.PLAYER_MANABARRIER).ifPresent(manabarrier -> {
                    ModMessages.sendToPlayer(new ManaBarrierDataSyncS2CPacket(manabarrier.getManaBarrier()), player);
                });
                player.getCapability(PlayerStatsProvider.PLAYER_MANABARRIERALIVE).ifPresent(manabarrieralive -> {
                    ModMessages.sendToPlayer(new ManaBarrierAliveDataSyncS2CPacket(manabarrieralive.getManaBarrierAlive()), player);
                });
                player.getCapability(PlayerStatsProvider.PLAYER_MAXMANABARRIER).ifPresent(max_manabarrier -> {
                    ModMessages.sendToPlayer(new MaxManaBarrierDataSyncS2CPacket(max_manabarrier.getMaxManaBarrier()), player);
                });
                player.getCapability(PlayerStatsProvider.PLAYER_MANABARRIERREVIVE).ifPresent(manabarrierrevive -> {
                    ModMessages.sendToPlayer(new ManaBarrierReviveDataSyncS2CPacket(manabarrierrevive.getManaBarrierRevive()), player);
                });

                player.getCapability(PlayerStatsProvider.PLAYER_MAXMANABARRIER).ifPresent(max_manabarrier -> {
                    if (max_manabarrier.getMaxManaBarrier() <= statSystems.maxManaBarrierRewardPerLevel[0]) {
                        max_manabarrier.setMaxManaBarrier(statSystems.maxManaBarrierRewardPerLevel[0]);
                        ModMessages.sendToPlayer(new MaxManaBarrierDataSyncS2CPacket(max_manabarrier.getMaxManaBarrier()), ((ServerPlayer) player));
                    }
                });
                player.getCapability(PlayerStatsProvider.PLAYER_DESTRUCTION_ACTIVE).ifPresent(destruction_active -> {
                    ModMessages.sendToPlayer(new DestructionActiveDataSyncS2CPacket(destruction_active.getDestructionActive()), player);
                });


                player.getCapability(PlayerStatsProvider.PLAYER_MENUSTATTABTOGGLE).ifPresent(menuStatTabToggle -> {
                    ModMessages.sendToPlayer(new MenuStatTabToggleDataSyncS2CPacket(menuStatTabToggle.getMenuStatTabToggle()), player);
                });
                player.getCapability(PlayerStatsProvider.PLAYER_MANACORE_LEVEL).ifPresent(manacore_level -> {
                    ModMessages.sendToPlayer(new ManaCoreLevelDataSyncS2CPacket(manacore_level.getManaCoreLevel()), player);
                });
                player.getCapability(PlayerStatsProvider.PLAYER_MANACORE_XP).ifPresent(manacore_xp -> {
                    ModMessages.sendToPlayer(new ManaCoreXpDataSyncS2CPacket(manacore_xp.getManaCoreXp()), player);
                });

                player.getCapability(PlayerStatsProvider.PLAYER_MANACORE_LEVEL).ifPresent(manacore_level -> {
                    if (manacore_level.getManaCoreLevel() < 1) {
                        manacore_level.setManaCoreLevel(1);
                        ModMessages.sendToPlayer(new ManaCoreLevelDataSyncS2CPacket(manacore_level.getManaCoreLevel()), ((ServerPlayer) player));
                    }
                });

            }
        }
    }
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(PlayerStatsProvider.PLAYER_MANABARRIER).ifPresent(manabarrier -> {
                if (manabarrier.getManaBarrierAlive()) {
                    float manaBarrierProcentage = (float)((manabarrier.getManaBarrier()*100) / manabarrier.getMaxManaBarrier());


                    /*float damageAfterReduction = (event.getAmount()/100) * manaBarrierProcentage;

                    event.setAmount(event.getAmount() - damageAfterReduction);

                    if (manabarrier.getManaBarrier() - (long) damageAfterReduction <= 0) {
                        manabarrier.setManaBarrierAlive(false);
                    } else {
                        manabarrier.addManaBarrier(-(long) damageAfterReduction);
                    }*/
                    if (manabarrier.getManaBarrier() - (long)event.getAmount() <= 0) {
                        manabarrier.setManaBarrierAlive(false);
                        ModMessages.sendToPlayer(new ManaBarrierAliveDataSyncS2CPacket(manabarrier.getManaBarrierAlive()), player);
                        manabarrier.setManaBarrier(0);
                    } else {
                        manabarrier.addManaBarrier(-(long)event.getAmount());
                    }
                    if (manaBarrierProcentage <= 50.0 && manaBarrierProcentage > 0) {

                        event.setAmount(0);
                    }else {
                        event.setAmount(0);
                    }
                }
                ModMessages.sendToPlayer(new ManaBarrierDataSyncS2CPacket(manabarrier.getManaBarrier()), player);
            });
        }
    }



}
