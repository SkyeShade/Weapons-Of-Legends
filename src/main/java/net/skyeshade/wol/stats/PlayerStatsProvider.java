package net.skyeshade.wol.stats;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerStats> PLAYER_MANA = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MANAREGENBUFFER = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MANABARRIERREGENBUFFER = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MANATOXP = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MAXMANA = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MANABARRIER = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MAXMANABARRIER = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MANABARRIERREVIVE = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_DESTRUCTION_ACTIVE = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MANABARRIERALIVE = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MENUSTATTABTOGGLE = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MANACORE_LEVEL = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MANACORE_XP = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });

    private PlayerStats mana = null;
    private PlayerStats manaregenbuffer = null;
    private PlayerStats manabarrierregenbuffer = null;
    private PlayerStats manatoxp = null;
    private PlayerStats max_mana = null;
    //manacore
    private PlayerStats manabarrier = null;
    private PlayerStats max_manabarrier = null;
    private PlayerStats manabarrierrevive = null;

    private PlayerStats destructionActive = null;
    private PlayerStats menuStatTabToggle = null;
    private PlayerStats manabarrieralive = null;

    private PlayerStats manacore_level = null;

    private PlayerStats manacore_xp = null;
    private final LazyOptional<PlayerStats> manaOptional = LazyOptional.of(this::createPlayerMana);
    private final LazyOptional<PlayerStats> manaRegenBufferOptional = LazyOptional.of(this::createPlayerManaRegenBuffer);
    private final LazyOptional<PlayerStats> manaBarrierRegenBufferOptional = LazyOptional.of(this::createPlayerManaBarrierRegenBuffer);
    private final LazyOptional<PlayerStats> manaToXpOptional = LazyOptional.of(this::createPlayerMana);
    private final LazyOptional<PlayerStats> maxManaOptional = LazyOptional.of(this::createPlayerMaxMana);
    //manacore
    private final LazyOptional<PlayerStats> manaBarrierOptional = LazyOptional.of(this::createPlayerManaBarrier);
    private final LazyOptional<PlayerStats> maxManaBarrierOptional = LazyOptional.of(this::createPlayerMaxManaBarrier);
    private final LazyOptional<PlayerStats> manaBarrierReviveOptional = LazyOptional.of(this::createPlayerManaBarrierRevive);
    private final LazyOptional<PlayerStats> manaBarrierAliveOptional = LazyOptional.of(this::createPlayerManaBarrierAlive);

    private final LazyOptional<PlayerStats> destructionActiveOptional = LazyOptional.of(this::createPlayerDestructionActive);
    private final LazyOptional<PlayerStats> menuStatTabToggleOptional = LazyOptional.of(this::createPlayerMenuStatTabToggle);

    private final LazyOptional<PlayerStats> manaCoreLevelOptional = LazyOptional.of(this::createPlayerManaCoreLevel);
    private final LazyOptional<PlayerStats> manaCoreXpOptional = LazyOptional.of(this::createPlayerManaCoreLevel);

    private PlayerStats createPlayerMana() {if(this.mana == null) {this.mana = new PlayerStats();}return this.mana;}
    private PlayerStats createPlayerManaRegenBuffer() {if(this.manaregenbuffer == null) {this.manaregenbuffer = new PlayerStats();}return this.manaregenbuffer;}
    private PlayerStats createPlayerManaBarrierRegenBuffer() {if(this.manabarrierregenbuffer == null) {this.manabarrierregenbuffer = new PlayerStats();}return this.manabarrierregenbuffer;}
    private PlayerStats createPlayerManaToXp() {if(this.manatoxp == null) {this.manatoxp = new PlayerStats();}return this.manatoxp;}
    private PlayerStats createPlayerMaxMana() {if(this.max_mana == null) {this.max_mana = new PlayerStats();}return this.max_mana;}

    //manacore

    private PlayerStats createPlayerManaBarrier() {if(this.manabarrier == null) {this.manabarrier = new PlayerStats();}return this.manabarrier;}
    private PlayerStats createPlayerMaxManaBarrier() {if(this.max_manabarrier == null) {this.max_manabarrier = new PlayerStats();}return this.max_manabarrier;}
    private PlayerStats createPlayerManaBarrierRevive() {if(this.manabarrierrevive == null) {this.manabarrierrevive = new PlayerStats();}return this.manabarrierrevive;}
    private PlayerStats createPlayerManaBarrierAlive() {if(this.manabarrieralive == null) {this.manabarrieralive = new PlayerStats();}return this.manabarrieralive;}
    private PlayerStats createPlayerDestructionActive() {if(this.destructionActive == null) {this.destructionActive = new PlayerStats();}return this.destructionActive;}

    private PlayerStats createPlayerMenuStatTabToggle() {if(this.menuStatTabToggle == null) {this.menuStatTabToggle = new PlayerStats();}return this.menuStatTabToggle;}
    private PlayerStats createPlayerManaCoreLevel() {if(this.manacore_level == null) {this.manacore_level = new PlayerStats();}return this.manacore_level;}
    private PlayerStats createPlayerManaCoreXp() {if(this.manacore_xp == null) {this.manacore_xp = new PlayerStats();}return this.manacore_xp;}
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_MANA) {
            return manaOptional.cast();
        }
        if(cap == PLAYER_MANAREGENBUFFER) {
            return manaRegenBufferOptional.cast();
        }
        if(cap == PLAYER_MANABARRIERREGENBUFFER) {
            return manaBarrierRegenBufferOptional.cast();
        }
        if(cap == PLAYER_MANATOXP) {
            return manaToXpOptional.cast();
        }
        if(cap == PLAYER_MAXMANA) {
            return maxManaOptional.cast();
        }
        if(cap == PLAYER_MANABARRIER) {
            return manaBarrierOptional.cast();
        }
        if(cap == PLAYER_MAXMANABARRIER) {
            return maxManaBarrierOptional.cast();
        }
        if(cap == PLAYER_MANABARRIERREVIVE) {
            return manaBarrierReviveOptional.cast();
        }
        if(cap == PLAYER_MANABARRIERALIVE) {
            return manaBarrierAliveOptional.cast();
        }
        if(cap == PLAYER_DESTRUCTION_ACTIVE) {
            return destructionActiveOptional.cast();
        }
        if(cap == PLAYER_MENUSTATTABTOGGLE) {
            return menuStatTabToggleOptional.cast();
        }
        if(cap == PLAYER_MANACORE_LEVEL) {
            return manaCoreLevelOptional.cast();
        }
        if(cap == PLAYER_MANACORE_XP) {
            return manaCoreXpOptional.cast();
        }
        return LazyOptional.empty();
    }

    List<PlayerStats> saveNBTDataList = new ArrayList<PlayerStats>();
    @Override
    public CompoundTag serializeNBT() {
        saveNBTDataList.add(createPlayerMana());
        saveNBTDataList.add(createPlayerManaRegenBuffer());
        saveNBTDataList.add(createPlayerManaBarrierRegenBuffer());
        saveNBTDataList.add(createPlayerManaToXp());
        saveNBTDataList.add(createPlayerMaxMana());
        saveNBTDataList.add(createPlayerManaBarrier());
        saveNBTDataList.add(createPlayerMaxManaBarrier());
        saveNBTDataList.add(createPlayerManaBarrierRevive());
        saveNBTDataList.add(createPlayerManaBarrierAlive());
        saveNBTDataList.add(createPlayerDestructionActive());
        saveNBTDataList.add(createPlayerMenuStatTabToggle());
        saveNBTDataList.add(createPlayerManaCoreLevel());
        saveNBTDataList.add(createPlayerManaCoreXp());
        for (PlayerStats e : saveNBTDataList) {
            CompoundTag nbt = new CompoundTag();
            e.saveNBTData(nbt);
            return nbt;
        }
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

        createPlayerMana().loadNBTData(nbt);
        createPlayerManaRegenBuffer().loadNBTData(nbt);
        createPlayerManaBarrierRegenBuffer().loadNBTData(nbt);
        createPlayerManaToXp().loadNBTData(nbt);

        createPlayerMaxMana().loadNBTData(nbt);

        createPlayerManaBarrier().loadNBTData(nbt);

        createPlayerMaxManaBarrier().loadNBTData(nbt);

        createPlayerManaBarrierRevive().loadNBTData(nbt);
        createPlayerManaBarrierAlive().loadNBTData(nbt);

        createPlayerDestructionActive().loadNBTData(nbt);
        createPlayerMenuStatTabToggle().loadNBTData(nbt);

        createPlayerManaCoreLevel().loadNBTData(nbt);
        createPlayerManaCoreXp().loadNBTData(nbt);



    }
}
