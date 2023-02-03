package keystrokesmod.client.module.modules.movement;

import com.google.common.eventbus.Subscribe;
import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.DescriptionSetting;
import keystrokesmod.client.module.setting.impl.SliderSetting;
import keystrokesmod.client.module.setting.impl.TickSetting;
import keystrokesmod.client.utils.ChatUtils;
import keystrokesmod.client.event.impl.PacketEvent;
import keystrokesmod.client.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public class Fly extends Module {
   private final Fly.VanFly vanFly = new VanFly();
   private final Fly.WeirdFly weirdFly = new WeirdFly();
   private final Fly.MotionYFly motionYFly = new MotionYFly();
   private final Fly.VulcanFly vulcanFly = new VulcanFly();
   public static DescriptionSetting dc;
   public static SliderSetting a;
   public static SliderSetting b;
   private static final String c1 = "Vanilla";
   private static final String c2 = "Weird Fly";
   private static final String c3 = "MotionY Fly";
   private static final String c4 = "Vulcan Fly";
   public static TickSetting c;
   public static SliderSetting f;

   public Fly() {
      super("Fly", ModuleCategory.movement);
      this.registerSetting(a = new SliderSetting("Value", 1.0D, 1.0D, 4.0D, 1.0D));
      this.registerSetting(dc = new DescriptionSetting(Utils.md + c1));
      this.registerSetting(b = new SliderSetting("Speed", 2.0D, 1.0D, 5.0D, 0.1D));
      this.registerSetting(f = new SliderSetting("Timer MotionY speed", 2.0D, 1.0D, 5.0D, 0.1D));
   }

   public void onEnable() {
      switch ((int) a.getInput()) {
         case 1:
            this.vanFly.onEnable();
            break;
         case 2:
            this.weirdFly.onEnable();
            break;
         case 3:
            this.motionYFly.onEnable();
            break;
         case 4:
            this.vulcanFly.onEnable();
            break;
      }

   }

   public void onDisable() {
      switch ((int) a.getInput()) {
         case 1:
            this.vanFly.onDisable();
            break;
         case 2:
            this.weirdFly.onDisable();
            break;
         case 3:
            this.motionYFly.onDisable();
            break;
         case 4:
            this.vulcanFly.onDisable();
      }

   }

   public void update() {
      switch ((int) a.getInput()) {
         case 1:
            this.vanFly.update();
            break;
         case 2:
            this.weirdFly.update();
            break;
         case 3:
            this.motionYFly.update();
            break;
         case 4:
            this.vulcanFly.update();
      }

   }

   public void guiUpdate() {
      switch ((int) a.getInput()) {
         case 1:
            dc.setDesc(Utils.md + c1);
            break;
         case 2:
            dc.setDesc(Utils.md + c2);
            break;
         case 3:
            dc.setDesc(Utils.md + c3);
            break;
         case 4:
            dc.setDesc(Utils.md + c4);
      }

   }

   class MotionYFly {
      int ticksPassed = 0;
      Double startY = null;

      public void onEnable() {
         int ticksPassed = 0;
         Double startY = null;
         Utils.Client.getTimer().timerSpeed = (float)f.getInput();
      }

      public void onDisable() {
         int ticksPassed = 0;
         Utils.Client.resetTimer();
      }

      public void onTick(TickEvent.ClientTickEvent event) {
         ticksPassed++;
      }

      public void update() {
         mc.thePlayer.onGround = true;
         if (Utils.Player.playerOverAir()) {
            mc.thePlayer.motionY = 0.0D;
         }
      }
   }

   class VulcanFly {
      Double startY = null;

      int ticksPassed = 0;

      public void onEnable() {
         startY = mc.thePlayer.posY;
      }

      public void onDisable() {
         if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() == Blocks.air) {
            startY = mc.thePlayer.posY - 1;
            while (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, startY - 1, mc.thePlayer.posZ)).getBlock() == Blocks.air) {
               startY--;
            }
         } else {
            startY = mc.thePlayer.posY;
         }
         mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                 mc.thePlayer.posX, startY + 1, mc.thePlayer.posZ, true
         ));
         startY = null;
         ticksPassed = 0;
      }

      public void onTick(TickEvent.ClientTickEvent event) {
         ticksPassed++;
         if (ticksPassed % 1200 == 0 && startY != null) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY + 5, mc.thePlayer.posZ, true
            ));
         }
      }

      public void update() {
         if (startY != null) {
            double newX = mc.thePlayer.posX + 10;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, newX, mc.thePlayer.posZ, true
            ));
         }
      }
   }



   class WeirdFly {
      int ticksPassed = 0;
      Double startY = null;

      public void onEnable() {
         int inc = 0;
         int ticksPassed = 0;
      }

      public void onDisable() {
         int ticksPassed = 0;
      }

      public void onTick(TickEvent.ClientTickEvent event) {
         ticksPassed++;
      }


      private int inc = 0;

      public void update() {
         inc++;
         int result = (inc % 2 == 0) ? 2 : 1;
         ChatUtils.sendMessage(String.valueOf(result));
         mc.thePlayer.onGround = true;
         if (Utils.Player.playerOverAir()) {
            if (result == 1) {
               mc.thePlayer.motionY = 0.7D;
            } else {
               mc.thePlayer.motionY = -0.7D;
            }
         }
      }
   }

   static class VanFly {
      private final float dfs = 0.05F;

      public void onEnable() {}

      public void onDisable() {
         if(Minecraft.getMinecraft().thePlayer== null)
            return;

         if (Minecraft.getMinecraft().thePlayer.capabilities.isFlying) {
            Minecraft.getMinecraft().thePlayer.capabilities.isFlying = false;
         }

         Minecraft.getMinecraft().thePlayer.capabilities.setFlySpeed(0.05F);
      }

      public void update() {
         Module.mc.thePlayer.motionY = 0.0D;
         Module.mc.thePlayer.capabilities.setFlySpeed((float)(0.05000000074505806D * Fly.b.getInput()));
         Module.mc.thePlayer.capabilities.isFlying = true;
      }
   }
}
