package net.skyeshade.wol.abilities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ShootSlashProjectileAbility {



    static Player caster;

    static int tickCounter = 0;

    @SubscribeEvent
    public static void onServerTick (TickEvent.ServerTickEvent event) {


        if (tickCounter % 5 == 0 && tickCounter > 1)
        {
            if(caster != null) {
                if (!caster.level.isClientSide()){
                    AbstractArrow arrow = new AbstractArrow(EntityType.ARROW, caster, caster.level) {
                        @Override
                        protected ItemStack getPickupItem() {
                            return null;
                        }
                    };
                    arrow.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0.0F, 2 * 3.0F, 1.0F);
                    caster.level.addFreshEntity(arrow);
                }
            }

        }


        tickCounter--;

    }






    public void shootProjectile (Player player, int speed, int ticks) {

        tickCounter = ticks;
        /*
        if (!player.level.isClientSide()){
            AbstractArrow arrow = new AbstractArrow(EntityType.ARROW, player, player.level) {
                @Override
                protected ItemStack getPickupItem() {
                    return null;
                }
            };
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2 * 3.0F, 1.0F);
            player.level.addFreshEntity(arrow);
        }
        */
    }
}
