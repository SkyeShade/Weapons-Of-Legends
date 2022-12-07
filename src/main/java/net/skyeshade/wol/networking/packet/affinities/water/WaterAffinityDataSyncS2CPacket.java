package net.skyeshade.wol.networking.packet.affinities.water;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class WaterAffinityDataSyncS2CPacket {
    private final long waterAffinity;

    public WaterAffinityDataSyncS2CPacket(long waterAffinity) {
        this.waterAffinity = waterAffinity;
    }

    public WaterAffinityDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.waterAffinity = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(waterAffinity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setPlayerWaterAffinity(waterAffinity);





        });
        return true;
    }
}
