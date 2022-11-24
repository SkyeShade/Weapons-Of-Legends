package net.skyeshade.wol.networking.packet.hp;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class HpDataSyncS2CPacket {
    private final long hp;

    public HpDataSyncS2CPacket(long hp) {
        this.hp = hp;
    }

    public HpDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.hp = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(hp);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setHp(hp);
        });
        return true;
    }
}
