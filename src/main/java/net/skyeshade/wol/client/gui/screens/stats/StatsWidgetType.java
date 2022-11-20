package net.skyeshade.wol.client.gui.screens.stats;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum StatsWidgetType {
   OBTAINED(0),
   UNOBTAINED(1);

   private final int y;

   private StatsWidgetType(int pY) {
      this.y = pY;
   }

   public int getIndex() {
      return this.y;
   }
}