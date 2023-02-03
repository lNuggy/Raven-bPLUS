package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.SliderSetting;
import keystrokesmod.client.module.setting.impl.DescriptionSetting;
import keystrokesmod.client.module.setting.impl.TickSetting;
import keystrokesmod.client.utils.Utils;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import keystrokesmod.client.utils.ChatUtils;
public class JumpReset extends Module {

    public static SliderSetting a;
    public static TickSetting b;
    public static TickSetting c;
    public static SliderSetting mode;
    public static SliderSetting height;
    public static DescriptionSetting dc;
    public static final int VANILLA_MODE = 1;
    public static final int MOTION_MODE = 2;

    public JumpReset() {
        super("JumpReset", ModuleCategory.combat);
        this.registerSetting(mode = new SliderSetting("Mode", VANILLA_MODE, VANILLA_MODE, MOTION_MODE, 1.0D));
        this.registerSetting(a = new SliderSetting("Motion", 90.0D, 0.0D, 100.0D, 1.0D));
        this.registerSetting(b = new TickSetting("AirMotion", true));
        this.registerSetting(c = new TickSetting("Debug", true));
        this.registerSetting(height = new SliderSetting("AirHeight", 0.18D, 0.01D, 0.75D, 0.01));
        this.registerSetting(dc = new DescriptionSetting("Mode: " + mode.getInput()));
    }

    public void guiUpdate() {
        switch ((int) mode.getInput()) {
            case VANILLA_MODE:
                dc.setDesc("Mode: Vanilla");
                break;
            case MOTION_MODE:
                dc.setDesc("Mode: Motion");
                break;
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent ev) {
        if (Utils.Player.isPlayerInGame() && mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime) {
            if ((int) mode.getInput() == MOTION_MODE) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = a.getInput() / 25;
                    if (c.isToggled()) {
                        double inputValue = a.getInput();
                        double motion = inputValue / 25;
                        String motionString = Double.toString(motion);
                        ChatUtils.sendMessage(motionString);
                    }

                } else {
                    if (b.isToggled()) {
                        mc.thePlayer.motionY = height.getInput();
                        if (c.isToggled()) {
                            ChatUtils.sendMessage("Motion-ing by" + height + "in air");
                        }
                    }
                }
            } else if ((int) mode.getInput() == VANILLA_MODE) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    if (c.isToggled()) {
                        ChatUtils.sendMessage("Jumped");
                    }
                } else {
                    if (b.isToggled()) {
                        mc.thePlayer.motionY = height.getInput();
                        if (c.isToggled()) {
                            ChatUtils.sendMessage("Motion-ing by" + height + "in air");
                        }
                    }
                }
            }
        }
    }
}