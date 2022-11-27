package net.skyeshade.wol.item.custom;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.skyeshade.wol.abilities.TimeStopAbility;
import net.skyeshade.wol.client.ClientStatsData;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.mana.UpdateManaC2SPacket;
import net.skyeshade.wol.sound.ModSounds;
import net.skyeshade.wol.util.StatSystems;

public class TimeStopSwordItem extends SwordItem {

    public TimeStopSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }




    TimeStopAbility timeStopAbility = new TimeStopAbility();
    static Player player;

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {


        if (ClientStatsData.getPlayerMana() >= 1000) {
            if(pPlayer.getLevel().isClientSide()) {
                ModMessages.sendToServer(new UpdateManaC2SPacket(-1000));


            }

            if (!TimeStopAbility.playerList.isEmpty()) {
                if (TimeStopAbility.playerList.contains(pPlayer)) {
                    int valuesIndex = TimeStopAbility.playerList.indexOf(pPlayer);


                    if (TimeStopAbility.timer.get(valuesIndex) > 41) {
                        TimeStopAbility.timer.set(valuesIndex, 40);
                    }
                }else {
                    timeStopAbility.stopTime(128, pPlayer, 300, pLevel);
                    pPlayer.playSound(ModSounds.STOP_TIME.get(), 10f, 1f);

                    //pPlayer.getCooldowns().addCooldown(this,700);
                    player = pPlayer;
                }
            }else {
                timeStopAbility.stopTime(128, pPlayer, 300, pLevel);
                pPlayer.playSound(ModSounds.STOP_TIME.get(), 10f, 1f);

                //pPlayer.getCooldowns().addCooldown(this,700);
                player = pPlayer;
            }








        }





        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {


        timeStopAbility.shootProjectile(entity, 300.0F);


        return super.onEntitySwing(stack, entity);

    }


}
