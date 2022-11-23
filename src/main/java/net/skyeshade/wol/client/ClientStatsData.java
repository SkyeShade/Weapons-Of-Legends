package net.skyeshade.wol.client;

public class ClientStatsData {
    private static long playerMana;
    private static long playerMaxMana;
    private static long playerManaBarrier;
    private static long playerManaBarrierRevive;
    private static long playerMaxManaBarrier;

    private static long playerManaCoreLevel;
    private static long playerManaCoreXp;

    private static boolean playerDestructionActive;

    private static boolean playerManaBarrierAlive;

    private static boolean playerMenuStatTabToggle;

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

    public static void setManaBarrier(long manabarrier) {
        ClientStatsData.playerManaBarrier = manabarrier;
    }
    public static void setMaxManaBarrier(long max_manabarrier) {ClientStatsData.playerMaxManaBarrier = max_manabarrier;}
    public static void setManaBarrierRevive(long manabarrierrevive) {ClientStatsData.playerManaBarrierRevive = manabarrierrevive;}


    public static void setDestructionActive(boolean destructionActive) {ClientStatsData.playerDestructionActive = destructionActive;}

    public static void setManaBarrierAlive(boolean manaBarrierAlive) {ClientStatsData.playerManaBarrierAlive = manaBarrierAlive;}

    public static void setMenuStatTabToggle(boolean menuStatTabToggle) {ClientStatsData.playerMenuStatTabToggle = menuStatTabToggle;}

    public static void setManaCoreLevel(long manacore_level) {
        ClientStatsData.playerManaCoreLevel = manacore_level;
    }

    public static void setManaCoreXp(long manacore_xp) {
        ClientStatsData.playerManaCoreXp = manacore_xp;
    }
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

    public static long getPlayerManaCoreLevel() {
        return playerManaCoreLevel;
    }

    public static long getPlayerManaCoreXp() {
        return playerManaCoreXp;
    }
}
