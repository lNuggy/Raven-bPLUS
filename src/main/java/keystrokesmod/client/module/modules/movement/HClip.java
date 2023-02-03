package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.SliderSetting;

public class HClip extends Module {
   public static SliderSetting a;

   public HClip() {
      super("VClip", ModuleCategory.movement);
      this.registerSetting(a = new SliderSetting("Distance", 2.0D, -10.0D, 10.0D, 0.5D));
   }

   public void onEnable() {
      if (a.getInput() != 0.0D) {
         mc.thePlayer.setPosition(mc.thePlayer.posX + a.getInput(), mc.thePlayer.posY, mc.thePlayer.posZ);
      }

      this.disable();
   }
}
