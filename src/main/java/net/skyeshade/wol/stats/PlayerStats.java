package net.skyeshade.wol.stats;

import net.minecraft.nbt.CompoundTag;
import net.skyeshade.wol.util.SpellBaseStatVariables;

import java.lang.reflect.Array;


public class PlayerStats {

    private final long MIN_MANABARRIER = 0;
    private final long MIN_MAXMANABARRIER = 0;
    private final long MIN_MANABARRIERREVIVE = 0;
    private final long MIN_MANA = 0;
    private final long MIN_MAXMANA = 0;
    private final long MIN_MAXHP = 0;
    private final long MIN_MANACORE_LEVEL = 0;
    private final long MIN_MANATOXP = 0;
    private final long MAX_MANATOXP = 0;
    private final long MIN_MANACORE_XP = 0;

    private long mana;

    private long hp;
    //stores all spellslots from 1 to 9
    private long[] spellSlots = {0,0,0,0,0,0,0,0,0};

    // row length is number of spells, add one per spell id, this is literally only so it doesnt get confusing af
    private long[] spellPowerLevel = SpellBaseStatVariables.spellPowerLevel;
    //also add another number per spell id
    private long[] spellRange = SpellBaseStatVariables.spellRange;
    //also add one for each spell id
    //byte instead of boolean, 0 = false 1 = true
    private byte[] spellPassiveToggle = SpellBaseStatVariables.spellPassiveToggle;



    private long max_hp;
    private float hpregenbuffer;
    private long max_mana;
    private long manabarrier;
    private long max_manabarrier;
    private long manabarrierrevive;
    private boolean manabarrieralive;

    private boolean spellSlotsToggle;
    private long manacore_level;
    private long manacore_xp;
    private long manatoxp;

    private float manaregenbuffer;
    private float manabarrierregenbuffer;

    private boolean destructionActive;
    private boolean menuStatTabToggle;

    //get functions
    public long getMana() {
        return mana;
    }

    public long getHp() {
        return hp;
    }

    public long getMaxHp() {
        return max_hp;
    }

    public float getHpRegenBuffer() {
        return hpregenbuffer;
    }
    public float getManaRegenBuffer() {
        return manaregenbuffer;
    }
    public float getManaBarrierRegenBuffer() {
        return manabarrierregenbuffer;
    }
    public long getManaToXp() {
        return manatoxp;
    }
    public long getMaxMana() {
        return max_mana;
    }
    public long getManaBarrier() {
        return manabarrier;
    }
    public long getMaxManaBarrier() {
        return max_manabarrier;
    }
    public long getManaBarrierRevive() {return manabarrierrevive;}
    public long getManaCoreLevel() {
        return manacore_level;
    }
    public long getManaCoreXp() {
        return manacore_xp;
    }

    public long[] getSpellSlots() {
        return spellSlots;
    }

    public long[] getSpellPowerLevel() {
        return spellPowerLevel;
    }

    public long[] getSpellRange() {
        return spellRange;
    }

    public byte[] getSpellPassiveToggle() {
        return spellPassiveToggle;
    }

    public boolean getDestructionActive() {
        return destructionActive;
    }
    public boolean getMenuStatTabToggle() {
        return menuStatTabToggle;
    }

    public boolean getSpellSlotsToggle() {
        return spellSlotsToggle;
    }

    public boolean getManaBarrierAlive() {return manabarrieralive;}

    //set functions
    public void setMaxMana(long set) {
        this.max_mana = Math.max(set, MIN_MAXMANA);
    }
    public void setMaxHp(long set) {
        this.max_hp = Math.max(set, MIN_MAXHP);
    }
    public void setHp(long set) {
        this.hp = Math.max(set, 0);
    }

    public void setSpellSlots(long set, int index) {
        this.spellSlots[index] = set;
    }

    public void setSpellPowerLevel(long set, int index) {
        this.spellPowerLevel[index] = set;
    }

    public void setSpellRange(long set, int index) {
        this.spellRange[index] = set;
    }

    public void setSpellPassiveToggle(byte set, int index) {
        this.spellPassiveToggle[index] = set;
    }
    public void setHpRegenBuffer(float set) {this.hpregenbuffer = set;}
    public void setManaRegenBuffer(float set) {this.manaregenbuffer = set;}
    public void setManaBarrierRegenBuffer(float set) {this.manabarrierregenbuffer = set;}
    public void setMaxManaBarrier(long set) {
        this.max_manabarrier = Math.max(set, MIN_MAXMANABARRIER);
    }
    public void setManaBarrierRevive(long set) {
        this.manabarrierrevive = Math.max(set, MIN_MANABARRIERREVIVE);
    }
    public void setManaBarrier(long set) {
        this.manabarrier = Math.max(set, MIN_MANABARRIER);
    }
    public void setManaCoreLevel(long set) {
        this.manacore_level = Math.max(set, MIN_MANACORE_LEVEL);
    }
    public void setManaToXp(long set) {
        this.manatoxp = Math.max(set, MIN_MANATOXP);
    }
    public void setManaCoreXp(long set) {
        this.manacore_xp = Math.max(set, MIN_MANACORE_XP);
    }
    public void setDestructionActive(boolean set) {this.destructionActive = set;}

    public void setMenuStatTabToggle(boolean set) {this.menuStatTabToggle = set;}

    public void setSpellSlotsToggle(boolean set) {this.spellSlotsToggle = set;}
    public void setManaBarrierAlive(boolean set) {this.manabarrieralive = set;}


    //add functions


    public void addMana(long add) {this.mana = Math.min(mana + add, max_mana);}



