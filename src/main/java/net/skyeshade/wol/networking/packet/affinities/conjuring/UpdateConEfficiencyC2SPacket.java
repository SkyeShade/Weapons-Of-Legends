package net.skyeshade.wol.networking.packet.affinities.conjuring;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;

import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateConEfficiencyC2SPacket {
    private final long conEfficiencyChange;


    public UpdateConEfficiencyC2SPacket(long conEfficiencyChange) {
        this.conEfficiencyChange = conEfficiencyChange;
    }

    public UpdateConEfficiencyC2SPacket(FriendlyByteBuf buf) {
        this.conEfficiencyChange = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(conEfficiencyChange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();

            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    stats.addConjuringEfficiency(conEfficiencyChange);
                    //StatSystems.xpSystem(conEfficiencyChange, player);



                ModMessages.sendToPlayer(new ConEfficiencyDataSyncS2CPacket(stats.getConjuringEfficiency()), player);

            });


        });
        return true;
    }

}
