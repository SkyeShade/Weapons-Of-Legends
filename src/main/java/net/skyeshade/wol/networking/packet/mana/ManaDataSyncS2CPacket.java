package net.skyeshade.wol.networking.packet.mana;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class ManaDataSyncS2CPacket {
    private final long mana;

    public ManaDataSyncS2CPacket(long mana) {
        this.mana = mana;
    }

    public ManaDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.mana = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(mana);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setMana(mana);





        });
        return true;
    }
}
