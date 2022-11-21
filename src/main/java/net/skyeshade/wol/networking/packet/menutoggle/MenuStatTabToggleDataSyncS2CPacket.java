package net.skyeshade.wol.networking.packet.menutoggle;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class MenuStatTabToggleDataSyncS2CPacket {
    private final boolean menuStatTabToggle;

    public MenuStatTabToggleDataSyncS2CPacket(boolean menuStatTabToggle) {
        this.menuStatTabToggle = menuStatTabToggle;
    }

    public MenuStatTabToggleDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.menuStatTabToggle = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(menuStatTabToggle);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setMenuStatTabToggle(menuStatTabToggle);
        });
        return true;
    }
}
