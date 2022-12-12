package net.skyeshade.wol.client;

import net.skyeshade.wol.util.SpellStatRegistering;

public class ClientStatsData {

    private static long playerFireAffinity;
    private static long playerWaterAffinity;
    private static long playerWindAffinity;
    private static long playerEarthAffinity;
    private static long playerAetherAffinity;
    private static long playerAugmentingEfficiency;
    private static long playerConjuringEfficiency;

    private static int playerCastingAmount;
    private static long playerMana;
    private static long playerMaxMana;

    private static long playerHp;
    private static long playerMaxHp;
    private static long playerManaBarrier;
    private static long playerManaBarrierRevive;
    private static long playerMaxManaBarrier;

    private static long playerManaCoreLevel;
    private static long playerManaCoreXp;

    private static long[] playerSpellSlots = {0,0,0,0,0,0,0,0,0};

    private static long[] playerSpellPowerLevel = SpellStatRegistering.spellPowerLevel;

    private static long[] playerSpellRange = SpellStatRegistering.spellRange;

    private static byte[] playerSpellPassiveToggle = SpellStatRegistering.spellPassiveToggle;


    private static boolean playerDestructionActive;

    private static boolean playerManaBarrierAlive;

    private static boolean playerMenuStatTabToggle;

    private static boolean playerSpellSlotsToggle;


    public static void setPlayerFireAffinity(long fireAffinity) {ClientStatsData.playerFireAffinity = fireAffinity;}
    public static void setPlayerWaterAffinity(long waterAffinity) {ClientStatsData.playerWaterAffinity = waterAffinity;}
    public static void setPlayerWindAffinity(long windAffinity) {ClientStatsData.playerWindAffinity = windAffinity;}
    public static void setPlayerEarthAffinity(long earthAffinity) {ClientStatsData.playerEarthAffinity = earthAffinity;}
    public static void setPlayerAetherAffinity(long aetherAffinity) {ClientStatsData.playerAetherAffinity = aetherAffinity;}
    public static void setPlayerAugmentingEfficiency(long augmentingEfficiency) {ClientStatsData.playerAugmentingEfficiency = augmentingEfficiency;}
    public static void setPlayerConjuringEfficiency(long conjuringEfficiency) {ClientStatsData.playerConjuringEfficiency = conjuringEfficiency;}
    public static void setPlayerCastingAmount(int castingAmount) {ClientStatsData.playerCastingAmount = castingAmount;}
    public static void setMana(long mana) {
        ClientStatsData.playerMana = mana;
    }
    public static void setMaxMana(long max_mana) {
        ClientStatsData.playerMaxMana = max_mana;
    }
    public static void setHp(long hp) {
        ClientStatsData.playerHp = hp;
    }
    public static void setMaxHp(long max_hp) {
        ClientStatsData.playerMaxHp = max_hp;
    }
    public static long getPlayerMana() {
        return playerMana;
    }
    public static long getPlayerMaxMana() {
        return playerMaxMana;
    }
    public static long getPlayerHp() {
        return playerHp;
    }
    public static long getPlayerMaxHp() {
        return playerMaxHp;
    }
    public static long[] getPlayerSpellSlots() {return playerSpellSlots;}
    public static long[] getPlayerSpellPowerLevel() {return playerSpellPowerLevel;}
    public static long[] getPlayerSpellRange() {return playerSpellRange;}
    public static byte[] getPlayerSpellPassiveToggle() {return playerSpellPassiveToggle;}

    public static void setManaBarrier(long manabarrier) {
        ClientStatsData.playerManaBarrier = manabarrier;
    }
    public static void setMaxManaBarrier(long max_manabarrier) {ClientStatsData.playerMaxManaBarrier = max_manabarrier;}
    public static void setManaBarrierRevive(long manabarrierrevive) {ClientStatsData.playerManaBarrierRevive = manabarrierrevive;}

    public static void setPlayerSpellSlots(long[] spellSlots) {ClientStatsData.playerSpellSlots = spellSlots;}

    public static void setPlayerSpellPowerLevel(long[] spellPowerLevel) {ClientStatsData.playerSpellPowerLevel = spellPowerLevel;}
    public static void setPlayerSpellRange(long[] spellRange) {ClientStatsData.playerSpellRange = spellRange;}
    public static void setPlayerSpellPassiveToggle(byte[] spellPassiveToggle) {ClientStatsData.playerSpellPassiveToggle = spellPassiveToggle;}

    public static void setDestructionActive(boolean destructionActive) {ClientStatsData.playerDestructionActive = destructionActive;}

    public static void setManaBarrierAlive(boolean manaBarrierAlive) {ClientStatsData.playerManaBarrierAlive = manaBarrierAlive;}

    public static void setMenuStatTabToggle(boolean menuStatTabToggle) {ClientStatsData.playerMenuStatTabToggle = menuStatTabToggle;}

    public static void setSpellSlotsToggle(boolean spellSlotsToggle) {ClientStatsData.playerSpellSlotsToggle = spellSlotsToggle;}

    public static void setManaCoreLevel(long manacore_level) {
        ClientStatsData.playerManaCoreLevel = manacore_level;
    }

    public static void setManaCoreXp(long manacore_xp) {
        ClientStatsData.playerManaCoreXp = manacore_xp;
    }




    public static long getPlayerFireAffinity() {return playerFireAffinity;}
    public static long getPlayerWaterAffinity() {return playerWaterAffinity;}
    public static long getPlayerWindAffinity() {return playerWindAffinity;}
    public static long getPlayerEarthAffinity() {return playerEarthAffinity;}
    public static long getPlayerAetherAffinity() {return playerAetherAffinity;}
    public static long getPlayerAugmentingEfficiency() {return playerAugmentingEfficiency;}
    public static long getPlayerConjuringEfficiency() {return playerConjuringEfficiency;}


    public static int getPlayerCastingAmount() {return playerCastingAmount;}
    public static long getPlayerManaBarrier() {
        return playerManaBarrier;
    }
    public static long getPlayerMaxManaBarrier() {
        return playerMaxManaBarrier;
    }
    public static long getPlayerManaBarrierRevive() {
        return playerManaBarrierRevive;
    }

    public static boolean getPlayerDestructionActive() {
        return playerDestructionActive;
    }

    public static boolean getPlayerManaBarrierAlive() {
        return playerManaBarrierAlive;
    }

    public static boolean getPlayerMenuStatTabToggle() {return playerMenuStatTabToggle;}

    public static boolean getPlayerSpellSlotsToggle() {return playerSpellSlotsToggle;}

    public static long getPlayerManaCoreLevel() {
        return playerManaCoreLevel;
    }

    public static long getPlayerManaCoreXp() {
        return playerManaCoreXp;
    }



    ///////
}
