package net.skyeshade.wol.networking.packet.manacore;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class ManaBarrierReviveDataSyncS2CPacket {
    private final long manabarrierrevive;

    public ManaBarrierReviveDataSyncS2CPacket(long manabarrierrevive) {
        this.manabarrierrevive = manabarrierrevive;
    }

    public ManaBarrierReviveDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.manabarrierrevive = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(manabarrierrevive);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setManaBarrierRevive(manabarrierrevive);
        });
        return true;
    }
}
