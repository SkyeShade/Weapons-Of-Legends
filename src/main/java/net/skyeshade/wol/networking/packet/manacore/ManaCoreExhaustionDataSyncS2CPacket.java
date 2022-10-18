package net.skyeshade.wol.networking.packet.manacore;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class ManaCoreExhaustionDataSyncS2CPacket {
    private final int manacore_exhaustion;

    public ManaCoreExhaustionDataSyncS2CPacket(int manacore_exhaustion) {
        this.manacore_exhaustion = manacore_exhaustion;
    }

    public ManaCoreExhaustionDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.manacore_exhaustion = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(manacore_exhaustion);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setManaCoreExhaustion(manacore_exhaustion);
        });
        return true;
    }
}
