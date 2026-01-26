package me.pog5.anyscale.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pog5.anyscale.Anyscale;
import me.pog5.anyscale.AnyscaleRenderPhase;
import me.pog5.anyscale.client.config.AnyscaleConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Gui.class)
public abstract class InGameHudMixin {
//
//    @Shadow protected abstract LivingEntity getRiddenEntity();
//
//
//    @Inject(method = "render", at = @At("HEAD"))
//    public void Anyscale$onRenderHead(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
//        Anyscale.phase = AnyscaleRenderPhase.BASE;
//    }
//
//    @Inject(method = "render", at = @At("TAIL"))
//    public void Anyscale$onRenderTail(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
//        Anyscale.phase = AnyscaleRenderPhase.BASE;
//    }
//
//    // TAB List
//    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDrawer;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"))
//    public void Anyscale$wrapRenderPlayerList(LayeredDrawer instance, DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
//        float listScale = AnyscaleConfig.loadOrCreate().playerlist_scale;
//        context.getMatrices().push();
//        context.getMatrices().scale(listScale, listScale, 1);
//        original.call(instance, context, tickCounter);
//        context.getMatrices().pop();
//    }
//
//    // Sidebar scoreboard
//    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"))
//    private void Anyscale$scoreboardHead(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
//        context.getMatrices().push();
//        float scale = AnyscaleConfig.loadOrCreate().scoreboard_scale;
//        context.getMatrices().scale(scale, scale, scale);
//        this.scaledWidth = (int) ((float) this.scaledWidth * (1.0F / scale));
//        this.scaledHeight = (int) ((float) this.scaledHeight * (1.0F / scale));
//    }
//
//    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("TAIL"))
//    private void Anyscale$scoreboardTail(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
//        context.getMatrices().pop();
//        float scale = AnyscaleConfig.loadOrCreate().scoreboard_scale;
//        this.scaledWidth = (int)((float)this.scaledWidth * scale);
//        this.scaledHeight = (int)((float)this.scaledHeight * scale);
//    }
//
//    // Hotbar Background
//    @Inject(method = "renderHotbar", at = @At("HEAD"))
//    private void Anyscale$hotbarBackgroundHead(float tickDelta, DrawContext context, CallbackInfo ci) {
//        context.getMatrices().push();
//        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
//        context.getMatrices().scale(scale, scale, scale);
//        this.scaledWidth = (int) ((float) this.scaledWidth * (1.0F / scale));
//        this.scaledHeight = (int) ((float) this.scaledHeight * (1.0F / scale));
//    }
//
//    @Inject(method = "renderHotbar", at = @At("TAIL"))
//    private void Anyscale$hotbarBackgroundTail(float tickDelta, DrawContext context, CallbackInfo ci) {
//        context.getMatrices().pop();
//        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
//        this.scaledWidth = (int)((float)this.scaledWidth * scale);
//        this.scaledHeight = (int)((float)this.scaledHeight * scale);
//    }
//
//    // XP Bar
//    @Inject(method = "renderExperienceBar", at = @At("HEAD"))
//    private void Anyscale$experienceBarHead(DrawContext context, int x, CallbackInfo ci) {
//        context.getMatrices().push();
//        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
//        context.getMatrices().scale(scale, scale, scale);
//        this.scaledWidth = (int) ((float) this.scaledWidth * (1.0F / scale));
//        this.scaledHeight = (int) ((float) this.scaledHeight * (1.0F / scale));
//    }
//
//    @Inject(method = "renderExperienceBar", at = @At("TAIL"))
//    private void Anyscale$experienceBarTail(DrawContext context, int x, CallbackInfo ci) {
//        context.getMatrices().pop();
//        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
//        this.scaledWidth = (int)((float)this.scaledWidth * scale);
//        this.scaledHeight = (int)((float)this.scaledHeight * scale);
//    }
//
//    // Fix XP Bar X position
//    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderExperienceBar(Lnet/minecraft/client/gui/DrawContext;I)V"), index = 1)
//    private int Anyscale$experienceBarFixXPos(int original) {
//        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
//        return (int) ((int) ((float) this.scaledWidth * (1.0F / scale)) / 2 - 91);
//    }
//
//    // Armor, Food, Air Bars
//    @Inject(method = "renderStatusBars", at = @At("HEAD"))
//    private void Anyscale$statusBarsHead(DrawContext context, CallbackInfo ci) {
//        context.getMatrices().push();
//        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
//        context.getMatrices().scale(scale, scale, scale);
//        this.scaledWidth = (int) ((float) this.scaledWidth * (1.0F / scale));
//        this.scaledHeight = (int) ((float) this.scaledHeight * (1.0F / scale));
//    }
//
//    @Inject(method = "renderStatusBars", at = @At("TAIL"))
//    private void Anyscale$statusBarsTail(DrawContext context, CallbackInfo ci) {
//        context.getMatrices().pop();
//        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
//        this.scaledWidth = (int)((float)this.scaledWidth * scale);
//        this.scaledHeight = (int)((float)this.scaledHeight * scale);
//    }
//
//    // Mount Health
//    @Inject(method = "renderMountHealth", at = @At("HEAD"))
//    private void Anyscale$mountHealthHead(DrawContext context, CallbackInfo ci) {
//        if (this.getRiddenEntity() == null) return;
//        context.getMatrices().push();
//        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
//        context.getMatrices().scale(scale, scale, scale);
//        this.scaledWidth = (int) ((float) this.scaledWidth * (1.0F / scale));
//        this.scaledHeight = (int) ((float) this.scaledHeight * (1.0F / scale));
//    }
//
//    @Inject(method = "renderMountHealth", at = @At("TAIL"))
//    private void Anyscale$mountHealthTail(DrawContext context, CallbackInfo ci) {
//        context.getMatrices().pop();
//        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
//        this.scaledWidth = (int)((float)this.scaledWidth * scale);
//        this.scaledHeight = (int)((float)this.scaledHeight * scale);
//    }
//
//    // Mount Jump Bar
//    @Inject(method = "renderMountJumpBar", at = @At("HEAD"))
//    private void Anyscale$mountJumpBarHead(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
//        if (this.getRiddenEntity() == null) return;
//        context.getMatrices().push();
//        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
//        context.getMatrices().scale(scale, scale, scale);
//        this.scaledWidth = (int) ((float) this.scaledWidth * (1.0F / scale));
//        this.scaledHeight = (int) ((float) this.scaledHeight * (1.0F / scale));
//    }
//
//    @Inject(method = "renderMountJumpBar", at = @At("TAIL"))
//    private void Anyscale$mountJumpBarTail(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
//        context.getMatrices().pop();
//        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
//        this.scaledWidth = (int)((float)this.scaledWidth * scale);
//        this.scaledHeight = (int)((float)this.scaledHeight * scale);
//    }
//
//    // Fix Mount Jump bar X position
//    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderMountJumpBar(Lnet/minecraft/entity/JumpingMount;Lnet/minecraft/client/gui/DrawContext;I)V"), index = 2)
//    private int Anyscale$mountJumpBarFixXPos(int x) {
//        float scale = AnyscaleConfig.loadOrCreate().hotbar_scale;
//        return (int) ((int) ((float) this.scaledWidth * (1.0F / scale)) / 2 - 91);
//    }
}
