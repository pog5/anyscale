package me.pog5.anyscale.mixin.client.sodium;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.pog5.anyscale.Anyscale;
import me.pog5.anyscale.client.config.AnyscaleConfig;
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(SodiumGameOptionPages.class)
public class SodiumGameOptionPagesMixin {
    @Shadow @Final private static MinecraftOptionsStorage vanillaOpts;

    @ModifyReturnValue(method = "general()Lnet/caffeinemc/mods/sodium/client/gui/options/OptionPage;", at = @At("RETURN"), remap = false)
    private static OptionPage anyscale$removeVanillaScaleFromSodiumGeneral(OptionPage original) {
        List<OptionGroup> optionGroups = new ArrayList<>();
        for (OptionGroup group : original.getGroups()) {
            var optionGroupBuilder = OptionGroup.createBuilder();
            for (var option : group.getOptions()) {
                if (option.getName().equals(Text.translatable("options.guiScale"))) {
                    optionGroupBuilder.add(OptionImpl.createBuilder(int.class, vanillaOpts)
                            .setName(Text.translatable("options.guiScale"))
                            .setTooltip(Text.translatable("sodium.options.gui_scale.tooltip").append("\n\nThis option has been disabled by Anyscale, use the UI scaling tab's Base Scale option."))
                            .setControl(opt -> new SliderControl(opt, 0, MinecraftClient.getInstance().getWindow().calculateScaleFactor(0, MinecraftClient.getInstance().forcesUnicodeFont()), 1, ControlValueFormatter.guiScale()))
                            .setBinding((opts, value) -> {
                                AnyscaleConfig.loadOrCreate().base_scale = value;
                            }, opts -> (int) AnyscaleConfig.loadOrCreate().base_scale)
                            .setEnabled(() -> false)
                            .build());
                } else {
                    optionGroupBuilder.add(option);
                }
            }
            optionGroups.add(optionGroupBuilder.build());
        }
        return new OptionPage(original.getName(), ImmutableList.copyOf(optionGroups));
    }
}
