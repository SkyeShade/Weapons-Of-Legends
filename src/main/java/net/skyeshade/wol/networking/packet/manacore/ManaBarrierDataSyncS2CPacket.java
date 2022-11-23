package net.skyeshade.wol.networking.packet.manacore;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class ManaBarrierDataSyncS2CPacket {
    private final long manaBarrier;

    public ManaBarrierDataSyncS2CPacket(long manaBarrier) {
        this.manaBarrier = manaBarrier;
    }

    public ManaBarrierDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.manaBarrier = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(manaBarrier);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setManaBarrier(manaBarrier);
        });
        return true;
    }
}
