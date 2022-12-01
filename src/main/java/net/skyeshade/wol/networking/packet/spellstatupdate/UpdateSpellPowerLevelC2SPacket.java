package net.skyeshade.wol.networking.packet.spellstatupdate;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.spellslots.SpellSlotsDataSyncS2CPacket;
import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateSpellPowerLevelC2SPacket {


    private final long[] spellPowerLevel;
    public UpdateSpellPowerLevelC2SPacket(long spellPowerLevel, int index) {

        this.spellPowerLevel = new long[]{spellPowerLevel, (long) index};
    }

    public UpdateSpellPowerLevelC2SPacket(FriendlyByteBuf buf) {
        this.spellPowerLevel = buf.readLongArray();

    }

    public void toBytes(FriendlyByteBuf buf) {

        buf.writeLongArray(spellPowerLevel);
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

                stats.addSpellPowerLevel(spellPowerLevel[0], (int)spellPowerLevel[1]);

                ModMessages.sendToPlayer(new SpellPowerLevelDataSyncS2CPacket(stats.getSpellPowerLevel()), player);
            });


        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
