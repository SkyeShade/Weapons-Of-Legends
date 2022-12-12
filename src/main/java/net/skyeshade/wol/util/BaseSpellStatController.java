package net.skyeshade.wol.util;

import net.minecraft.server.level.ServerPlayer;
import net.skyeshade.wol.client.ClientStatsData;
import net.skyeshade.wol.stats.PlayerStatsProvider;

public class BaseSpellStatController {

    final long baseUnlockCost;
    final long baseDamage;
    final long baseManaCost;
    final long baseCastingTime;
    final int affinityID;
    final double damageChangeRate;
    final double manaChangeRate;
    final double castingTimeChangeRate;
    final double affinityEffectiveness;

    final boolean augmenting;
    final boolean conjuring;
    final long spellID;



    public BaseSpellStatController(long spellID, long baseUnlockCost, long baseDamage, long baseManaCost, long baseCastingTime, int affinityID, double damageChangeRate, double manaChangeRate, double castingTimeChangeRate, double affinityEffectiveness, boolean augmenting, boolean conjuring) {

        this.spellID = spellID;
        this.baseUnlockCost = baseUnlockCost;
        this.baseDamage = baseDamage;
        this.baseManaCost = baseManaCost;
        this.baseCastingTime = baseCastingTime;
        this.affinityID = affinityID;
        this.damageChangeRate = damageChangeRate;
        this.manaChangeRate = manaChangeRate;
        this.castingTimeChangeRate = castingTimeChangeRate;
        this.affinityEffectiveness = affinityEffectiveness;
        this.augmenting = augmenting;
        this.conjuring = conjuring;

        SpellStatRegistering.spellStatControllers.add(this);



    }


    /**
     *
     * @param player ServerPlayer
     * @return returns absoluteDamage on server side
     */
    public long getAbsoluteDamage(ServerPlayer player) {
        long affinityValue = StatSystems.getAffinityFromID(affinityID, player);

        long powerLevel = getPowerLevelFromSpellID(spellID, player);
        return (long)(this.baseDamage+damageChangeRate*powerLevel);
    }
    /**
     *
     * @return absoluteDamage on client
     */
    public long getAbsoluteDamage(){
        long affinityValue = StatSystems.getAffinityFromIDClient(affinityID);

        long powerLevel = ClientStatsData.getPlayerSpellPowerLevel()[(int)spellID];
        return (long)(this.baseDamage+damageChangeRate*powerLevel);
    }

    /**
     *
     * @param player ServerPlayer
     * @return returns absoluteManaCost on server side
     */
    public long getAbsoluteManaCost(ServerPlayer player) {
        long affinityValue = StatSystems.getAffinityFromID(affinityID, player)/100;

        long powerLevel = getPowerLevelFromSpellID(spellID, player);


        long conAugValue = 0;

        if (conjuring)
            conAugValue = getConAugServerSide(true, player) + conAugValue;
        if (augmenting)
            conAugValue = getConAugServerSide(false, player) + conAugValue;


        return Math.max((long)(this.baseManaCost+manaChangeRate*powerLevel-(affinityValue*affinityEffectiveness)-conAugValue*affinityEffectiveness), 0);
    }
    /**
     *
     * @return absoluteManaCost on client
     */
    public long getAbsoluteManaCost(){
        long affinityValue = StatSystems.getAffinityFromIDClient(affinityID)/100;

        long powerLevel = ClientStatsData.getPlayerSpellPowerLevel()[(int)spellID];
        long conAugValue = 0;
        if (conjuring)
            conAugValue = ClientStatsData.getPlayerConjuringEfficiency() + conAugValue;
        if (augmenting)
            conAugValue = ClientStatsData.getPlayerAugmentingEfficiency() + conAugValue;



        return Math.max((long)(this.baseManaCost+manaChangeRate*powerLevel-(affinityValue*affinityEffectiveness)-conAugValue*affinityEffectiveness), 0);
    }




    /**
     *
     * @param player ServerPlayer
     * @return returns absoluteCastingTime on server side
     */
    public long getAbsoluteCastingTime(ServerPlayer player) {
        long affinityValue = StatSystems.getAffinityFromID(affinityID, player)/100;

        long powerLevel = getPowerLevelFromSpellID(spellID, player);
        long conAugValue = 0;

        if (conjuring)
            conAugValue = getConAugServerSide(true, player) + conAugValue;
        if (augmenting)
            conAugValue = getConAugServerSide(false, player) + conAugValue;


        return Math.max((long)(this.baseCastingTime+castingTimeChangeRate*powerLevel-(affinityValue*affinityEffectiveness)-conAugValue*affinityEffectiveness), 0);
    }
    /**
     *
     * @return absoluteCastingTime on client
     */
    public long getAbsoluteCastingTime(){
        long affinityValue = StatSystems.getAffinityFromIDClient(affinityID)/100;

        long powerLevel = ClientStatsData.getPlayerSpellPowerLevel()[(int)spellID];

        long conAugValue = 0;
        if (conjuring)
            conAugValue = ClientStatsData.getPlayerConjuringEfficiency() + conAugValue;
        if (augmenting)
            conAugValue = ClientStatsData.getPlayerAugmentingEfficiency() + conAugValue;


        return Math.max((long)(this.baseCastingTime+castingTimeChangeRate*powerLevel-(affinityValue*affinityEffectiveness)-conAugValue*affinityEffectiveness), 0);
    }

    public long getBaseUnlockCost(){
        return baseUnlockCost;
    }
    public long getBaseCastingTime(){
        return baseCastingTime;
    }
    public long getBaseDamage() {
        return baseDamage;
    }
    public long getBaseManaCost () {
        return baseManaCost;
    }

    public long getSpellID (){
        return this.spellID;
    }

    private long getPowerLevelFromSpellID (long spellID, ServerPlayer player) {
        final long[] l = {0};
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            l[0] = stats.getSpellPowerLevel()[(int)spellID];
        });
        return l[0];
    }
    /**
     * @param conAug true = conjuring, false = augmenting
     * @return returns the value on the player
     * */
    private long getConAugServerSide (boolean conAug, ServerPlayer player) {
        final long[] l = {0};
        if (conAug){

            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                l[0] = stats.getConjuringEfficiency();
            });

        }else {
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                l[0] = stats.getAugmentingEfficiency();
            });
        }
        return l[0];
    }
}