    public void addSpellPowerLevel(long add, int index) {
        this.spellPowerLevel[index] = spellPowerLevel[index] + add;
    }

    public void addSpellRange(long add, int index) {
        this.spellRange[index] = spellRange[index] + add;
    }
    public void addHp(long add) {this.hp = Math.min(hp + add, max_hp);}
    public void addMaxHp(long add) {this.max_hp = max_hp + add;}
    public void addHpRegenBuffer(float add) {this.hpregenbuffer = hpregenbuffer + add;}
    public void addManaRegenBuffer(float add) {this.manaregenbuffer = manaregenbuffer + add;}
    public void addManaBarrierRegenBuffer(float add) {this.manabarrierregenbuffer = manabarrierregenbuffer + add;}
    public void addManaToXp(long add) {this.manatoxp = (manatoxp + add);}
    public void addMaxMana(long add) {this.max_mana = max_mana + add;}
    public void addManaBarrier(long add) {
        this.manabarrier = Math.min(manabarrier + add, max_manabarrier);
    }
    public void addMaxManaBarrier(long add) {
        this.max_manabarrier = max_manabarrier + add;
    }
    public void addManaBarrierRevive(long add) {this.manabarrierrevive = manabarrierrevive + add;}
    public void addManaCoreLevel(long add) {this.manacore_level = manacore_level + add;}
    public void addManaCoreXp(long add) {this.manacore_xp = manacore_xp + add;}



    public void copyFrom(PlayerStats source) {
        this.mana = source.mana;
        this.hp = source.hp;
        this.max_hp = source.max_hp;
        this.hpregenbuffer = source.hpregenbuffer;
        this.manaregenbuffer = source.manaregenbuffer;
        this.manabarrierregenbuffer = source.manabarrierregenbuffer;
        this.max_mana = source.max_mana;
        this.manabarrier = source.manabarrier;
        this.max_manabarrier = source.max_manabarrier;
        this.manabarrierrevive = source.manabarrierrevive;
        this.manabarrieralive = source.manabarrieralive;
        this.destructionActive = source.destructionActive;
        this.menuStatTabToggle = source.menuStatTabToggle;
        this.manacore_level = source.manacore_level;
        this.manacore_xp = source.manacore_xp;
        this.manatoxp = source.manatoxp;
        this.spellSlotsToggle = source.spellSlotsToggle;
        this.spellSlots = source.spellSlots;
        this.spellPowerLevel = source.spellPowerLevel;
        this.spellRange = source.spellRange;
        this.spellPassiveToggle = source.spellPassiveToggle;
    }
    public void saveNBTData(CompoundTag nbt) {
        //System.out.prlongln("Saving NBT"+"\n"+mana+"\n"+max_mana);
        nbt.putLong("mana", mana);
        nbt.putLong("hp", hp);
        nbt.putLong("max_hp", max_hp);
        nbt.putFloat("hpregenbuffer", hpregenbuffer);
        nbt.putFloat("manaregenbuffer", manaregenbuffer);
        nbt.putFloat("manabarrierregenbuffer", manabarrierregenbuffer);
        nbt.putLong("max_mana", max_mana);
        nbt.putLong("manabarrier", manabarrier);
        nbt.putLong("max_manabarrier", max_manabarrier);
        nbt.putLong("manabarrierrevive", manabarrierrevive);
        nbt.putBoolean("destruction_active", destructionActive);
        nbt.putBoolean("menuStatTabToggle", menuStatTabToggle);
        nbt.putBoolean("spellSlotsToggle", spellSlotsToggle);
        nbt.putBoolean("manabarrieralive", manabarrieralive);
        nbt.putLong("manacore_level", manacore_level);
        nbt.putLong("manacore_xp", manacore_xp);
        nbt.putLong("manatoxp", manatoxp);
        nbt.putLongArray("spellslots", spellSlots);
        nbt.putLongArray("spellpowerlevel", spellPowerLevel);
        nbt.putLongArray("spellrange", spellRange);
        nbt.putByteArray("spellpassivetoggle", spellPassiveToggle);


    }

    public void loadNBTData(CompoundTag nbt) {
        //System.out.prlongln("Loading NBT"+"\n"+mana+"\n"+max_mana);
        mana = nbt.getLong("mana");
        hp = nbt.getLong("hp");
        max_hp = nbt.getLong("max_hp");
        hpregenbuffer = nbt.getFloat("hpregenbuffer");
        manaregenbuffer = nbt.getFloat("manaregenbuffer");
        manabarrierregenbuffer = nbt.getFloat("manabarrierregenbuffer");
        max_mana = nbt.getLong("max_mana");
        manabarrier = nbt.getLong("manabarrier");
        max_manabarrier = nbt.getLong("max_manabarrier");
        manabarrierrevive = nbt.getLong("manabarrierrevive");
        destructionActive = nbt.getBoolean("destruction_active");
        menuStatTabToggle = nbt.getBoolean("menuStatTabToggle");
        spellSlotsToggle = nbt.getBoolean("spellSlotsToggle");
        manabarrieralive = nbt.getBoolean("manabarrieralive");
        manacore_level = nbt.getLong("manacore_level");
        manacore_xp = nbt.getLong("manacore_xp");
        manatoxp = nbt.getLong("manatoxp");
        spellSlots = nbt.getLongArray("spellslots");
        spellPowerLevel = nbt.getLongArray("spellpowerlevel");
        spellRange = nbt.getLongArray("spellrange");
        spellPassiveToggle = nbt.getByteArray("spellpassivetoggle");
    }
}
