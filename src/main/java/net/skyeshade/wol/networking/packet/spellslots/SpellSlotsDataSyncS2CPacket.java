package net.skyeshade.wol.networking.packet.spellslots;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class SpellSlotsDataSyncS2CPacket {
    private final long[] spellSlots;

    public SpellSlotsDataSyncS2CPacket(long[] spellSlots) {
        this.spellSlots = spellSlots;
    }

    public SpellSlotsDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.spellSlots = buf.readLongArray();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLongArray(spellSlots);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setPlayerSpellSlots(spellSlots);
        });
        return true;
    }
}
