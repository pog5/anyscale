package me.pog5.anyscale.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Gui.class)
public interface InGameHudAccessor {
    @Accessor("tabList")
    PlayerTabOverlay getPlayerListHud();

    @Invoker("renderScoreboardSidebar")
    void invokeRenderScoreboardSidebar(GuiGraphics context, DeltaTracker objective);
}
