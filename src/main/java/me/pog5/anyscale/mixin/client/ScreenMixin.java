package me.pog5.anyscale.mixin.client;

import me.pog5.anyscale.client.config.AnyscaleConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(MinecraftClient.class)
public class ScreenMixin {
    @Inject(method = "setScreen", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;"))
    public void onSetScreen(Screen screen, CallbackInfo ci) {
        if (
                screen != null &&
                MinecraftClient.getInstance().currentScreen == null
                && !screen.getTitle().contains(Text.translatable("chat_screen.title"))
        ) {
            float scale = AnyscaleConfig.loadOrCreate().menu_scale;
            MinecraftClient.getInstance().getWindow().setScaleFactor(scale);
        }
        if (screen == null) {
            MinecraftClient.getInstance().onResolutionChanged();
        }
    }
}
