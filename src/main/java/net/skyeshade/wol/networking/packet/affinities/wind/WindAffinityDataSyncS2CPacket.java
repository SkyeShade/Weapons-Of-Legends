package net.skyeshade.wol.networking.packet.affinities.wind;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class WindAffinityDataSyncS2CPacket {
    private final long windAffinity;

    public WindAffinityDataSyncS2CPacket(long windAffinity) {
        this.windAffinity = windAffinity;
    }

    public WindAffinityDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.windAffinity = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(windAffinity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setPlayerWindAffinity(windAffinity);





        });
        return true;
    }
}
