package net.skyeshade.wol.client.gui.screens.stats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.skyeshade.wol.client.ClientStatsData;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.spellslots.UpdateSpellSlotsC2SPacket;
import net.skyeshade.wol.networking.packet.spellslots.UpdateSpellSlotsToggleC2SPacket;

public class DisplaySpellInformation extends Screen {
    public DisplaySpellInformation() {
        super(GameNarrator.NO_TITLE);
    }
    private static final ResourceLocation SPELL_WINDOW_LOCATION = new ResourceLocation("wol:textures/gui/spell_window.png");
    private static final ResourceLocation SPELLSLOT = new ResourceLocation("wol:textures/gui/spellslot.png");
    private static final ResourceLocation SPELLSLOT_HIGH = new ResourceLocation("wol:textures/gui/spellslot_high.png");
    private static final ResourceLocation EXIT = new ResourceLocation("wol:textures/gui/exit.png");
    private static final ResourceLocation EXIT_HIGH = new ResourceLocation("wol:textures/gui/exit_high.png");
    public boolean hoverExit = false;
    int size = 15;
    int spellSlotIndex;

    long pSpellID;

    public void displaySpellInformation(long spellID, PoseStack pPoseStack, int pMouseX, int pMouseY, int pOffsetX, int pOffsetY) {
        if (spellID != 0) {
            pSpellID = spellID;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, SPELL_WINDOW_LOCATION);
            this.blit(pPoseStack, pOffsetX+256/2-200/2-3, pOffsetY+234/2-164/2+5, 0, 0, 200, 164);


            for (int i = 0; i < 9; i++) {
                RenderSystem.setShaderTexture(0, SPELLSLOT);
                blit(pPoseStack, pOffsetX+250/2-1-(size*9)/2+i*size,pOffsetY+234/2+164-29-(size*9)/2,0,0,size,size,size,size);

            }
            if (hoverSpellSlotIndex(pMouseX,pMouseY,pOffsetX,pOffsetY) != -1) {
                int index = hoverSpellSlotIndex(pMouseX,pMouseY,pOffsetX,pOffsetY)+1;
                RenderSystem.setShaderTexture(0, SPELLSLOT_HIGH);
                blit(pPoseStack, pOffsetX+250/2-1-(size*9)/2+index*size-size,pOffsetY+234/2+164-29-(size*9)/2,0,0,size,size,size,size);
            }


            for (int i = 0; i < 9; i++) {
                if (ClientStatsData.getPlayerSpellSlots()[i] == spellID) {

                    RenderSystem.setShaderTexture(0, SpellIconDeterminer.determineSpellIconFromID(spellID));
                    blit(pPoseStack, pOffsetX+252/2-1-(size*9)/2+i*size,pOffsetY+236/2+164-29-(size*9)/2,0,0,13,13,13,13);
                }

            }
            RenderSystem.setShaderTexture(0, EXIT);
            blit(pPoseStack, pOffsetX+214,pOffsetY+42,0,0,9,9,9,9);
            if (StatsIcons.isMouseOver(pOffsetX+214-4,pOffsetY+42,pMouseX-8,pMouseY-4,10,10)) {
                RenderSystem.setShaderTexture(0, EXIT_HIGH);
                blit(pPoseStack, pOffsetX+214,pOffsetY+42,0,0,9,9,9,9);
                hoverExit = true;
            }else {
                hoverExit = false;
            }




            spellSlotIndex = hoverSpellSlotIndex(pMouseX,pMouseY,pOffsetX,pOffsetY);



        }
    }
    public void setSpellSlot(int buttonPressed) {
        if (buttonPressed == 0) {
            if (spellSlotIndex != -1) {
                ModMessages.sendToServer(new UpdateSpellSlotsC2SPacket(pSpellID,spellSlotIndex));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }else if (buttonPressed == 1) {
            if (spellSlotIndex != -1) {
                ModMessages.sendToServer(new UpdateSpellSlotsC2SPacket(0,spellSlotIndex));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }

    }

    public boolean mouseClickEvent (int button) {
        setSpellSlot(button);
        if (hoverExit) {
            //System.out.println(spellWindow);
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return false;
        }
        return true;
    }

    public int hoverSpellSlotIndex(int pMouseX, int pMouseY, int pOffsetX, int pOffsetY) {
        for (int i = 0; i < 9; i++) {
            if (StatsIcons.isMouseOver(pOffsetX+248/2-1-(size*9)/2+i*size,pOffsetY+238/2+164-29-(size*9)/2,pMouseX-8,pMouseY-5,size+2,size+2)) {
                return i;
            }

        }
        return -1;
    }
}
