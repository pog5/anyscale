package me.pog5.anyscale.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pog5.anyscale.client.AnyscaleClient;
import me.pog5.anyscale.client.config.AnyscaleConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow
    private int scaledWidth;

    @Shadow
    private int scaledHeight;

    // TAB List
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;render(Lnet/minecraft/client/gui/DrawContext;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"))
    public void playerList(PlayerListHud instance, DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, Operation<Void> original) {
        float listScale = AnyscaleConfig.loadOrCreate().playerlist_scale;
        context.getMatrices().push();
        context.getMatrices().scale(listScale, listScale, 1);
        original.call(instance, context, (int) (scaledWindowWidth / listScale), scoreboard, objective);
        context.getMatrices().pop();
    }

    // Sidebar scoreboard
    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"))
    private void scoreboardPush(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        context.getMatrices().push();
        float scale = AnyscaleConfig.loadOrCreate().scoreboard_scale;
        context.getMatrices().scale(scale, scale, scale);
        this.scaledWidth = (int) ((float) this.scaledWidth * (1.0F / scale));
        this.scaledHeight = (int) ((float) this.scaledHeight * (1.0F / scale));
    }

    @Inject(method = "renderScoreboardSidebar", at = @At("TAIL"))
    private void scoreboardPop(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        context.getMatrices().pop();
        float scale = AnyscaleConfig.loadOrCreate().scoreboard_scale;
        this.scaledWidth = (int)((float)this.scaledWidth * scale);
        this.scaledHeight = (int)((float)this.scaledHeight * scale);
    }
}
