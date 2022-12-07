package net.skyeshade.wol.networking.packet.affinities.earth;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class EarthAffinityDataSyncS2CPacket {
    private final long earthAffinity;

    public EarthAffinityDataSyncS2CPacket(long earthAffinity) {
        this.earthAffinity = earthAffinity;
    }

    public EarthAffinityDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.earthAffinity = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(earthAffinity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setPlayerEarthAffinity(earthAffinity);





        });
        return true;
    }
}
