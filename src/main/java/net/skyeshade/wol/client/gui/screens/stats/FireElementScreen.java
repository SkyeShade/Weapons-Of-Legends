package net.skyeshade.wol.client.gui.screens.stats;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class FireElementScreen extends Screen {
   private static final ResourceLocation WINDOW_LOCATION = new ResourceLocation("wol:textures/gui/window.png");
   private static final ResourceLocation WINDOW_ARROWBACK = new ResourceLocation("wol:textures/gui/arrow_back.png");

   private static final ResourceLocation EXIT = new ResourceLocation("wol:textures/gui/exit.png");
   private static final ResourceLocation EXIT_HIGH = new ResourceLocation("wol:textures/gui/exit_high.png");

   private static final ResourceLocation WINDOW_ARROWBACK_HIGHLIGHTED = new ResourceLocation("wol:textures/gui/arrow_back_high.png");

   private static final ResourceLocation ICON_HIGH = new ResourceLocation("wol:textures/gui/icon_high.png");
   private static final ResourceLocation WINDOW_BACKGROUND = new ResourceLocation("wol:textures/gui/dark.png");

   private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/advancements/widgets.png");
   public static final int WINDOW_WIDTH = 252;
   public static final int WINDOW_HEIGHT = 234;
   private static final int WINDOW_INSIDE_X = 9;
   private static final int WINDOW_INSIDE_Y = 18;
   public static final int WINDOW_INSIDE_WIDTH = 234;
   public static final int WINDOW_INSIDE_HEIGHT = 207;
   private static final int WINDOW_TITLE_X = 8;
   private static final int WINDOW_TITLE_Y = 6;
   public static final int BACKGROUND_TILE_WIDTH = 16;
   public static final int BACKGROUND_TILE_HEIGHT = 16;
   public static final int BACKGROUND_TILE_COUNT_X = 14;
   public static final int BACKGROUND_TILE_COUNT_Y = 7;


   public boolean hoverArrowBack = false;
   public boolean hoverFireBallSpell = false;

   private int spellSlotHoverIndex;

   public long hoverSpellID = 0;


   DisplaySpellInformation displaySpellInformation = new DisplaySpellInformation();

   //StatsIcons statsIcons = new StatsIcons();
   private double scrollX;
   private double scrollY;

   //private final StatsWidget root;
   private boolean centered;


   @Nullable
   //private StatsTab selectedTab;
   private boolean isScrolling;


   public FireElementScreen() {
      super(GameNarrator.NO_TITLE);
   }



   protected void init() {

   }

   public void removed() {

   }

   public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

      if (pButton == 0) {
         int i = (this.width - 252) / 2;
         int j = (this.height - 140) / 2;
         if (hoverArrowBack) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            Minecraft.getInstance().setScreen(new StatsScreen());
         }


      }
      displaySpellInformation.mouseSpellClickEvent(pButton);
      return super.mouseClicked(pMouseX, pMouseY, pButton);

   }






   public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
       int i = (this.width - WINDOW_WIDTH) / 2;
       int j = (this.height - WINDOW_HEIGHT) / 2;
       this.renderBackground(pPoseStack);
       this.renderInside(pPoseStack, pMouseX, pMouseY, i, j);


       this.renderWindow(pPoseStack, pMouseX, pMouseY, i, j);

       displaySpellInformation.displaySpellInformation(pPoseStack, pMouseX, pMouseY, i, j, this.font);


   }




   private void renderInside(PoseStack pPoseStack, int pMouseX, int pMouseY, int pOffsetX, int pOffsetY) {

       PoseStack posestack = RenderSystem.getModelViewStack();
       posestack.pushPose();
       posestack.translate((double)(pOffsetX + 9), (double)(pOffsetY + 18), 0.0D);
       RenderSystem.applyModelViewMatrix();

       posestack.popPose();
       RenderSystem.applyModelViewMatrix();
       RenderSystem.depthFunc(515);
       RenderSystem.disableDepthTest();



         ///////
      if (!this.centered) {
         this.scrollX = (-117.0D );
         this.scrollY = (-103.5D );
         this.centered = true;
      }


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
      ResourceLocation resourcelocation = WINDOW_BACKGROUND;
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderTexture(0, resourcelocation);

      int i = Mth.floor(this.scrollX);
      int j = Mth.floor(this.scrollY);
      int k = i % 16;
      int l = j % 16;

      for(int i1 = -1; i1 <= 15; ++i1) {
         for(int j1 = -1; j1 <= 15; ++j1) {
            blit(pPoseStack, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
         }
      }

      RenderSystem.setShader(GameRenderer::getPositionTexShader);



      displaySpellInformation.hoverSpell = false;
      int fireBallSpellDisplacementX = 0;
      int fireBallSpellDisplacementY = 0;

      displaySpellInformation.displaySpell(1,pPoseStack,i,j,fireBallSpellDisplacementX,fireBallSpellDisplacementY,pOffsetX,pOffsetY,pMouseX,pMouseY,WINDOW_INSIDE_WIDTH,WINDOW_INSIDE_HEIGHT);


      int waterSpellDisplacementX = 26;
      int waterSpellDisplacementY = 0;

      displaySpellInformation.displaySpell(2,pPoseStack,i,j,waterSpellDisplacementX,waterSpellDisplacementY,pOffsetX,pOffsetY,pMouseX,pMouseY,WINDOW_INSIDE_WIDTH,WINDOW_INSIDE_HEIGHT);



       pPoseStack.popPose();

   }

   public void renderWindow(PoseStack pPoseStack, int pMouseX, int pMouseY, int pOffsetX, int pOffsetY) {

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderTexture(0, WINDOW_LOCATION);
      this.blit(pPoseStack, pOffsetX, pOffsetY, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);


      if (StatsIcons.isMouseOver(pOffsetX+6,pOffsetY+5,pMouseX-8,pMouseY-5,20,12)) {
         RenderSystem.setShaderTexture(0, WINDOW_ARROWBACK_HIGHLIGHTED);
         blit(pPoseStack, pOffsetX+6,pOffsetY+5,0,0,18,10,18,10);
         hoverArrowBack = true;
      }else {
         RenderSystem.setShaderTexture(0, WINDOW_ARROWBACK);
         blit(pPoseStack, pOffsetX+6,pOffsetY+5,0,0,18,10,18,10);
         hoverArrowBack = false;
      }





      this.font.draw(pPoseStack, "Fire", (float)(pOffsetX -1-19/2+ WINDOW_WIDTH/2), (float)(pOffsetY + 6), 16753920);

   }
   public void renderOnTopSpellInfo(PoseStack pPoseStack, int pMouseX, int pMouseY, int pOffsetX, int pOffsetY) {

    }

   public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
      if (pButton != 0) {
         this.isScrolling = false;
         return false;
      } else {
         if (!this.isScrolling) {
            this.isScrolling = true;
         } else {
            this.scroll(pDragX, pDragY);
         }
         return true;
      }
   }
   public void scroll(double pDragX, double pDragY) {
        if (!displaySpellInformation.spellWindow) {
            this.scrollX = Mth.clamp(this.scrollX + pDragX, -234.0D, 0.0D);
            this.scrollY = Mth.clamp(this.scrollY + pDragY, -207.0D, 0.0D);
        }

   }





}
