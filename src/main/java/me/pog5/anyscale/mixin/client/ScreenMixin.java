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
    public void Anyscale$handleSetScreen(Screen screen, CallbackInfo ci) {
        float baseScale = AnyscaleConfig.loadOrCreate().base_scale;
        if (
                screen != null &&
                        MinecraftClient.getInstance().currentScreen == null
                        && !screen.getTitle().contains(Text.translatable("chat_screen.title")) // dont scale up chat
                        && !Objects.equals(screen.getTitle(), Text.empty())
        ) {
            if (!screen.getTitle().contains(Text.translatable("gui.advancements"))
                    && !screen.getTitle().contains(Text.translatable("gui.socialInteractions.title"))
                    && !screen.getTitle().contains(Text.translatable("menu.game"))
            ) { // ^^ the easiest way to check if we're in a container: just don't do it for things that aren't a container
                float scale = AnyscaleConfig.loadOrCreate().menu_scale;
                MinecraftClient.getInstance().getWindow().setScaleFactor(baseScale * scale);
            } else {
                MinecraftClient.getInstance().getWindow().setScaleFactor(baseScale);
            }
        }
        if (screen == null) {
            MinecraftClient.getInstance().getWindow().setScaleFactor(baseScale);
            MinecraftClient.getInstance().onResolutionChanged();
        }
    }
}
