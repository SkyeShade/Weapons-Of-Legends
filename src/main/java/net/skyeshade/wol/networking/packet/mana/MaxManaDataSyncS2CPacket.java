package net.skyeshade.wol.networking.packet.mana;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class MaxManaDataSyncS2CPacket {
    private final long max_mana;

    public MaxManaDataSyncS2CPacket(long max_mana) {
        this.max_mana = max_mana;
    }

    public MaxManaDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.max_mana = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(max_mana);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setMaxMana(max_mana);
        });
        return true;
    }
}
