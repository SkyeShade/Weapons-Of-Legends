package net.skyeshade.wol.util;

public class SpellBaseStatVariables {
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
                    2, //base Damage
                    20, //base Mana Cost
                    50, //base Casting time in ticks
                    25, //base Cooldown in ticks

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
                    250, //base Cooldown in ticks

            };
            return statArray[index];
        }

        return 0;
    }
}
