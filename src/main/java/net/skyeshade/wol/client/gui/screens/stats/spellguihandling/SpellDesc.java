package net.skyeshade.wol.client.gui.screens.stats.spellguihandling;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skyeshade.wol.client.ClientStatsData;
import net.skyeshade.wol.util.SpellStatRegistering;

@OnlyIn(Dist.CLIENT)
public class SpellDesc extends Screen {

    // formats and displays descriptions per spell id
    //
    //
    //
    //

    //TODO: add proper descriptions
    public SpellDesc() {
        super(GameNarrator.NO_TITLE);
    }

    public void displayDesc(long spellID, boolean title, boolean page2, PoseStack pPoseStack, int pOffsetX, int pOffsetY,int totalDragOffsetX,int totalDragOffsetY, Font font) {
        String text = "";
        if (spellID == 1) {
            if (title) {
                drawCenteredString(pPoseStack,font,"FireBall",totalDragOffsetX,totalDragOffsetY-9,16753920);
            }else {
                font.draw(pPoseStack, "FireBall", pOffsetX + 56, pOffsetY + 51, 16753920);
                if (!page2) {
                    text = "Launches a basic fireball in the direction the caster is facing. Has a rather short casting time compared to mana cost and damage, and therefore is good as a general purpose combat spell.";
                }
            }

        }
        if (spellID == 2) {
            if (title) {
                drawCenteredString(pPoseStack,font,"Test",totalDragOffsetX,totalDragOffsetY-9,16753920);
            }else {
                font.draw(pPoseStack, "Test", pOffsetX + 56, pOffsetY + 51, 16753920);
                if (!page2) {
                    text = "Placeholder Text.";
                }
            }

        }
        if (page2) {
            long baseDamage = SpellStatRegistering.getSpellController(spellID).getBaseDamage();
            long absoluteDamage = SpellStatRegistering.getSpellController(spellID).getAbsoluteDamage();

            long manaCost = SpellStatRegistering.getSpellController(spellID).getBaseManaCost();
            long absoluteManaCost = SpellStatRegistering.getSpellController(spellID).getAbsoluteManaCost();

            long baseCastingTime = SpellStatRegistering.getSpellController(spellID).getBaseCastingTime();
            long absoluteCastingTime = SpellStatRegistering.getSpellController(spellID).getAbsoluteCastingTime();


            drawCenteredString(pPoseStack,font,"Damage: "+ baseDamage + " + " + (absoluteDamage-baseDamage),pOffsetX + 56-30+100,pOffsetY + 60 + 18,16753920);

            if (absoluteManaCost-manaCost < 0){
                drawCenteredString(pPoseStack,font,"Mana Cost: "+ manaCost  + " " + (absoluteManaCost-manaCost),pOffsetX + 56-30+100,pOffsetY + 60 + 18*2,16753920);
            }else {
                drawCenteredString(pPoseStack,font,"Mana Cost: "+ manaCost  + " + " + (absoluteManaCost-manaCost),pOffsetX + 56-30+100,pOffsetY + 60 + 18*2,16753920);
            }

            drawCenteredString(pPoseStack,font,"Power: "+ ClientStatsData.getPlayerSpellPowerLevel()[(int)spellID],pOffsetX + 56-30+100,pOffsetY + 60 + 18*4,16753920);


            if (absoluteCastingTime-baseCastingTime < 0) {
                drawCenteredString(pPoseStack,font,"Casting Time: "+ (baseCastingTime*1000)/20 + " " + (absoluteCastingTime-baseCastingTime)*1000/20 + "ms",pOffsetX + 56-30+100,pOffsetY + 60 + 18*3,16753920);
            }else {
                drawCenteredString(pPoseStack,font,"Casting Time: "+ (baseCastingTime*1000)/20 + " + " + (absoluteCastingTime-baseCastingTime)*1000/20 + "ms",pOffsetX + 56-30+100,pOffsetY + 60 + 18*3,16753920);
            }



        }
        String[] stringArray = text.split(" ");
        String text2 = "";
        int line = 3;
        for (int index = 0; index < stringArray.length; index++){

            if (text2.length() + stringArray[index].length() < 36) {
                if (text2.length() == 0) {
                    text2 = stringArray[index];
                }else {
                    text2 = text2 + " " + stringArray[index];
                }
            }else {
                drawCenteredString(pPoseStack,font,text2,pOffsetX + 56-30+100,pOffsetY + 47 + 9 * line-2,16753920);
                text2 = stringArray[index];
                line++;
            }
            if (index == stringArray.length-1) {
                drawCenteredString(pPoseStack,font,text2,pOffsetX + 56-30+100,pOffsetY + 47 + 9 * line-2,16753920);
            }
        }
    }
}
