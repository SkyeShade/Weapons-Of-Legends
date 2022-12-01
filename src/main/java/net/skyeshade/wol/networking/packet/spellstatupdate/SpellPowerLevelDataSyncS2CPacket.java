package net.skyeshade.wol.networking.packet.spellstatupdate;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class SpellPowerLevelDataSyncS2CPacket {
    private final long[] spellPowerLevel;

    public SpellPowerLevelDataSyncS2CPacket(long[] spellPowerLevel) {
        this.spellPowerLevel = spellPowerLevel;
    }

    public SpellPowerLevelDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.spellPowerLevel = buf.readLongArray();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLongArray(spellPowerLevel);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setPlayerSpellPowerLevel(spellPowerLevel);
        });
        return true;
    }
}
