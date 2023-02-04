package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.SliderSetting;
import keystrokesmod.client.module.setting.impl.TickSetting;
import keystrokesmod.client.utils.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.awt.Color;
import java.util.TimerTask;

public class BedAura extends Module {
   public static SliderSetting r;
   public static SliderSetting red;
   public static SliderSetting green;
   public static SliderSetting blue;
   public static TickSetting rainbow;
   public static TickSetting rotate;
   private java.util.Timer t;
   private BlockPos m = null;
   private final long per = 600L;

   public BedAura() {
      super("BedAura", ModuleCategory.player);
      this.registerSetting(rotate = new TickSetting("Rotate", true));
      this.registerSetting(r = new SliderSetting("Range", 5.0D, 2.0D, 10.0D, 1.0D));
      this.registerSetting(red = new SliderSetting("Red", 255.0D, 0.0D, 255.0D, 1.0D));
      this.registerSetting(green = new SliderSetting("Green", 0.0D, 0.0D, 255.0D, 1.0D));
      this.registerSetting(blue = new SliderSetting("Blue", 0.0D, 0.0D, 255.0D, 1.0D));
      this.registerSetting(rainbow = new TickSetting("Rainbow", false));
   }

   @SubscribeEvent
   public void onRenderWorldLast(RenderWorldLastEvent ev) {
      int rgb = rainbow.isToggled() ? Utils.Client.rainbowDraw(2L, 0L) : (new Color((int)red.getInput(), (int)green.getInput(), (int)blue.getInput())).getRGB();
      if (this.m != null) {
         Utils.HUD.drawDefaultBox(this.m, rgb, true);
      }
   }

   public void onEnable() {
      (this.t = new java.util.Timer()).scheduleAtFixedRate(this.t(), 0L, 600L);
   }

   public void onDisable() {
      if (this.t != null) {
         this.t.cancel();
         this.t.purge();
         this.t = null;
      }

      this.m = null;
   }

   public TimerTask t() {
      return new TimerTask() {
         public void run() {
            int ra = (int)BedAura.r.getInput();

            for(int y = ra; y >= -ra; --y) {
               for(int x = -ra; x <= ra; ++x) {
                  for(int z = -ra; z <= ra; ++z) {
                     if (Utils.Player.isPlayerInGame()) {
                        BlockPos p = new BlockPos(Module.mc.thePlayer.posX + (double) x, Module.mc.thePlayer.posY + (double) y, Module.mc.thePlayer.posZ + (double) z);
                        boolean bed = Module.mc.theWorld.getBlockState(p).getBlock() == Blocks.bed;
                        if (BedAura.this.m == p) {
                           if (!bed) {
                              BedAura.this.m = null;
                           }
                        } else if (bed) {
                           BedAura.this.mi(p);
                           BedAura.this.m = p;
                           break;
                        }
                     }
                  }
               }
            }

         }
      };
   }

   public void mi(BlockPos bed) {
      Vec3 playerPos = new Vec3(Module.mc.thePlayer.posX, Module.mc.thePlayer.posY + Module.mc.thePlayer.getEyeHeight(), Module.mc.thePlayer.posZ);
      Vec3 bedPos = new Vec3(bed.getX() + 0.5, bed.getY() + 0.5, bed.getZ() + 0.5);
      double xDiff = bedPos.xCoord - playerPos.xCoord;
      double yDiff = bedPos.yCoord - playerPos.yCoord;
      double zDiff = bedPos.zCoord - playerPos.zCoord;
      double yaw = Math.toDegrees(Math.atan2(zDiff, xDiff)) - 90;
      double pitch = Math.toDegrees(Math.atan2(yDiff, Math.sqrt(xDiff * xDiff + zDiff * zDiff)));
      Utils.Player.blockAim((float) yaw, (float) pitch, true);
   }
}