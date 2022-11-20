package net.skyeshade.wol.client.gui.screens.stats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;

import static net.minecraft.client.gui.GuiComponent.blit;


public class StatsIcons {
    public boolean isMouseOver(int pX, int pY, int pMouseX, int pMouseY,int hitBoxX,int hitBoxY) {
            if (Math.abs(pMouseX-pX) < hitBoxX/2 && Math.abs(pMouseY-pY) < hitBoxY/2) {
                return true;
            }
            return false;
    }

}
