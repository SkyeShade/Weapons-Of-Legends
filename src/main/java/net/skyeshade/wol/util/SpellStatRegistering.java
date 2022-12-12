package net.skyeshade.wol.util;

import java.util.ArrayList;

public class SpellStatRegistering {
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
    public static ArrayList<BaseSpellStatController> spellStatControllers = new ArrayList<>();
    public static void registerSpells() {
        BaseSpellStatController fireBallStatController = new BaseSpellStatController(
                1,
                100,
                5,
                20,
                50,
                1,
                2,
                20,
                20,
                1.02,
                false,
                true
        );
        BaseSpellStatController testSpellStatController = new BaseSpellStatController(
                2,
                100,
                50000000000L,
                200000000,
                50,
                1,
                1.05f,
                1.05f,
                1.05f,
                2.05f,
                false,
                true
        );

    }
    public static BaseSpellStatController getSpellController (long spellID) {
        for (BaseSpellStatController spellStatController : spellStatControllers)
            if (spellStatController.getSpellID() == spellID)
                return spellStatController;
        return null;
    }
}
