package net.skyeshade.wol.stats.event;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.abilities.TimeStopAbility;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.destruction.DestructionActiveDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.hp.HpDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.hp.MaxHpDataSyncS2CPacket;
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
            if(!event.getObject().getCapability(PlayerStatsProvider.PLAYER_STATS).isPresent()) {
                event.addCapability(new ResourceLocation(WOL.MOD_ID, "properties"), new PlayerStatsProvider());
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(newStore -> {
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




                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    stats.addManaRegenBuffer(((float) stats.getMaxMana() / (float) statSystems.secondsForBaseManaRegen) / 20.0F);
                    if (stats.getManaRegenBuffer() >= 1.0f) {
                        stats.addMana((int) stats.getManaRegenBuffer());
                        stats.setManaRegenBuffer(stats.getManaRegenBuffer() - (int) stats.getManaRegenBuffer());
                        ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(stats.getMana()), ((ServerPlayer) player));
                    }

                    stats.addHpRegenBuffer(((float) stats.getMaxHp() / (float) statSystems.secondsForBaseHpRegen) / 20.0F);
                    if (stats.getHpRegenBuffer() >= 1.0f) {
                        stats.addHp((int) stats.getHpRegenBuffer());
                        stats.setHpRegenBuffer(stats.getHpRegenBuffer() - (int) stats.getHpRegenBuffer());
                        ModMessages.sendToPlayer(new HpDataSyncS2CPacket(stats.getHp()), ((ServerPlayer) player));
                    }
                    //System.out.println(mana.getMana());


                    if (stats.getMana() > 0) {

                        if (stats.getManaBarrierAlive()) {
                            if (stats.getManaBarrier() != stats.getMaxManaBarrier()) {
                                stats.addManaBarrierRegenBuffer(((float) stats.getMaxManaBarrier() / (float) statSystems.secondsForBaseManaBarrierRegen) / 20.0F);
                                if (stats.getManaBarrierRegenBuffer() >= 1.0f) {
                                    stats.addManaBarrier((int) stats.getManaBarrierRegenBuffer());
                                    stats.addMana(-statSystems.manaBarrierRegenCost);
                                    statSystems.xpSystem(-statSystems.manaBarrierRegenCost, (ServerPlayer) player);
                                    stats.setManaBarrierRegenBuffer(stats.getManaBarrierRegenBuffer() - (int) stats.getManaBarrierRegenBuffer());
                                    ModMessages.sendToPlayer(new ManaBarrierDataSyncS2CPacket(stats.getManaBarrier()), ((ServerPlayer) player));
                                }
                            }
                        }else if (stats.getManaBarrierRevive() < statSystems.secondsForBaseManaBarrierRevive){
                            if (tickcount % 20 == 0) {
                                stats.addManaBarrierRevive(1);
                            }
                        } else if (stats.getManaBarrierRevive() == statSystems.secondsForBaseManaBarrierRevive){
                            stats.setManaBarrierAlive(true);
                            stats.setManaBarrierRevive(0);
                            ModMessages.sendToPlayer(new ManaBarrierAliveDataSyncS2CPacket(stats.getManaBarrierAlive()), ((ServerPlayer)player));
                        }
                        //System.out.println(mana.getMana());

                    }
                    if (!stats.getManaBarrierAlive()) {
                        stats.setManaBarrier(0);
                        ModMessages.sendToPlayer(new ManaBarrierDataSyncS2CPacket(stats.getManaBarrier()), ((ServerPlayer)player));

                    }
                    System.out.println("test hp 1: "+ player.getHealth());
                    float hpProcentage = (float)(stats.getHp()*100)/stats.getMaxHp();
                    System.out.println("procentage: "+ hpProcentage);
                    player.setHealth((float)Math.ceil(((20.0f/100)*hpProcentage)));
                    System.out.println("test custo  hp: "+ stats.getHp());
                    ModMessages.sendToPlayer(new HpDataSyncS2CPacket(stats.getHp()), ((ServerPlayer)player));
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

                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(stats.getMana()), player);
                    ModMessages.sendToPlayer(new MaxManaDataSyncS2CPacket(stats.getMaxMana()), player);

                    ModMessages.sendToPlayer(new HpDataSyncS2CPacket(stats.getHp()), player);
                    ModMessages.sendToPlayer(new MaxHpDataSyncS2CPacket(stats.getMaxHp()), player);

                    ModMessages.sendToPlayer(new ManaBarrierDataSyncS2CPacket(stats.getManaBarrier()), player);
                    ModMessages.sendToPlayer(new ManaBarrierAliveDataSyncS2CPacket(stats.getManaBarrierAlive()), player);
                    ModMessages.sendToPlayer(new MaxManaBarrierDataSyncS2CPacket(stats.getMaxManaBarrier()), player);
                    ModMessages.sendToPlayer(new ManaBarrierReviveDataSyncS2CPacket(stats.getManaBarrierRevive()), player);
                    ModMessages.sendToPlayer(new DestructionActiveDataSyncS2CPacket(stats.getDestructionActive()), player);
                    ModMessages.sendToPlayer(new MenuStatTabToggleDataSyncS2CPacket(stats.getMenuStatTabToggle()), player);
                    ModMessages.sendToPlayer(new ManaCoreLevelDataSyncS2CPacket(stats.getManaCoreLevel()), player);
                    ModMessages.sendToPlayer(new ManaCoreXpDataSyncS2CPacket(stats.getManaCoreXp()), player);



                    if (stats.getMaxMana() <= statSystems.maxManaRewardPerLevel[0]) {
                        stats.setMaxMana(statSystems.maxManaRewardPerLevel[0]);
                        ModMessages.sendToPlayer(new MaxManaDataSyncS2CPacket(stats.getMaxMana()), ((ServerPlayer) player));
                    }
                    if (stats.getMaxHp() <= statSystems.maxManaRewardPerLevel[0]) {
                        stats.setMaxHp(statSystems.maxManaRewardPerLevel[0]);
                        ModMessages.sendToPlayer(new MaxHpDataSyncS2CPacket(stats.getMaxHp()), ((ServerPlayer) player));
                    }
                    if (stats.getMaxManaBarrier() <= statSystems.maxManaBarrierRewardPerLevel[0]) {
                        stats.setMaxManaBarrier(statSystems.maxManaBarrierRewardPerLevel[0]);
                        ModMessages.sendToPlayer(new MaxManaBarrierDataSyncS2CPacket(stats.getMaxManaBarrier()), ((ServerPlayer) player));
                    }
                    if (stats.getManaCoreLevel() < 1) {
                        stats.setManaCoreLevel(1);
                        ModMessages.sendToPlayer(new ManaCoreLevelDataSyncS2CPacket(stats.getManaCoreLevel()), ((ServerPlayer) player));
                    }

                });

            }
        }
    }
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                if (stats.getManaBarrierAlive()) {
                    float manaBarrierProcentage = (float)((stats.getManaBarrier()*100) / stats.getMaxManaBarrier());


                    /*float damageAfterReduction = (event.getAmount()/100) * manaBarrierProcentage;

                    event.setAmount(event.getAmount() - damageAfterReduction);

                    if (manabarrier.getManaBarrier() - (long) damageAfterReduction <= 0) {
                        manabarrier.setManaBarrierAlive(false);
                    } else {
                        manabarrier.addManaBarrier(-(long) damageAfterReduction);
                    }*/
                    if (stats.getManaBarrier() - (long)event.getAmount() <= 0) {
                        stats.setManaBarrierAlive(false);
                        ModMessages.sendToPlayer(new ManaBarrierAliveDataSyncS2CPacket(stats.getManaBarrierAlive()), player);
                        stats.setManaBarrier(0);
                    } else {
                        stats.addManaBarrier(-(long)event.getAmount());
                    }
                    if (manaBarrierProcentage <= 50.0 && manaBarrierProcentage > 0) {


                    }else {

                    }
                }else if (!stats.getManaBarrierAlive()) {
                    if (stats.getHp() - (int)event.getAmount() <= 0) {
                        stats.setHp(0);
                        event.getEntity().kill();
                    } else {
                        stats.addHp(-(long)event.getAmount());

                    }



                }
                ModMessages.sendToPlayer(new HpDataSyncS2CPacket(stats.getHp()), player);
                ModMessages.sendToPlayer(new ManaBarrierDataSyncS2CPacket(stats.getManaBarrier()), player);
            });
            event.setAmount(0);
        }

    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                stats.setHp((stats.getMaxHp()/100)*20);
                ModMessages.sendToPlayer(new HpDataSyncS2CPacket(stats.getHp()), player);
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerRegen(LivingHealEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                stats.addHp((long)event.getAmount());
                ModMessages.sendToPlayer(new HpDataSyncS2CPacket(stats.getHp()), player);
            });
            event.setCanceled(true);
        }
    }

}
