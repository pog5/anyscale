package me.pog5.anyscale.mixin.client;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.pog5.anyscale.client.config.ConfigGUIBuilder;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = SodiumOptionsGUI.class)
public class SodiumOptionsGUIMixin {
    @Shadow(remap = false)
    @Final
    private List<OptionPage> pages;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addAnyscaleOptions(Screen prevScreen, CallbackInfo ci) {
        OptionPage page = ConfigGUIBuilder.addAnyscaleGui();
        this.pages.add(page);
    }
}
