package net.skyeshade.wol.client;

public class ClientStatsData {
    private static long playerMana;
    private static long playerMaxMana;
    private static long playerManaCore;
    private static long playerManaCoreExhaustion;
    private static long playerMaxManaCore;

    private static long playerManaCoreLevel;
    private static long playerManaCoreXp;

    private static boolean playerDestructionActive;

    public static void setMana(long mana) {
        ClientStatsData.playerMana = mana;
    }
    public static void setMaxMana(long max_mana) {
        ClientStatsData.playerMaxMana = max_mana;
    }
    public static long getPlayerMana() {
        return playerMana;
    }
    public static long getPlayerMaxMana() {
        return playerMaxMana;
    }

    public static void setManaCore(long manacore) {
        ClientStatsData.playerManaCore = manacore;
    }
    public static void setMaxManaCore(long max_manacore) {
        ClientStatsData.playerMaxManaCore = max_manacore;
    }
    public static void setManaCoreExhaustion(long manacore_exhaustion) {
        ClientStatsData.playerManaCoreExhaustion = manacore_exhaustion;
    }


    public static void setDestructionActive(boolean destructionActive) {ClientStatsData.playerDestructionActive = destructionActive;}

    public static void setManaCoreLevel(long manacore_level) {
        ClientStatsData.playerManaCoreLevel = manacore_level;
    }

    public static void setManaCoreXp(long manacore_xp) {
        ClientStatsData.playerManaCoreXp = manacore_xp;
    }
    public static long getPlayerManaCore() {
        return playerManaCore;
    }
    public static long getPlayerMaxManaCore() {
        return playerMaxManaCore;
    }
    public static long getPlayerManaCoreExhaustion() {
        return playerManaCoreExhaustion;
    }

    public static boolean getPlayerDestructionActive() {
        return playerDestructionActive;
    }

    public static long getPlayerManaCoreLevel() {
        return playerManaCoreLevel;
    }

    public static long getPlayerManaCoreXp() {
        return playerManaCoreXp;
    }
}
