package net.skyeshade.wol.util;

import net.minecraft.server.level.ServerPlayer;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.hp.HpDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.hp.MaxHpDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.mana.MaxManaDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.ManaCoreLevelDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.ManaCoreXpDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.MaxManaBarrierDataSyncS2CPacket;
import net.skyeshade.wol.stats.PlayerStatsProvider;

public class StatSystems {
    int requiredManaUsageForXp = 1;
    public long[] requiredCoreLevelXp = {
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

    public long[] maxHpRewardPerLevel = {
            100L,
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

    public long secondsForBaseHpRegen = 600;
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


}
