package net.skyeshade.wol.networking.packet.manacore;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class ManaCoreXpDataSyncS2CPacket {
    private final long manacore_xp;



    public ManaCoreXpDataSyncS2CPacket(long manacore_xp) {
        this.manacore_xp = manacore_xp;
    }

    public ManaCoreXpDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.manacore_xp = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(manacore_xp);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!


            ClientStatsData.setManaCoreXp(manacore_xp);
        });
        return true;
    }
}
