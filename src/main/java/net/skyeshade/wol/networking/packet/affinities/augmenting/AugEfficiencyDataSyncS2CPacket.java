package net.skyeshade.wol.networking.packet.affinities.augmenting;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class AugEfficiencyDataSyncS2CPacket {
    private final long augEfficiency;

    public AugEfficiencyDataSyncS2CPacket(long augEfficiency) {
        this.augEfficiency = augEfficiency;
    }

    public AugEfficiencyDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.augEfficiency = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(augEfficiency);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setPlayerAugmentingEfficiency(augEfficiency);





        });
        return true;
    }
}
