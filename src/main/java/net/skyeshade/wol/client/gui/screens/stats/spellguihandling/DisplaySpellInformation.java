package net.skyeshade.wol.client.gui.screens.stats.spellguihandling;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skyeshade.wol.client.ClientStatsData;
import net.skyeshade.wol.client.gui.screens.stats.StatsIcons;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.spellslots.UpdateSpellSlotsC2SPacket;

@OnlyIn(Dist.CLIENT)
public class DisplaySpellInformation extends Screen {


    //deals with anything displaying and handling spells inside elements
    //this does not have anything to do with casting, only setting spellslots
    //and displaying spells (desciptions and all)
    //
    //
    //
    //
    public DisplaySpellInformation() {
        super(GameNarrator.NO_TITLE);
    }
    SpellDesc spellDesc = new SpellDesc();
    private static final ResourceLocation SPELL_WINDOW_LOCATION = new ResourceLocation("wol:textures/gui/spell_window.png");
    private static final ResourceLocation SPELLSLOT = new ResourceLocation("wol:textures/gui/spellslot.png");
    private static final ResourceLocation BUTTON_RIGHT = new ResourceLocation("wol:textures/gui/button_right.png");
    private static final ResourceLocation BUTTON_RIGHT_HIGH= new ResourceLocation("wol:textures/gui/button_right_high.png");

    private static final ResourceLocation SPELLSLOT_HIGH = new ResourceLocation("wol:textures/gui/spellslot_high.png");
    private static final ResourceLocation EXIT = new ResourceLocation("wol:textures/gui/exit.png");
    private static final ResourceLocation EXIT_HIGH = new ResourceLocation("wol:textures/gui/exit_high.png");
    private static final ResourceLocation ICON_HIGH = new ResourceLocation("wol:textures/gui/icon_high.png");
    public boolean hoverExit = false;
    int size = 15;
    int spellSlotIndex;

    int page = 0;
    public boolean hoverNextPage = false;
    public boolean hoverPrevPage = false;
    public boolean spellWindow = false;
    public boolean hoverSpell = false;

    long spellID;

