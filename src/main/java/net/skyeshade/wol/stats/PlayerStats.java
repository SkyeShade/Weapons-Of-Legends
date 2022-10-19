package net.skyeshade.wol.stats;

import net.minecraft.nbt.CompoundTag;


public class PlayerStats {

    private long mana;
    private long max_mana;
    private final long MIN_MANA = 0;
    private final long MIN_MAXMANA = 0;
    //private final long MAX_MANA = 79;


    //manacore
    private long manacore;
    private long max_manacore;
    private long manacore_exhaustion;
    private final long MIN_MANACORE = 0;
    private final long MIN_MAXMANACORE = 0;
    private final long MIN_MANACORE_EXHAUSTION = 0;
    //private final long MAX_MANA = 79;




    public long getMana() {
        return mana;
    }
    public void addMana(long add) {
        this.mana = Math.min(mana + add, max_mana);
    }
    public void subMana(long sub) {
        this.mana = Math.max(mana - sub, MIN_MANA);
    }
    public long getMaxMana() {
        return max_mana;
    }
    public void addMaxMana(long add) {
        this.max_mana = max_mana + add;
    }
    public void subMaxMana(long sub) {
        this.max_mana = Math.max(max_mana - sub, MIN_MAXMANA);
    }
    public void setMaxMana(long set) {
        this.max_mana = Math.max(set, MIN_MAXMANA);
    }

    //manacore

    public long getManaCore() {
        return manacore;
    }
    public void addManaCore(long add) {
        this.manacore = Math.min(manacore + add, max_manacore);
    }
    public void subManaCore(long sub) {
        this.manacore = Math.max(manacore - sub, manacore_exhaustion);
    }
    public long getMaxManaCore() {
        return max_manacore;
    }
    public void addMaxManaCore(long add) {
        this.max_manacore = max_manacore + add;
    }
    public void subMaxManaCore(long sub) {
        this.max_manacore = Math.max(max_manacore - sub, MIN_MAXMANACORE);
    }
    public void setMaxManaCore(long set) {
        this.max_manacore = Math.max(set, MIN_MAXMANACORE);
    }
    public long getManaCoreExhaustion() {
        return manacore_exhaustion;
    }
    public void addManaCoreExhaustion(long add) {
        this.manacore_exhaustion = Math.min(manacore_exhaustion + add, max_manacore);}
    public void subManaCoreExhaustion(long sub) {
        this.manacore_exhaustion = Math.max(manacore_exhaustion - sub, MIN_MANACORE_EXHAUSTION);}
    public void setManaCoreExhaustion(long set) {
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
        //System.out.prlongln("Saving NBT"+"\n"+mana+"\n"+max_mana);
        nbt.putLong("mana", mana);
        nbt.putLong("max_mana", max_mana);

        nbt.putLong("manacore", manacore);
        nbt.putLong("max_manacore", max_manacore);
        nbt.putLong("manacore_exhaustion", manacore_exhaustion);

    }
    public void loadNBTData(CompoundTag nbt) {
        //System.out.prlongln("Loading NBT"+"\n"+mana+"\n"+max_mana);
        mana = nbt.getLong("mana");
        max_mana = nbt.getLong("max_mana");
        manacore = nbt.getLong("manacore");
        max_manacore = nbt.getLong("max_manacore");
        manacore_exhaustion = nbt.getLong("manacore_exhaustion");
    }
}
