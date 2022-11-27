package net.skyeshade.wol.networking.packet.spellslots;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateSpellSlotsToggleC2SPacket {
    private final boolean spellSlotsToggleChange;
    public UpdateSpellSlotsToggleC2SPacket(boolean spellSlotsToggle) {this.spellSlotsToggleChange = spellSlotsToggle;}

    public UpdateSpellSlotsToggleC2SPacket(FriendlyByteBuf buf) {
        this.spellSlotsToggleChange = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(spellSlotsToggleChange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();



                // increase the water level / stats level of player
                // Output the current stats level
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                stats.setSpellSlotsToggle(spellSlotsToggleChange);


                ModMessages.sendToPlayer(new SpellSlotsToggleDataSyncS2CPacket(stats.getSpellSlotsToggle()), player);
            });


        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
