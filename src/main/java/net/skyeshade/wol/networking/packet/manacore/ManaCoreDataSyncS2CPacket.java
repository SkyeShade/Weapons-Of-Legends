package net.skyeshade.wol.networking.packet.manacore;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class ManaCoreDataSyncS2CPacket {
    private final int manacore;

    public ManaCoreDataSyncS2CPacket(int manacore) {
        this.manacore = manacore;
    }

    public ManaCoreDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.manacore = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(manacore);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setManaCore(manacore);
        });
        return true;
    }
}
