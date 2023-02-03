package keystrokesmod.client.module.modules.combat;

import java.util.Iterator;

import keystrokesmod.client.clickgui.raven.ClickGui;
import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.modules.world.AntiBot;
import keystrokesmod.client.module.setting.impl.DescriptionSetting;
import keystrokesmod.client.module.setting.impl.SliderSetting;
import keystrokesmod.client.module.setting.impl.TickSetting;
import keystrokesmod.client.utils.Utils;
import keystrokesmod.client.utils.fonts.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RodAimbot extends Module {
    public static SliderSetting a;
    public static SliderSetting b;
    public static SliderSetting d;
    public static TickSetting c;

    public RodAimbot() {
        super("RodAimbot", Module.ModuleCategory.combat);
        this.registerSetting(a = new SliderSetting("FOV", 90.0D, 15.0D, 360.0D, 1.0D));
        this.registerSetting(b = new SliderSetting("Distance", 4.5D, 1.0D, 30.0D, 0.5D));
        this.registerSetting(d = new SliderSetting("Height", -7, -70, 70.0D, 7D));
        this.registerSetting(c = new TickSetting("Aim invisible", false));
    }

    @SubscribeEvent
    public void x(MouseEvent ev) {
        if (ev.button == 1 && ev.buttonstate && Utils.Player.isPlayerInGame() && mc.currentScreen == null) {
            if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFishingRod && mc.thePlayer.fishEntity == null) {
                Entity en = this.gE();
                if (en != null) {
                    ev.setCanceled(true);
                    Utils.Player.aim(en, (int) -d.getInput(), true);
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                }
            }

        }
    }

    public Entity gE() {
        int f = (int)a.getInput();
        Iterator var2 = mc.theWorld.playerEntities.iterator();

        EntityPlayer en;
        do {
            do {
                do {
                    do {
                        if (!var2.hasNext()) {
                            return null;
                        }

                        en = (EntityPlayer)var2.next();
                    } while(en == mc.thePlayer);
                } while(en.deathTime != 0);
            } while(!c.isToggled() && en.isInvisible());
        } while((double)mc.thePlayer.getDistanceToEntity(en) > b.getInput() || AntiBot.bot(en) || !Utils.Player.isEntityInFOV(en, (float)f));

        return en;
    }
}