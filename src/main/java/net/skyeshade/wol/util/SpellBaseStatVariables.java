package net.skyeshade.wol.util;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.skyeshade.wol.client.ClientStatsData;
import net.skyeshade.wol.stats.PlayerStatsProvider;

public class SpellBaseStatVariables {
    //TODO: balance spell stats
    //these values are used to innitiate arrays in PlayerStats and ClientStatsData
    // row length is number of spells, add one per spell id, this is literally only so it doesnt get confusing af
    //the first value is always -1 since spellid 0 doesnt exist, no matter what happens index 0 should not be reached
    public static long[] spellPowerLevel = {-1,0,0};
    //also add another number per spell id (+1)
    public static long[] spellRange = {-1,0,0};
    //also add one for each spell id
    //byte instead of boolean, 0 = false 1 = true
    public static byte[] spellPassiveToggle = {-1,0,0};

    public static long getSpellBaseStats (long spellID, int index) {
        if (spellID == 1) {
            //fireBall
            long[] statArray= {
                    100, //unlock cost
                    5, //base Damage
                    20, //base Mana Cost
                    50, //base Casting time in ticks

            };
            return statArray[index];
        }
        if (spellID == 2) {
            //test
            long[] statArray= {
                    1000, //unlock cost
                    9000000000000000000L, //base Damage
                    200000000, //base Mana Cost
                    500, //base Casting time in ticks

            };
            return statArray[index];
        }

        return 0;
    }



    public static long getSpellDamageIncrease (long spellID, long powerLevel) {
        long baseDamage = getSpellBaseStats(spellID, 1);
        if (spellID == 1) {
            return (long)(baseDamage*Math.pow(1.05f, powerLevel));
        }
        if (spellID == 2) {
            return baseDamage+baseDamage*(long)(powerLevel*0.5f);
        }
        return 0;
    }


    public static long getSpellManaCost (long spellID, long powerLevel, long affinity, long augmentation, long conjuring) {
        long baseManaCost = getSpellBaseStats(spellID, 2);
        if (spellID == 1) {

            return getAbseluteMana(powerLevel,baseManaCost,affinity, 0, conjuring ,1.05f, 1.05f);
            //return 1;
        }
        if (spellID == 2) {

            return baseManaCost+baseManaCost*(long)(powerLevel*0.5f);
        }
        return 0;
    }

    public static long getSpellCastingTime (long spellID, long powerLevel, long affinity, long coreLevel) {


        return 0;
    }


    /**
     *
     * @param powerLevel powerlevel
     * @param baseManaCost baseManaCost
     * @param affinity affinity value
     * @param augmenting augmenting value (put 0 if spell is a conjuring spell)
     * @param conjuring conjuring value (put 0 if spell is a augmenting spell)
     * @param manaChangeRate rate at which mana should increase/decrease
     * @return returns absolute mana consumption
     */
    public static long getAbseluteMana (long powerLevel, long baseManaCost, long affinity, long augmenting, long conjuring, double manaChangeRate, double affinityEffectiveness) {
        double affinityCostReduction = affinity;
        long abseluteManaCost = (long)(baseManaCost*Math.pow(manaChangeRate, powerLevel));


        return abseluteManaCost;
    }


    /**
     *
     * @param powerLevel powerlevel
     * @param baseCastingTime base casting time
     * @param affinityID 0 = non elemental, 1 = fire, 2 = water, 3 = wind, 4 = earth, 5 = aether
     * @param augmenting false = conjuring, true = augmenting
     * @param manaChangeRate rate at which mana should increase/decrease
     * @return returns absolute casting time
     */
    public static long getAbseluteCastingTime (long powerLevel, long baseCastingTime, int affinityID, boolean augmenting, double manaChangeRate) {
        return (long)(baseCastingTime*Math.pow(manaChangeRate, powerLevel));
    }


    public static int getAffinityIDFromSpellID (long spellID) {
        //0 = non elemental, 1 = fire, 2 = water, 3 = wind, 4 = earth, 5 = aether

        if (spellID == 1){
            return 1;
        }else if (spellID == 2){
            return 0;
        }else if (spellID == 3){

        }else if (spellID == 4){

        }else if (spellID == 5){

        }
        return 0;

    }




}
