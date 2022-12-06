package net.skyeshade.wol.networking.packet.spellfire.extras;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class CastingAmountDataSyncS2CPacket {
    private final int castingAmount;

    public CastingAmountDataSyncS2CPacket(int castingAmount) {
        this.castingAmount = castingAmount;
    }

    public CastingAmountDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.castingAmount = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(castingAmount);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setPlayerCastingAmount(castingAmount);





        });
        return true;
    }
}
