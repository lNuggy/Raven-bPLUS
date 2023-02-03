package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.main.Raven;
import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.DescriptionSetting;
import keystrokesmod.client.module.setting.impl.SliderSetting;
import keystrokesmod.client.module.setting.impl.TickSetting;
import keystrokesmod.client.utils.Utils;
import keystrokesmod.client.utils.ChatUtils;
import net.minecraft.client.settings.KeyBinding;

public class BHop extends Module {
   public static SliderSetting a;
   public static SliderSetting b;
   public static TickSetting c;
   public static SliderSetting d;
   public static SliderSetting mode;
   private int BHOP = 1;
   private int LOWHOP = 2;
   private int LEGIT = 3;
   private final double bspd = 0.0025D;
   public static DescriptionSetting dc;


   public BHop() {
      super("Bhop", ModuleCategory.movement);
      this.registerSetting(mode = new SliderSetting("Mode", 1.0D, 1.0D, 3.0D, 1.0D));
      this.registerSetting(a = new SliderSetting("Speed", 2.0D, 1.0D, 15.0D, 0.2D));
      this.registerSetting(b = new SliderSetting("Jump Height", 0.3, 0.05, 3.0D, 0.01D));
      this.registerSetting(c = new TickSetting("Damage Boost", true));
      this.registerSetting(d = new SliderSetting("Damage Boost Speed", 5.0D, 1.0D, 100.0D, 0.1D));
      this.registerSetting(dc = new DescriptionSetting(Utils.md + ""));
   }

   public void guiUpdate() {
      switch ((int) mode.getInput()) {
         case 1:
            dc.setDesc(Utils.md + "Bhop");
            break;
         case 2:
            dc.setDesc(Utils.md + "LowHop");
            break;
         case 3:
            dc.setDesc(Utils.md + "Legit");
            break;
      }
   }

   public void update() {
      Module fly = Raven.moduleManager.getModuleByClass(Fly.class);
      if (mode.getInput() == BHOP) {
         if (fly != null && !fly.isEnabled() && Utils.Player.isMoving() && !mc.thePlayer.isInWater()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
            mc.thePlayer.noClip = true;
            if (mc.thePlayer.onGround) {
               mc.thePlayer.jump();
            }

            mc.thePlayer.setSprinting(true);
            double spd = 0.0025D * a.getInput();
            double m = (float) (Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) + spd);
            Utils.Player.strafe(m);

            if (c.isToggled()) {
               if (Utils.Player.isPlayerInGame() && mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime) {
                  Utils.Player.strafe(d.getInput());
               }
            }
         }
      } else if (mode.getInput() == LOWHOP) {
         if (fly != null && !fly.isEnabled() && Utils.Player.isMoving() && !mc.thePlayer.isInWater()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
            mc.thePlayer.noClip = true;
            if (mc.thePlayer.onGround) {
               mc.thePlayer.motionY = b.getInput();
            }

            double spd = 0.0015D * a.getInput();
            double m = (float) (Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) + spd);
            Utils.Player.strafe(m);

            if (c.isToggled()) {
               if (Utils.Player.isPlayerInGame() && mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime) {
                  Utils.Player.strafe(d.getInput());
               }
            }
         }
      } else if (mode.getInput() == LEGIT) {
         if (fly != null && !fly.isEnabled() && Utils.Player.isMoving() && !mc.thePlayer.isInWater()) {
            if (mc.thePlayer.onGround) {
               mc.thePlayer.jump();
            }
            if (c.isToggled()) {
               if (Utils.Player.isPlayerInGame() && mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime) {
                  Utils.Player.strafe(d.getInput());
               }
            }
         }
      }
   }
}
