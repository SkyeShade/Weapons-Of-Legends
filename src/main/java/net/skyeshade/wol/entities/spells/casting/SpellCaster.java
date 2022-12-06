package net.skyeshade.wol.entities.spells.casting;

import net.minecraft.server.level.ServerPlayer;
import net.skyeshade.wol.entities.spells.casting.fire.CastFireBall;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.mana.ManaDataSyncS2CPacket;
import net.skyeshade.wol.stats.PlayerStatsProvider;
import net.skyeshade.wol.util.SpellBaseStatVariables;
import net.skyeshade.wol.util.StatSystems;

import java.util.concurrent.atomic.AtomicLong;

public class SpellCaster {

    //casts spells depending on spell id
    //
    //
    //
    public static void CastSpell (ServerPlayer player, long spellID) {
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

            long manaCost = SpellBaseStatVariables.getSpellBaseStats(spellID, 2) + SpellBaseStatVariables.getSpellManaCostIncrease(spellID, stats.getSpellPowerLevel()[(int) spellID]);

            if (StatSystems.parrallelCastingAllowedPerCoreLevel[(int)stats.getManaCoreLevel()-1] > stats.getCastingAmount()) {
                if (spellID == 1) {

                    if (stats.getMana() >= manaCost) {

                        stats.addMana(-manaCost);
                        StatSystems.xpSystem(-manaCost, player);
                        ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(stats.getMana()), player);

                        CastFireBall.castFireBall(player);

                    }
                }



            }
        });
    }
}