    public void displaySpellInformation(PoseStack pPoseStack, int pMouseX, int pMouseY, int pOffsetX, int pOffsetY, Font font) {
        if (spellID != 0) {
            if (spellWindow) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.enableBlend();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, SPELL_WINDOW_LOCATION);
                this.blit(pPoseStack, pOffsetX + 256 / 2 - 200 / 2 - 2, pOffsetY + 234 / 2 - 164 / 2 + 5, 0, 0, 200, 164);

                RenderSystem.setShaderTexture(0, SpellIconDeterminer.determineSpellIconFromID(spellID));
                blit(pPoseStack, pOffsetX + 256 / 2 - 200 /2, pOffsetY + 234 / 2 - 164 / 2 + 7, 0, 0, 26, 26, 26, 26);



                for (int i = 0; i < 9; i++) {
                    RenderSystem.setShaderTexture(0, SPELLSLOT);
                    blit(pPoseStack, pOffsetX + 250 / 2  - (size * 9) / 2 + i * size, pOffsetY + 234 / 2 + 164 - 29 - (size * 9) / 2, 0, 0, size, size, size, size);
                }
                if (hoverSpellSlotIndex(pMouseX, pMouseY, pOffsetX, pOffsetY) != -1) {
                    int index = hoverSpellSlotIndex(pMouseX, pMouseY, pOffsetX, pOffsetY) + 1;
                    RenderSystem.setShaderTexture(0, SPELLSLOT_HIGH);
                    blit(pPoseStack, pOffsetX + 250 / 2  - (size * 9) / 2 + index * size - size, pOffsetY + 234 / 2 + 164 - 29 - (size * 9) / 2, 0, 0, size, size, size, size);
                }
                for (int i = 0; i < 9; i++) {
                        RenderSystem.setShaderTexture(0, SpellIconDeterminer.determineSpellIconFromID(ClientStatsData.getPlayerSpellSlots()[i]));
                        blit(pPoseStack, pOffsetX + 252 / 2  - (size * 9) / 2 + i * size, pOffsetY + 236 / 2 + 164 - 29 - (size * 9) / 2, 0, 0, 13, 13, 13, 13);
                }
                RenderSystem.setShaderTexture(0, EXIT);
                blit(pPoseStack, pOffsetX + 215, pOffsetY + 42, 0, 0, 9, 9, 9, 9);
                if (StatsIcons.isMouseOver(pOffsetX + 214 - 3, pOffsetY + 42, pMouseX - 8, pMouseY - 4, 10, 10)) {
                    RenderSystem.setShaderTexture(0, EXIT_HIGH);
                    blit(pPoseStack, pOffsetX + 215, pOffsetY + 42, 0, 0, 9, 9, 9, 9);
                    hoverExit = true;
                } else {
                    hoverExit = false;
                }
                RenderSystem.setShaderTexture(0, BUTTON_RIGHT);
                if (page == 0) {

                    blit(pPoseStack, pOffsetX + 200, pOffsetY +185, 0, 0, 10, 15, 10, 15);
                    spellDesc.displayDesc(spellID,false, false,pPoseStack,pOffsetX,pOffsetY,0,0,font);
                    if (StatsIcons.isMouseOver(pOffsetX + 200-4, pOffsetY +185, pMouseX - 8, pMouseY - 7, 12, 17)) {
                        RenderSystem.setShaderTexture(0, BUTTON_RIGHT_HIGH);
                        blit(pPoseStack, pOffsetX + 200, pOffsetY +185, 0, 0, 10, 15, 10, 15);



                        hoverNextPage = true;
                    } else {
                        hoverPrevPage = false;
                        hoverNextPage = false;
                    }

                }else if (page == 1) {

                    blit(pPoseStack, pOffsetX + 41, pOffsetY +185, 0, 0, 10, 15, -10, 15);
                    spellDesc.displayDesc(spellID,false, true,pPoseStack,pOffsetX,pOffsetY,0,0,font);
                    if (StatsIcons.isMouseOver(pOffsetX + 41-4, pOffsetY +185, pMouseX - 8, pMouseY - 7, 12, 17)) {
                        RenderSystem.setShaderTexture(0, BUTTON_RIGHT_HIGH);
                        blit(pPoseStack, pOffsetX + 41, pOffsetY +185, 0, 0, 10, 15, -10, 15);

                        hoverPrevPage = true;
                    } else {
                        hoverPrevPage = false;
                        hoverNextPage = false;
                    }

                }




                spellSlotIndex = hoverSpellSlotIndex(pMouseX, pMouseY, pOffsetX, pOffsetY);
            }
        }
    }

    public void setSpellSlot(int buttonPressed) {
        if (buttonPressed == 0) {
            if (spellSlotIndex != -1) {
                ModMessages.sendToServer(new UpdateSpellSlotsC2SPacket(spellID,spellSlotIndex));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }else if (buttonPressed == 1) {
            if (spellSlotIndex != -1) {
                ModMessages.sendToServer(new UpdateSpellSlotsC2SPacket(0,spellSlotIndex));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }
    }

    public void mouseSpellClickEvent (int button) {
        if (spellWindow) {
            setSpellSlot(button);
            if (hoverExit) {
                if (button == 0) {
                    spellWindow = false;
                    spellID = 0;
                    page = 0;
                    hoverSpell = false;
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                }
            }
            if (hoverNextPage) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                page = 1;
            }
            if (hoverPrevPage) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                page = 0;
            }

        }
        if (hoverSpell && !spellWindow) {
            if (button == 0) {
                //System.out.println(spellWindow);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                spellWindow = true;
            }
        }
    }

    public int hoverSpellSlotIndex(int pMouseX, int pMouseY, int pOffsetX, int pOffsetY) {
        for (int i = 0; i < 9; i++) {
            if (StatsIcons.isMouseOver(pOffsetX+248/2-(size*9)/2+i*size,pOffsetY+238/2+164-29-(size*9)/2,pMouseX-8,pMouseY-5,size+2,size+2)) {
                return i;
            }
        }
        return -1;
    }
    public void displaySpell (long pSpellID, PoseStack pPoseStack, int dragOffsetX, int dragOffsetY, int displacementX, int displacementY,int pOffsetX, int pOffsetY, int pMouseX, int pMouseY, int WINDOW_INSIDE_WIDTH, int WINDOW_INSIDE_HEIGHT, Font font) {
        pPoseStack.pushPose();
        pPoseStack.translate(pOffsetX+9, pOffsetY+18, 0.0D);

        RenderSystem.enableDepthTest();
        RenderSystem.colorMask(false, false, false, false);
        fill(pPoseStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        pPoseStack.translate(0.0D, 0.0D, -950.0D);
        RenderSystem.depthFunc(518);
        fill(pPoseStack, 234, 207, 0, 0, -16777216);
        RenderSystem.depthFunc(515);
        pPoseStack.popPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SpellIconDeterminer.determineSpellIconFromID(pSpellID));
        blit(pPoseStack, dragOffsetX+displacementX-13+WINDOW_INSIDE_WIDTH,dragOffsetY+displacementY-13+WINDOW_INSIDE_HEIGHT,0,0,26,26,26,26);

        if (StatsIcons.isMouseOver(dragOffsetX + displacementX + WINDOW_INSIDE_WIDTH + pOffsetX + 26, dragOffsetY + displacementY + WINDOW_INSIDE_HEIGHT + pOffsetY + 26, pMouseX + 18, pMouseY + 9, 26, 26)) {
            if (!spellWindow) {

                RenderSystem.setShaderTexture(0, ICON_HIGH);
                blit(pPoseStack, dragOffsetX + displacementX - 13 + WINDOW_INSIDE_WIDTH, dragOffsetY + displacementY - 13 + WINDOW_INSIDE_HEIGHT, 0, 0, 26, 26, 26, 26);


                hoverSpell = true;
                spellID = pSpellID;
                spellDesc.displayDesc(spellID, true, false, pPoseStack, pOffsetX, pOffsetY, dragOffsetX+ displacementX + WINDOW_INSIDE_WIDTH, dragOffsetY + displacementY - 13 + WINDOW_INSIDE_HEIGHT, font);
            }
        }



    }
}
