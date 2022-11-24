package net.skyeshade.wol.networking.packet.hp;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class MaxHpDataSyncS2CPacket {
    private final long max_hp;

    public MaxHpDataSyncS2CPacket(long max_hp) {
        this.max_hp = max_hp;
    }

    public MaxHpDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.max_hp = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(max_hp);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setMaxHp(max_hp);
        });
        return true;
    }
}
