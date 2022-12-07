package net.skyeshade.wol.networking.packet.affinities.fire;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class FireAffinityDataSyncS2CPacket {
    private final long fireAffinity;

    public FireAffinityDataSyncS2CPacket(long fireAffinity) {
        this.fireAffinity = fireAffinity;
    }

    public FireAffinityDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.fireAffinity = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(fireAffinity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setPlayerFireAffinity(fireAffinity);





        });
        return true;
    }
}
