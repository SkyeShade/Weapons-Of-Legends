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
    public static Capability<PlayerStats> PLAYER_STATS = CapabilityManager.get(new CapabilityToken<PlayerStats>() { });
    private PlayerStats stats = null;
    private final LazyOptional<PlayerStats> statsOptional = LazyOptional.of(this::createPlayerStats);
    private PlayerStats createPlayerStats() {if(this.stats == null) {this.stats = new PlayerStats();}return this.stats;}

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_STATS) {
            return statsOptional.cast();
        }
        return LazyOptional.empty();
    }
    List<PlayerStats> saveNBTDataList = new ArrayList<PlayerStats>();
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerStats().saveNBTData(nbt);
        return nbt;
    }
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerStats().loadNBTData(nbt);
    }
}
