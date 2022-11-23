package net.skyeshade.wol.networking.packet.manacore;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class ManaBarrierAliveDataSyncS2CPacket {
    private final boolean manaBarrierAlive;

    public ManaBarrierAliveDataSyncS2CPacket(boolean manaBarrierAlive) {
        this.manaBarrierAlive = manaBarrierAlive;
    }

    public ManaBarrierAliveDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.manaBarrierAlive = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(manaBarrierAlive);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setManaBarrierAlive(manaBarrierAlive);
        });
        return true;
    }
}
