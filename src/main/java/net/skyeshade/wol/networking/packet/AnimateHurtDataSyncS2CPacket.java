package net.skyeshade.wol.networking.packet;


import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AnimateHurtDataSyncS2CPacket {


    public AnimateHurtDataSyncS2CPacket() {

    }

    public AnimateHurtDataSyncS2CPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!

            Minecraft.getInstance().player.animateHurt();
        });
        return true;
    }
}
