package net.skyeshade.wol.networking.packet.manacore;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class ManaCoreLevelDataSyncS2CPacket {
    private final long manacore_level;

    public ManaCoreLevelDataSyncS2CPacket(long manacore_level) {
        this.manacore_level = manacore_level;
    }

    public ManaCoreLevelDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.manacore_level = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(manacore_level);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setManaCoreLevel(manacore_level);
        });
        return true;
    }
}
