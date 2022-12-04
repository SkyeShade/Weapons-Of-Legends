package net.skyeshade.wol.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    //TODO: proper key names
    public static final String KEY_CATEGORY_WOL = "key.category.wol.tutorial";
    public static final String KEY_SPELLSLOTS_TOGGLE = "key.wol.spellslottoggle";

    public static final String KEY_ADD_MAXMANA = "key.wol.addmaxmana";
    
    public static final KeyMapping SPELLSLOTS_TOGGLE_KEY = new KeyMapping(KEY_SPELLSLOTS_TOGGLE, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, KEY_CATEGORY_WOL);

    public static final KeyMapping MAXMANAADD_KEY = new KeyMapping(KEY_ADD_MAXMANA, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, KEY_CATEGORY_WOL);
}