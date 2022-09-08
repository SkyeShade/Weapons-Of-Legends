package net.skyeshade.wol.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.skyeshade.wol.abilities.ShootSlashProjectileAbility;
import net.skyeshade.wol.abilities.TimeStopAbility;

public class TimeStopSwordItem extends SwordItem {
    public TimeStopSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }


    TimeStopAbility timeStopAbility = new TimeStopAbility();
    ShootSlashProjectileAbility shootSlashProjectileAbility = new ShootSlashProjectileAbility();

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        shootSlashProjectileAbility.shootProjectile(pPlayer, 10, 200);
        timeStopAbility.stopTime(64, pPlayer, 200);

        return super.use(pLevel, pPlayer, pUsedHand);
    }


}
