package net.skyeshade.wol.util;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.common.Mod;
import net.skyeshade.wol.client.ClientStatsData;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.affinities.aether.AetherAffinityDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.affinities.augmenting.AugEfficiencyDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.affinities.conjuring.ConEfficiencyDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.affinities.earth.EarthAffinityDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.affinities.fire.FireAffinityDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.affinities.water.WaterAffinityDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.affinities.wind.WindAffinityDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.hp.HpDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.hp.MaxHpDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.mana.MaxManaDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.ManaCoreLevelDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.ManaCoreXpDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.MaxManaBarrierDataSyncS2CPacket;
import net.skyeshade.wol.stats.PlayerStatsProvider;

public class StatSystems {

    //TODO: balance every stat


    public static int[] parrallelCastingAllowedPerCoreLevel = {
            1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4};
    public static long[] requiredCoreLevelXp = {
            100L,
            1000L,
            1000L,
            1000L,
            1000L,
            1000L,
            1000L,
            1000L,
            1000L,
            1000L,
            1000L,
            1000L,
            1000L,
            1000L
    };

    public static long[] maxManaRewardPerLevel = {
            1000L,
            2000L,
            3000L,
            4500L,
            9000L,
            13500L,
            20250L,
            40500L,
            60750L,
            91125L,
            182250L,
            273375L,
            410062L,
            2050312L
    };

    public static long[] maxManaBarrierRewardPerLevel = {
            100L,
            200L,
            300L,
            450L,
            900L,
            1350L,
            2025L,
            4050L,
            6075L,
            9112L,
            18225L,
            27337L,
            41006L,
            205031L
    };

    public static long[] maxHpRewardPerLevel = {
            20L,
            40L,
            80L,
            160L,
            320L,
            640L,
            1280L,
            5120L,
            10240L,
            20480L,
            40960L,
            81920L,
            163840L,
            327680L
    };
    public static long[] conAndAugEfficiencyRewardPerLevel = {
            2000L,
            40L,
            80L,
            160L,
            320L,
            640L,
            1280L,
            5120L,
            10240L,
            20480L,
            40960L,
            81920L,
            163840L,
            327680L
    };

    public static long secondsForBaseHpRegen = 600; //default 600
    public static long secondsForBaseManaRegen = 6; //default 600

    public static long secondsForBaseManaBarrierRegen = 60;

    public static long manaBarrierRegenCost = 1;

    public static long secondsForBaseManaBarrierRevive = 60;

    static int requiredManaUsageForXp = 1;

    /**
     * Deals with adding xp
     *
     * @param manaChange the amount of mana the spell uses (only accepts negative value)
     * @param player ServerPlayer since this is run on the server
     */

