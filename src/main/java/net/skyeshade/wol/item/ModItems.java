package net.skyeshade.wol.item;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.item.custom.TimeStopSwordItem;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, WOL.MOD_ID);


    public static final RegistryObject<Item> KAGE_KOKOUY = ITEMS.register("kage_no_kokyu",
            () -> new TimeStopSwordItem(Tiers.NETHERITE, 4, -2f,
                    new Item.Properties().tab(ModCreativeModeTab.WEAPONS_OF_LEGENDS).durability(-1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
