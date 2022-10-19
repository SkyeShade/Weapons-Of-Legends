package net.skyeshade.wol.networking.packet.manacore;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class MaxManaCoreDataSyncS2CPacket {
    private final long max_manacore;

    public MaxManaCoreDataSyncS2CPacket(long max_manacore) {
        this.max_manacore = max_manacore;
    }

    public MaxManaCoreDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.max_manacore = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(max_manacore);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setMaxManaCore(max_manacore);
        });
        return true;
    }
}
