package net.skyeshade.wol.networking.packet.destruction;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class DestructionActiveDataSyncS2CPacket {
    private final boolean destructionActive;

    public DestructionActiveDataSyncS2CPacket(boolean destructionActive) {
        this.destructionActive = destructionActive;
    }

    public DestructionActiveDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.destructionActive = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(destructionActive);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setDestructionActive(destructionActive);
        });
        return true;
    }
}
