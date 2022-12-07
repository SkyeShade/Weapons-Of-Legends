package net.skyeshade.wol.networking.packet.affinities.conjuring;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class ConEfficiencyDataSyncS2CPacket {
    private final long conEfficiency;

    public ConEfficiencyDataSyncS2CPacket(long conEfficiency) {
        this.conEfficiency = conEfficiency;
    }

    public ConEfficiencyDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.conEfficiency = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(conEfficiency);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setPlayerConjuringEfficiency(conEfficiency);





        });
        return true;
    }
}
