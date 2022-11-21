package net.skyeshade.wol.stats.event;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.destruction.DestructionActiveDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.mana.ManaDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.mana.MaxManaDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.*;
import net.skyeshade.wol.networking.packet.menutoggle.MenuStatTabToggleDataSyncS2CPacket;
import net.skyeshade.wol.stats.PlayerStats;
import net.skyeshade.wol.stats.PlayerStatsProvider;

@Mod.EventBusSubscriber(modid = WOL.MOD_ID)
public class ModEvents {

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
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MANACORE).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MANACORE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MAXMANACORE).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MAXMANACORE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_MANACORE_EXHAUSTION).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_MANACORE_EXHAUSTION).ifPresent(newStore -> {
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
        if(event.side == LogicalSide.SERVER) {
            tickcount++;

            for (Player player : event.getServer().getPlayerList().getPlayers()) {
                if (tickcount % 20 == 0) {
                    player.getCapability(PlayerStatsProvider.PLAYER_MANACORE_EXHAUSTION).ifPresent(manacore_exhaustion -> {
                        manacore_exhaustion.subManaCoreExhaustion(1);
                        ModMessages.sendToPlayer(new ManaCoreExhaustionDataSyncS2CPacket(manacore_exhaustion.getManaCoreExhaustion()), ((ServerPlayer) player));

                    });
                }
                if (tickcount % 1 == 0) {

                    player.getCapability(PlayerStatsProvider.PLAYER_MANA).ifPresent(mana -> {
                        mana.addMana(1);
                        ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana()), ((ServerPlayer) player));
                        //System.out.println(mana.getMana());
                    });



                    player.getCapability(PlayerStatsProvider.PLAYER_MANACORE).ifPresent(manacore -> {
                        manacore.subManaCore(1);
                        ModMessages.sendToPlayer(new ManaCoreDataSyncS2CPacket(manacore.getManaCore()), ((ServerPlayer) player));

                    });


                    player.getCapability(PlayerStatsProvider.PLAYER_MANACORE_XP).ifPresent(manacore_xp -> {
                        manacore_xp.addManaCoreXp(20);
                        ModMessages.sendToPlayer(new ManaCoreXpDataSyncS2CPacket(manacore_xp.getManaCoreXp()), ((ServerPlayer) player));

                    });
                }

            }


        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerStatsProvider.PLAYER_MANA).ifPresent(mana -> {
                    ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana()), player);
                });
                player.getCapability(PlayerStatsProvider.PLAYER_MAXMANA).ifPresent(max_mana -> {
                    ModMessages.sendToPlayer(new MaxManaDataSyncS2CPacket(max_mana.getMaxMana()), player);
                });


                player.getCapability(PlayerStatsProvider.PLAYER_MAXMANA).ifPresent(max_mana -> {
                    if (max_mana.getMaxMana() <= 100) {
                        max_mana.setMaxMana(100);
                        ModMessages.sendToPlayer(new MaxManaDataSyncS2CPacket(max_mana.getMaxMana()), ((ServerPlayer) player));
                    }
                });
                //manacore stuff

                player.getCapability(PlayerStatsProvider.PLAYER_MANACORE).ifPresent(manacore -> {
                    ModMessages.sendToPlayer(new ManaCoreDataSyncS2CPacket(manacore.getManaCore()), player);
                });
                player.getCapability(PlayerStatsProvider.PLAYER_MAXMANACORE).ifPresent(max_manacore -> {
                    ModMessages.sendToPlayer(new MaxManaCoreDataSyncS2CPacket(max_manacore.getMaxManaCore()), player);
                });
                player.getCapability(PlayerStatsProvider.PLAYER_MANACORE_EXHAUSTION).ifPresent(manacore_exhaustion -> {
                    ModMessages.sendToPlayer(new ManaCoreExhaustionDataSyncS2CPacket(manacore_exhaustion.getManaCoreExhaustion()), player);
                });

                player.getCapability(PlayerStatsProvider.PLAYER_MAXMANACORE).ifPresent(max_manacore -> {
                    if (max_manacore.getMaxManaCore() <= 1000) {
                        max_manacore.setMaxManaCore(1000);
                        ModMessages.sendToPlayer(new MaxManaCoreDataSyncS2CPacket(max_manacore.getMaxManaCore()), ((ServerPlayer) player));
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


}
