package net.skyeshade.wol.networking.packet.spellstatupdate;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class SpellRangeDataSyncS2CPacket {
    private final long[] spellRange;

    public SpellRangeDataSyncS2CPacket(long[] spellRange) {
        this.spellRange = spellRange;
    }

    public SpellRangeDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.spellRange = buf.readLongArray();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLongArray(spellRange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setPlayerSpellRange(spellRange);
        });
        return true;
    }
}
