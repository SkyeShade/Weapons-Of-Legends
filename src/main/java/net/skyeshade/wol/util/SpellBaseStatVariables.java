package net.skyeshade.wol.util;

public class SpellBaseStatVariables {

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
