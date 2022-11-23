package net.skyeshade.wol.util;

import net.minecraft.server.level.ServerPlayer;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.mana.MaxManaDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.ManaCoreLevelDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.ManaCoreXpDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.MaxManaBarrierDataSyncS2CPacket;
import net.skyeshade.wol.stats.PlayerStatsProvider;

public class StatSystems {
    int requiredManaUsageForXp = 100;
    public long[] requiredCoreLevelXp = {
            100L,
            1000L,
            10000L,
            100000L,
            1000000L,
            10000000L,
            100000000L,
            1000000000L,
            10000000000L,
            100000000000L,
            1000000000000L,
            10000000000000L,
            100000000000000L,
            1000000000000000L
    };

    public long[] maxManaRewardPerLevel = {
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

    public long[] maxManaBarrierRewardPerLevel = {
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
    public long secondsForBaseManaRegen = 600;

    public long secondsForBaseManaBarrierRegen = 60;

    public long manaBarrierRegenCost = 1;

    public long secondsForBaseManaBarrierRevive = 60;

    long absoluteManaChange;
    public void xpSystem (long manaChange, ServerPlayer player){
        //long startTime = System.currentTimeMillis();
        if (manaChange < 0) {

            absoluteManaChange = Math.abs(manaChange);
                //if mana change is over requiredManaUsageForXp, it calculates how much xp and possibly level you should get and how much should remain in the xp buffer
            player.getCapability(PlayerStatsProvider.PLAYER_MANATOXP).ifPresent(manatoxp -> {
                player.getCapability(PlayerStatsProvider.PLAYER_MANACORE_XP).ifPresent(manacorexp -> {
                    player.getCapability(PlayerStatsProvider.PLAYER_MANACORE_LEVEL).ifPresent(manacore_level -> {

                        for (long i = absoluteManaChange+manatoxp.getManaToXp(); i >= requiredManaUsageForXp; i = i-requiredManaUsageForXp) {

                            if (manacorexp.getManaCoreXp()+1 >= requiredCoreLevelXp[(int)manacore_level.getManaCoreLevel()-1]) {


                                if (manacore_level.getManaCoreLevel() != requiredCoreLevelXp.length) {
                                    manacorexp.setManaCoreXp(0);
                                    manacore_level.addManaCoreLevel(1);
                                    player.getCapability(PlayerStatsProvider.PLAYER_MAXMANA).ifPresent(maxmana -> {
                                        maxmana.addMaxMana(maxManaRewardPerLevel[(int)manacore_level.getManaCoreLevel()-1]);
                                        maxmana.addMaxManaBarrier(maxManaBarrierRewardPerLevel[(int)manacore_level.getManaCoreLevel()-1]);
                                        ModMessages.sendToPlayer(new MaxManaBarrierDataSyncS2CPacket(maxmana.getMaxManaBarrier()), player);
                                        ModMessages.sendToPlayer(new MaxManaDataSyncS2CPacket(maxmana.getMaxMana()), player);
                                    });
                                    ModMessages.sendToPlayer(new ManaCoreLevelDataSyncS2CPacket(manacore_level.getManaCoreLevel()), player);
                                }else {
                                    manacorexp.setManaCoreXp(requiredCoreLevelXp[requiredCoreLevelXp.length-1]);
                                }

                            } else {
                                manacorexp.addManaCoreXp(1);
                            }

                            if (i-requiredManaUsageForXp < requiredManaUsageForXp) {
                                manatoxp.setManaToXp(i-requiredManaUsageForXp);
                            }
                        }
                        if (absoluteManaChange < requiredManaUsageForXp) {
                            manatoxp.addManaToXp(absoluteManaChange);
                        }
                        ModMessages.sendToPlayer(new ManaCoreXpDataSyncS2CPacket(manacorexp.getManaCoreXp()), player);

                    });

                });

            });
        }
        //long stopTime = System.currentTimeMillis();
        //System.out.println("xp calculation took: "+ (stopTime-startTime) + " ms");
    }
}
