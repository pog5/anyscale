package me.pog5.anyscale.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pog5.anyscale.client.config.AnyscaleConfig;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import squeek.appleskin.client.HUDOverlayHandler;

@Mixin(value = HUDOverlayHandler.class, priority = 1055)
public class AppleSkinMixin {

    @WrapOperation(method = "onPreRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getScaledWidth()I"))
    private int Anyscale$appleskinPreWidth(Window instance, Operation<Integer> original) {
        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
        return (int) ((float) original.call(instance) * (1.0F / scale));
    }
    @WrapOperation(method = "onPreRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getScaledHeight()I"))
    private int Anyscale$appleskinPreHeight(Window instance, Operation<Integer> original) {
        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
        return (int) ((float) original.call(instance) * (1.0F / scale));
    }

    @WrapOperation(method = "onRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getScaledWidth()I"))
    private int Anyscale$appleskinWidth(Window instance, Operation<Integer> original) {
        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
        return (int) ((float) original.call(instance) * (1.0F / scale));
    }
    @WrapOperation(method = "onRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getScaledHeight()I"))
    private int Anyscale$appleskinHeight(Window instance, Operation<Integer> original) {
        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
        return (int) ((float) original.call(instance) * (1.0F / scale));
    }

//    @Redirect(method = "onRender", at = @At(value = "INVOKE", target = "Lsqueek/appleskin/client/HUDOverlayHandler;drawHealthOverlay(Lsqueek/appleskin/api/event/HUDOverlayEvent$HealthRestored;Lnet/minecraft/client/MinecraftClient;F)V"))
//    @Redirect(method = "onRender", at = @At(value = "INVOKE", target = "Lsqueek/appleskin/client/HUDOverlayHandler;drawHungerOverlay(Lsqueek/appleskin/api/event/HUDOverlayEvent$HungerRestored;Lnet/minecraft/client/MinecraftClient;IFZ)V"))
//    @Redirect(method = "onRender", at = @At(value = "INVOKE", target = "Lsqueek/appleskin/client/HUDOverlayHandler;drawSaturationOverlay(Lsqueek/appleskin/api/event/HUDOverlayEvent$Saturation;Lnet/minecraft/client/MinecraftClient;FF)V"))

}
