package net.skyeshade.wol.networking.packet.manacore;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class MaxManaCoreDataSyncS2CPacket {
    private final int max_manacore;

    public MaxManaCoreDataSyncS2CPacket(int max_manacore) {
        this.max_manacore = max_manacore;
    }

    public MaxManaCoreDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.max_manacore = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(max_manacore);
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
