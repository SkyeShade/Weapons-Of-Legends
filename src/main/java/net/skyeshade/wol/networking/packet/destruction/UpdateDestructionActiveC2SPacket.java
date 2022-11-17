package net.skyeshade.wol.networking.packet.destruction;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.mana.ManaDataSyncS2CPacket;
import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateDestructionActiveC2SPacket {
    private final boolean destructionActiveChange;
    public UpdateDestructionActiveC2SPacket(boolean destructionActive) {this.destructionActiveChange = destructionActive;}

    public UpdateDestructionActiveC2SPacket(FriendlyByteBuf buf) {
        this.destructionActiveChange = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(destructionActiveChange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();



                // increase the water level / stats level of player
                // Output the current stats level
            player.getCapability(PlayerStatsProvider.PLAYER_DESTRUCTION_ACTIVE).ifPresent(destructionActive -> {

                destructionActive.setDestructionActive(destructionActiveChange);


                ModMessages.sendToPlayer(new DestructionActiveDataSyncS2CPacket(destructionActive.getDestructionActive()), player);
            });


        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
