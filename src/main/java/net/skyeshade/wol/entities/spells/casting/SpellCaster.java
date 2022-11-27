package net.skyeshade.wol.entities.spells.casting;

import net.minecraft.server.level.ServerPlayer;
import net.skyeshade.wol.entities.spells.casting.fire.CastFireBall;

public class SpellCaster {

    public static void CastSpell (ServerPlayer player, long spellID) {
        if (spellID == 1) {
            CastFireBall.castFireBall(player);
        }
    }

}
