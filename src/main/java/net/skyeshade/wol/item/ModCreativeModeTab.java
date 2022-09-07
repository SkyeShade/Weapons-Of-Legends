package net.skyeshade.wol.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab WEAPONS_OF_LEGENDS = new CreativeModeTab("weapons_of_legends") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.KAGE_KOKOUY.get());
        }
    };
}
