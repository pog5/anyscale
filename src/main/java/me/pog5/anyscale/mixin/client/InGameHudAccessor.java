package me.pog5.anyscale.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InGameHud.class)
public interface InGameHudAccessor {
    @Accessor("playerListHud")
    PlayerListHud getPlayerListHud();

    @Invoker("renderScoreboardSidebar")
    void invokeRenderScoreboardSidebar(DrawContext context, ScoreboardObjective objective);
}
