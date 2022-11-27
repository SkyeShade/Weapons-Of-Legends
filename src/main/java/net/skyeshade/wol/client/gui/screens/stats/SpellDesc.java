package net.skyeshade.wol.client.gui.screens.stats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class SpellDesc extends Screen {

    public SpellDesc() {
        super(GameNarrator.NO_TITLE);
    }

    public void displayDesc(long spellID, PoseStack pPoseStack, int pOffsetX, int pOffsetY, Font font) {

        if (spellID == 1) {
            String text = "Launches a basic fireball in the direction the caster is facing. Has a rather short casting time compared to mana cost and damage, and therefore is good as a general purpose combat spell.";
            font.draw(pPoseStack, "FireBall", pOffsetX + 55, pOffsetY + 51, 16753920);
            String[] stringArray = text.split(" ");
            String text2 = "";
            int line = 3;
            for (int index = 0; index < stringArray.length; index++){

                if (text2.length() + stringArray[index].length() < 37) {
                    if (text2.length() == 0) {
                        text2 = stringArray[index];
                    }else {
                        text2 = text2 + " " + stringArray[index];
                    }
                }else {
                    font.draw(pPoseStack, text2, pOffsetX + 55-26, pOffsetY + 44 + 9 * line-2, 16753920);
                    text2 = stringArray[index];
                    line++;
                }
                if (index == 32) {
                    font.draw(pPoseStack, text2, pOffsetX + 55-26, pOffsetY + 44 + 9 * line-2, 16753920);
                }
            }
        }
    }
}
