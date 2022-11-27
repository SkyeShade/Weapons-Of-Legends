package net.skyeshade.wol.client.gui.screens.stats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.skyeshade.wol.client.ClientStatsData;

import static net.minecraft.client.gui.GuiComponent.blit;


public class StatsIcons {
    public static boolean isMouseOver(int pX, int pY, int pMouseX, int pMouseY,int hitBoxX,int hitBoxY) {
            if (Math.abs(pMouseX-pX) < hitBoxX/2 && Math.abs(pMouseY-pY) < hitBoxY/2) {
                return true;
            }
            return false;
    }
    public static int getBarPVOffset(long corelevel) {

        if(corelevel == 1){
            return 10;
        }else if (corelevel <= 4 && corelevel >=2){
            return 20;
        }else if (corelevel <= 7 && corelevel >=5){
            return 30;
        }else if (corelevel <= 10 && corelevel >=8){
            return 40;
        } else if (corelevel <= 13 && corelevel >=11){
            return 50;
        } else {
            return 60;
        }
    }



}