    public static void xpSystem (long manaChange, ServerPlayer player){
        //long startTime = System.currentTimeMillis();
        if (manaChange < 0) {
            long absoluteManaChange;
            absoluteManaChange = Math.abs(manaChange);
                //if mana change is over requiredManaUsageForXp, it calculates how much xp and possibly level you should get and how much should remain in the xp buffer
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                for (long i = absoluteManaChange+stats.getManaToXp(); i >= requiredManaUsageForXp; i = i-requiredManaUsageForXp) {
                    if (stats.getManaCoreXp()+1 >= requiredCoreLevelXp[(int)stats.getManaCoreLevel()-1]) {
                        if (stats.getManaCoreLevel() != requiredCoreLevelXp.length) {
                            stats.setManaCoreXp(0);
                            stats.addManaCoreLevel(1);

                            stats.addMaxMana(maxManaRewardPerLevel[(int)stats.getManaCoreLevel()-1]);
                            stats.addMaxManaBarrier(maxManaBarrierRewardPerLevel[(int)stats.getManaCoreLevel()-1]);
                            stats.addMaxHp(maxHpRewardPerLevel[(int)stats.getManaCoreLevel()-1]);

                            ModMessages.sendToPlayer(new MaxHpDataSyncS2CPacket(stats.getMaxHp()), player);
                            ModMessages.sendToPlayer(new MaxManaBarrierDataSyncS2CPacket(stats.getMaxManaBarrier()), player);
                            ModMessages.sendToPlayer(new MaxManaDataSyncS2CPacket(stats.getMaxMana()), player);

                            ModMessages.sendToPlayer(new ManaCoreLevelDataSyncS2CPacket(stats.getManaCoreLevel()), player);
                        }else {
                            stats.setManaCoreXp(requiredCoreLevelXp[requiredCoreLevelXp.length-1]);
                        }

                    } else {
                        stats.addManaCoreXp(1);
                    }
                    if (i-requiredManaUsageForXp < requiredManaUsageForXp) {
                        stats.setManaToXp(i-requiredManaUsageForXp);
                    }
                }
                if (absoluteManaChange < requiredManaUsageForXp) {
                    stats.addManaToXp(absoluteManaChange);
                }
                ModMessages.sendToPlayer(new ManaCoreXpDataSyncS2CPacket(stats.getManaCoreXp()), player);

            });
        }
        //long stopTime = System.currentTimeMillis();
        //System.out.println("xp calculation took: "+ (stopTime-startTime) + " ms");
    }

    /**
     * Deals with adding affinity after spell usage
     *
     * @param manaChange the amount of mana the spell uses (only accepts negative)
     * @param player ServerPlayer since this is run on the server
     * @param affinityID 0 = non elemental, 1 = fire, 2 = water, 3 = wind, 4 = earth, 5 = aether
     * @param augmenting false = conjuring, true = augmenting
     */
    public static void addAffinity (long manaChange, ServerPlayer player, int affinityID, boolean augmenting) {

        if (manaChange < 0) {
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                long absManaChange = Math.abs(manaChange);

                int procentManaChange = (int)Math.floor(((double)absManaChange/(double)stats.getMaxMana())*100D);
                //System.out.println(absManaChange);

                //System.out.println();
                if (affinityID == 1) {
                    stats.addFireAffinity(procentManaChange);
                    ModMessages.sendToPlayer(new FireAffinityDataSyncS2CPacket(stats.getFireAffinity()), player);
                }else if (affinityID == 2) {
                    stats.addWaterAffinity(procentManaChange);
                    ModMessages.sendToPlayer(new WaterAffinityDataSyncS2CPacket(stats.getWaterAffinity()), player);
                }else if (affinityID == 3) {
                    stats.addWindAffinity(procentManaChange);
                    ModMessages.sendToPlayer(new WindAffinityDataSyncS2CPacket(stats.getWindAffinity()), player);
                }else if (affinityID == 4) {
                    stats.addEarthAffinity(procentManaChange);
                    ModMessages.sendToPlayer(new EarthAffinityDataSyncS2CPacket(stats.getEarthAffinity()), player);
                }else if (affinityID == 5) {
                    stats.addAetherAffinity(procentManaChange);
                    ModMessages.sendToPlayer(new AetherAffinityDataSyncS2CPacket(stats.getAetherAffinity()), player);
                }

                if (augmenting) {
                    stats.addAugmentingEfficiency(procentManaChange);
                    ModMessages.sendToPlayer(new AugEfficiencyDataSyncS2CPacket(stats.getAugmentingEfficiency()), player);
                }else {
                    stats.addConjuringEfficiency(procentManaChange);
                    ModMessages.sendToPlayer(new ConEfficiencyDataSyncS2CPacket(stats.getConjuringEfficiency()), player);
                }
            });
        }
    }


    /**
     *
     * @param player ServerPlayer, set null if its on client
     * @param affinityID 0 = non elemental, 1 = fire, 2 = water, 3 = wind, 4 = earth, 5 = aether
     */
    public static long getAffinityFromID (int affinityID, ServerPlayer player) {
        if (player != null) {
            final long[] l = {0};
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                if (affinityID == 1) {
                    l[0] = stats.getFireAffinity();
                }else if (affinityID == 2){
                    l[0] = stats.getWaterAffinity();
                }else if (affinityID == 3){
                    l[0] = stats.getWindAffinity();
                } else if (affinityID == 4){
                    l[0] = stats.getEarthAffinity();
                } else if (affinityID == 5){
                    l[0] = stats.getAetherAffinity();
                }
            });
            return l[0];
        }
        return 0;
    }
    public static long getAffinityFromIDClient (int affinityID) {
        if (affinityID == 1) {
            return ClientStatsData.getPlayerFireAffinity();
        }else if (affinityID == 2){
            return ClientStatsData.getPlayerWaterAffinity();
        }else if (affinityID == 3){
            return ClientStatsData.getPlayerWindAffinity();
        } else if (affinityID == 4){
            return ClientStatsData.getPlayerEarthAffinity();
        } else if (affinityID == 5){
            return ClientStatsData.getPlayerAetherAffinity();
        }
        return 0;
    }



}
