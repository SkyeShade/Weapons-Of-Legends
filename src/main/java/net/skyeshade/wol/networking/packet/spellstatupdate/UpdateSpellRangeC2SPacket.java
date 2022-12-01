package net.skyeshade.wol.networking.packet.spellstatupdate;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateSpellRangeC2SPacket {


    private final long[] spellRange;
    public UpdateSpellRangeC2SPacket(long spellRange, int index) {

        this.spellRange = new long[]{spellRange, (long) index};
    }

    public UpdateSpellRangeC2SPacket(FriendlyByteBuf buf) {
        this.spellRange = buf.readLongArray();

    }

    public void toBytes(FriendlyByteBuf buf) {

        buf.writeLongArray(spellRange);
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

                stats.addSpellRange(spellRange[0], (int)spellRange[1]);

                ModMessages.sendToPlayer(new SpellRangeDataSyncS2CPacket(stats.getSpellRange()), player);
            });


        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
