package keystrokesmod.client.module.modules.movement;

import com.google.common.eventbus.Subscribe;
import keystrokesmod.client.event.impl.PacketEvent;
import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.DescriptionSetting;
import keystrokesmod.client.module.setting.impl.SliderSetting;
import keystrokesmod.client.module.setting.impl.TickSetting;
import net.minecraft.network.play.server.S30PacketWindowItems;

public class NoSlow extends Module {
   public static DescriptionSetting a;
   public static DescriptionSetting dc;
   public static SliderSetting b;
   public static SliderSetting mode;
   public static final int VANILLA_MODE = 1;
   public static final int WATCHDOG_MODE = 2;
   private static TickSetting sprint;

   public NoSlow() {
      super("NoSlow", ModuleCategory.movement);
      this.registerSetting(mode = new SliderSetting("Mode", VANILLA_MODE, VANILLA_MODE, WATCHDOG_MODE, 1.0D));
      this.registerSetting(dc = new DescriptionSetting("Mode:" + mode.getInput()));
      this.registerSetting(sprint = new TickSetting("Sprint", true));
      this.registerSetting(b = new SliderSetting("Slow %", 80.0D, 0.0D, 80.0D, 1.0D));
   }

   public static void sl() {
      float val = (100.0F - (float)b.getInput()) / 100.0F;
      mc.thePlayer.movementInput.moveStrafe *= val;
      mc.thePlayer.movementInput.moveForward *= val;
      if (mc.thePlayer.isSprinting() || sprint.isToggled()) {
         mc.thePlayer.movementInput.moveStrafe /= val;
         mc.thePlayer.movementInput.moveForward /= val;
      }
   }

   @Subscribe
   public void onPacket(PacketEvent e) {
      mc.thePlayer.setSprinting(sprint.isToggled());
      if ((int)mode.getInput() == WATCHDOG_MODE) {
         if (e.getPacket() instanceof S30PacketWindowItems) {
            if (mc.thePlayer.isUsingItem()) {
               e.cancel();
            }
         }
      } else if ((int)mode.getInput() == VANILLA_MODE) {
         sl();
      }
   }

   public void guiUpdate() {
      switch((int)mode.getInput()) {
         case VANILLA_MODE:
            dc.setDesc("Mode: Vanilla");
            break;
         case WATCHDOG_MODE:
            dc.setDesc("Mode: Watchdog");
      }
   }
}