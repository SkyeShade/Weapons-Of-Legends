package net.skyeshade.wol.networking.packet.manacore;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class MaxManaBarrierDataSyncS2CPacket {
    private final long max_manabarrier;

    public MaxManaBarrierDataSyncS2CPacket(long max_manabarrier) {
        this.max_manabarrier = max_manabarrier;
    }

    public MaxManaBarrierDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.max_manabarrier = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(max_manabarrier);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setMaxManaBarrier(max_manabarrier);
        });
        return true;
    }
}
