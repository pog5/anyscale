package me.pog5.anyscale.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.Window;
import me.pog5.anyscale.client.AnyscaleClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Window.class)
public class WindowMixin {

    @Shadow
    private int framebufferWidth;

    @Shadow
    private int framebufferHeight;

    @Shadow
    private int guiScaledWidth;

    @Shadow
    private int guiScaledHeight;

    @WrapMethod(method = "getGuiScale")
    int except(Operation<Integer> original) throws Exception {
        var e = new IllegalAccessException("Something tried to get the GUI scale without Anyscale!");
        e.printStackTrace(System.err);
        return original.call();
    }

    @WrapMethod(method = "setGuiScale")
    void setGuiScale(int i, Operation<Void> original) {
        var trueScale = AnyscaleClient.config.base_scale;
//        var trueScale = Minecraft.getInstance().options.guiScale().get();
        guiScaledWidth = (int) (framebufferWidth / trueScale);
        guiScaledHeight = (int) (framebufferHeight / trueScale);
//        original.call(i);
    }
}
