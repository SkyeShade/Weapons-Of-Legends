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
    public static Capability<PlayerStats> PLAYER_MAXMANA = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    //mancore
    public static Capability<PlayerStats> PLAYER_MANACORE = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MAXMANACORE = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    public static Capability<PlayerStats> PLAYER_MANACORE_EXHAUSTION = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });

    public static Capability<PlayerStats> PLAYER_DESTRUCTION_ACTIVE = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });

    private PlayerStats mana = null;
    private PlayerStats max_mana = null;
    //manacore
    private PlayerStats manacore = null;
    private PlayerStats max_manacore = null;
    private PlayerStats manacore_exhaustion = null;

    private PlayerStats destructionActive = null;
    private final LazyOptional<PlayerStats> manaOptional = LazyOptional.of(this::createPlayerMana);
    private final LazyOptional<PlayerStats> maxManaOptional = LazyOptional.of(this::createPlayerMaxMana);
    //manacore
    private final LazyOptional<PlayerStats> manaCoreOptional = LazyOptional.of(this::createPlayerManaCore);
    private final LazyOptional<PlayerStats> maxManaCoreOptional = LazyOptional.of(this::createPlayerMaxManaCore);
    private final LazyOptional<PlayerStats> manaCoreExhaustionOptional = LazyOptional.of(this::createPlayerManaCoreExhaustion);

    private final LazyOptional<PlayerStats> destructionActiveOptional = LazyOptional.of(this::createPlayerMana);

    private PlayerStats createPlayerMana() {
        if(this.mana == null) {
            this.mana = new PlayerStats();
        }
        return this.mana;
    }
    private PlayerStats createPlayerMaxMana() {if(this.max_mana == null) {this.max_mana = new PlayerStats();}return this.max_mana;}


    //manacore

    private PlayerStats createPlayerManaCore() {if(this.manacore == null) {this.manacore = new PlayerStats();}return this.manacore;}
    private PlayerStats createPlayerMaxManaCore() {if(this.max_manacore == null) {this.max_manacore = new PlayerStats();}return this.max_manacore;}
    private PlayerStats createPlayerManaCoreExhaustion() {if(this.manacore_exhaustion == null) {this.manacore_exhaustion = new PlayerStats();}return this.manacore_exhaustion;}
    private PlayerStats createPlayerDestructionActive() {if(this.destructionActive == null) {this.destructionActive = new PlayerStats();}return this.destructionActive;}

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_MANA) {
            return manaOptional.cast();
        }
        if(cap == PLAYER_MAXMANA) {
            return maxManaOptional.cast();
        }
        if(cap == PLAYER_MANACORE) {
            return manaCoreOptional.cast();
        }
        if(cap == PLAYER_MAXMANACORE) {
            return maxManaCoreOptional.cast();
        }
        if(cap == PLAYER_MANACORE_EXHAUSTION) {
            return manaCoreExhaustionOptional.cast();
        }
        if(cap == PLAYER_DESTRUCTION_ACTIVE) {
            return destructionActiveOptional.cast();
        }
        return LazyOptional.empty();
    }

    List<PlayerStats> saveNBTDataList = new ArrayList<PlayerStats>();
    @Override
    public CompoundTag serializeNBT() {
        saveNBTDataList.add(createPlayerMana());
        saveNBTDataList.add(createPlayerMaxMana());
        saveNBTDataList.add(createPlayerManaCore());
        saveNBTDataList.add(createPlayerMaxManaCore());
        saveNBTDataList.add(createPlayerManaCoreExhaustion());
        saveNBTDataList.add(createPlayerDestructionActive());
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


                createPlayerMaxMana().loadNBTData(nbt);


                createPlayerManaCore().loadNBTData(nbt);


                createPlayerMaxManaCore().loadNBTData(nbt);


                createPlayerManaCoreExhaustion().loadNBTData(nbt);

                createPlayerDestructionActive().loadNBTData(nbt);



    }
}
