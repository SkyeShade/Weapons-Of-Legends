package net.skyeshade.wol.client.gui.screens.stats.spellguihandling;

import net.minecraft.resources.ResourceLocation;

public class SpellIconDeterminer {

    //determines spell icon textures depending on id
    //
    //
    //
    private static final ResourceLocation FIREBALLSPELLICON = new ResourceLocation("wol:textures/gui/fire_icon.png");

    private static final ResourceLocation WATERSPELLICON = new ResourceLocation("wol:textures/gui/water_icon.png");

    //private static final ResourceLocation FIREBALLSPELLICON = new ResourceLocation("wol:textures/gui/fire_icon.png");
    private static final ResourceLocation BLANK = new ResourceLocation("wol:textures/gui/empty.png");
    public static ResourceLocation determineSpellIconFromID (long spellId) {
        if (spellId == 1) {
            return FIREBALLSPELLICON;

        }
        if (spellId == 2) {
            return WATERSPELLICON;

        }
        return BLANK;
    }
}
