package keystrokesmod.client.module.modules.render;

import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.DescriptionSetting;
import keystrokesmod.client.module.setting.impl.SliderSetting;
import keystrokesmod.client.module.setting.impl.TickSetting;
import keystrokesmod.client.utils.fonts.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public class Health extends Module {
    public static SliderSetting yPosition;

    public Health() {
        super("Health", ModuleCategory.render);
        this.registerSetting(yPosition = new SliderSetting("Y Position", 0.5, 0, 1, 0.05));
    }

    @SubscribeEvent
    public void r(TickEvent.RenderTickEvent e) {
        if (mc.currentScreen == null) {
            ScaledResolution res = new ScaledResolution(mc);
            int width = res.getScaledWidth();
            int height = res.getScaledHeight();

            String t = mc.thePlayer.getHealth() + "/" + mc.thePlayer.getMaxHealth();
            int x = width / 2 - mc.fontRendererObj.getStringWidth(t) / 2;
            int y = (int)(height * yPosition.getInput());

            int rgb = 0x00FF00;
            if (mc.thePlayer.getHealth() <= 5) {
                rgb = 0xff0000;
            } else if (mc.thePlayer.getHealth() <= 10) {
                rgb = 0xffa500;
            }

            mc.fontRendererObj.drawStringWithShadow(t, x, y, rgb);
        }
    }
}