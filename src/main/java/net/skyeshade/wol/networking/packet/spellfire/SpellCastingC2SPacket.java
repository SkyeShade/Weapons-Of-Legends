package net.skyeshade.wol.networking.packet.spellfire;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.entities.EntityInit;
import net.skyeshade.wol.entities.spells.casting.SpellCaster;
import net.skyeshade.wol.entities.spells.fireelement.FireBallEntity;

import java.util.function.Supplier;

public class SpellCastingC2SPacket {

    private final long spellID;
    public SpellCastingC2SPacket(long spellID) {
        this.spellID = spellID;
    }

    public SpellCastingC2SPacket(FriendlyByteBuf buf) {
        this.spellID = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(spellID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            SpellCaster.CastSpell(player, spellID);


        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
