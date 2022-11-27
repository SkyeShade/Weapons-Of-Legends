package net.skyeshade.wol.networking.packet.spellslots;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.client.ClientStatsData;

import java.util.function.Supplier;

public class SpellSlotsToggleDataSyncS2CPacket {
    private final boolean spellSlotsToggle;

    public SpellSlotsToggleDataSyncS2CPacket(boolean spellSlotsToggle) {
        this.spellSlotsToggle = spellSlotsToggle;
    }

    public SpellSlotsToggleDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.spellSlotsToggle = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(spellSlotsToggle);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStatsData.setSpellSlotsToggle(spellSlotsToggle);
        });
        return true;
    }
}
