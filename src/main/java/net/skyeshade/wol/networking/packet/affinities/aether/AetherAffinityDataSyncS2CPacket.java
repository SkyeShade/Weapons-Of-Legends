package net.skyeshade.wol.networking.packet.affinities.aether;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class AetherAffinityDataSyncS2CPacket {
    private final long aetherAffinity;

    public AetherAffinityDataSyncS2CPacket(long aetherAffinity) {
        this.aetherAffinity = aetherAffinity;
    }

    public AetherAffinityDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.aetherAffinity = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(aetherAffinity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setPlayerAetherAffinity(aetherAffinity);





        });
        return true;
    }
}
