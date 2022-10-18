package net.skyeshade.wol.stats;

import net.minecraft.nbt.CompoundTag;


public class PlayerStats {

    private int mana;
    private int max_mana;
    private final int MIN_MANA = 0;
    private final int MIN_MAXMANA = 0;
    //private final int MAX_MANA = 79;


    //manacore
    private int manacore;
    private int max_manacore;
    private int manacore_exhaustion;
    private final int MIN_MANACORE = 0;
    private final int MIN_MAXMANACORE = 0;
    private final int MIN_MANACORE_EXHAUSTION = 0;
    //private final int MAX_MANA = 79;




    public int getMana() {
        return mana;
    }
    public void addMana(int add) {
        this.mana = Math.min(mana + add, max_mana);
    }
    public void subMana(int sub) {
        this.mana = Math.max(mana - sub, MIN_MANA);
    }
    public int getMaxMana() {
        return max_mana;
    }
    public void addMaxMana(int add) {
        this.max_mana = max_mana + add;
    }
    public void subMaxMana(int sub) {
        this.max_mana = Math.max(max_mana - sub, MIN_MAXMANA);
    }
    public void setMaxMana(int set) {
        this.max_mana = Math.max(set, MIN_MAXMANA);
    }




    //manacore

    public int getManaCore() {
        return manacore;
    }
    public void addManaCore(int add) {
        this.manacore = Math.min(manacore + add, max_manacore);
    }
    public void subManaCore(int sub) {
        this.manacore = Math.max(manacore - sub, manacore_exhaustion);
    }
    public int getMaxManaCore() {
        return max_manacore;
    }
    public void addMaxManaCore(int add) {
        this.max_manacore = max_manacore + add;
    }
    public void subMaxManaCore(int sub) {
        this.max_manacore = Math.max(max_manacore - sub, MIN_MAXMANACORE);
    }
    public void setMaxManaCore(int set) {
        this.max_manacore = Math.max(set, MIN_MAXMANACORE);
    }
    public int getManaCoreExhaustion() {
        return manacore_exhaustion;
    }
    public void addManaCoreExhaustion(int add) {this.manacore_exhaustion = Math.min(manacore_exhaustion + add, max_manacore);}
    public void subManaCoreExhaustion(int sub) {this.manacore_exhaustion = Math.max(manacore_exhaustion - sub, MIN_MANACORE_EXHAUSTION);}
    public void setManaCoreExhaustion(int set) {
        this.manacore_exhaustion = Math.max(set, MIN_MANACORE_EXHAUSTION);
    }

    public void copyFrom(PlayerStats source) {
        this.mana = source.mana;
        this.max_mana = source.max_mana;
        this.manacore = source.manacore;

        this.max_manacore = source.max_manacore;
        this.manacore_exhaustion = source.manacore_exhaustion;
    }
    public void saveNBTData(CompoundTag nbt) {
        System.out.println("Saving NBT"+"\n"+mana+"\n"+max_mana);
        nbt.putInt("mana", mana);
        nbt.putInt("max_mana", max_mana);

        nbt.putInt("manacore", manacore);
        nbt.putInt("max_manacore", max_manacore);
        nbt.putInt("manacore_exhaustion", manacore_exhaustion);

    }
    public void loadNBTData(CompoundTag nbt) {
        System.out.println("Loading NBT"+"\n"+mana+"\n"+max_mana);
        mana = nbt.getInt("mana");
        max_mana = nbt.getInt("max_mana");
        manacore = nbt.getInt("manacore");
        max_manacore = nbt.getInt("max_manacore");
        manacore_exhaustion = nbt.getInt("manacore_exhaustion");
    }
}
