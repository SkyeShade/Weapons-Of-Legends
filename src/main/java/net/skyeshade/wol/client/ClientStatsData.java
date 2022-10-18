package net.skyeshade.wol.client;

public class ClientStatsData {
    private static int playerMana;
    private static int playerMaxMana;
    private static int playerManaCore;
    private static int playerManaCoreExhaustion;
    private static int playerMaxManaCore;

    public static void setMana(int mana) {
        ClientStatsData.playerMana = mana;
    }
    public static void setMaxMana(int max_mana) {
        ClientStatsData.playerMaxMana = max_mana;
    }
    public static int getPlayerMana() {
        return playerMana;
    }
    public static int getPlayerMaxMana() {
        return playerMaxMana;
    }

    public static void setManaCore(int manacore) {
        ClientStatsData.playerManaCore = manacore;
    }
    public static void setMaxManaCore(int max_manacore) {
        ClientStatsData.playerMaxManaCore = max_manacore;
    }
    public static void setManaCoreExhaustion(int manacore_exhaustion) {
        ClientStatsData.playerManaCoreExhaustion = manacore_exhaustion;
    }
    public static int getPlayerManaCore() {
        return playerManaCore;
    }
    public static int getPlayerMaxManaCore() {
        return playerMaxManaCore;
    }
    public static int getPlayerManaCoreExhaustion() {
        return playerManaCoreExhaustion;
    }
}
