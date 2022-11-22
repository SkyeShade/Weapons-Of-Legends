package net.skyeshade.wol.networking.packet.mana;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.manacore.ManaCoreLevelDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.manacore.ManaCoreXpDataSyncS2CPacket;
import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateManaC2SPacket {
    private final long manaChange;
    public UpdateManaC2SPacket(long manaChange) {
        this.manaChange = manaChange;
    }

    public UpdateManaC2SPacket(FriendlyByteBuf buf) {
        this.manaChange = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(manaChange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();



                // increase the water level / stats level of player
                // Output the current stats level
            player.getCapability(PlayerStatsProvider.PLAYER_MANA).ifPresent(mana -> {

                    mana.addMana(manaChange);
                    if (manaChange < 0) {
                        //if mana change is over 100, it calculates how much xp and possibly level you should get and how much should remain in the xp buffer
                        player.getCapability(PlayerStatsProvider.PLAYER_MANATOXP).ifPresent(manatoxp -> {


                                player.getCapability(PlayerStatsProvider.PLAYER_MANACORE_XP).ifPresent(manacorexp -> {

                                    manacorexp.addManaCoreXp((int)(Math.abs(manaChange)+manatoxp.getManaToXp() / 100));

                                    manatoxp.setManaToXp((manatoxp.getManaToXp()+Math.abs(manaChange))-(int)(((Math.abs(manaChange)+manatoxp.getManaToXp()) / 100)*100));

                                });




                        });


                    }

                ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana()), player);
                ModMessages.sendToPlayer(new ManaCoreXpDataSyncS2CPacket(mana.getManaCoreXp()), player);
                ModMessages.sendToPlayer(new ManaCoreLevelDataSyncS2CPacket(mana.getManaCoreLevel()), player);
            });


        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
