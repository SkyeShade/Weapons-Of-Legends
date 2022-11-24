package net.skyeshade.wol.networking.packet.menutoggle;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.destruction.DestructionActiveDataSyncS2CPacket;
import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateMenuStatTabToggleC2SPacket {
    private final boolean menuStatTabToggleChange;
    public UpdateMenuStatTabToggleC2SPacket(boolean menuStatTabToggle) {this.menuStatTabToggleChange = menuStatTabToggle;}

    public UpdateMenuStatTabToggleC2SPacket(FriendlyByteBuf buf) {
        this.menuStatTabToggleChange = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(menuStatTabToggleChange);
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

                stats.setMenuStatTabToggle(menuStatTabToggleChange);


                ModMessages.sendToPlayer(new MenuStatTabToggleDataSyncS2CPacket(stats.getMenuStatTabToggle()), player);
            });


        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
