package me.pog5.anyscale.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.pog5.anyscale.client.config.AnyscaleConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import squeek.appleskin.api.event.HUDOverlayEvent;
import squeek.appleskin.client.HUDOverlayHandler;
import squeek.appleskin.helpers.TextureHelper;
import squeek.appleskin.util.IntPoint;

@Mixin(value = HUDOverlayHandler.class, priority = 1055)
public class AppleSkinMixin {

    @Shadow
    private boolean needDisableBlend;

    @Redirect(method = "onRender", at = @At(value = "INVOKE", target = "Lsqueek/appleskin/client/HUDOverlayHandler;drawHealthOverlay(Lsqueek/appleskin/api/event/HUDOverlayEvent$HealthRestored;Lnet/minecraft/client/MinecraftClient;F)V"))
//                            this.drawHealthOverlay(healthRenderEvent, mc, this.flashAlpha);
    private void Anyscale$appleSkinHealth(HUDOverlayHandler instance, HUDOverlayEvent.HealthRestored event, MinecraftClient mc, float alpha) {
        DrawContext context = event.context;
        float health = mc.player.getHealth();
        float modifiedHealth = event.modifiedHealth;
        int right = event.x;
        int top = event.y;
//        this.drawHealthOverlay(event.context, mc.player.getHealth(), event.modifiedHealth, mc, event.x, event.y, alpha);
        if (!(modifiedHealth <= health)) {
//          START  instance.enableAlpha(alpha);
            needDisableBlend = !GL11.glIsEnabled(GL11.GL_BLEND);
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
//          END  instance.enableAlpha(alpha);
            mc.getTextureManager().bindTexture(TextureHelper.MC_ICONS);
            int fixedModifiedHealth = (int)Math.ceil((double)modifiedHealth);
            boolean isHardcore = mc.player.getWorld() != null && mc.player.getWorld().getLevelProperties().isHardcore();
            int startHealthBars = (int)Math.max(0.0, Math.ceil((double)health) / 2.0);
            int endHealthBars = (int)Math.max(0.0, Math.ceil((double)(modifiedHealth / 2.0F)));
            int iconStartOffset = 16;
            int iconSize = 9;

            for(int i = startHealthBars; i < endHealthBars; ++i) {
                IntPoint offset = (IntPoint)instance.healthBarOffsets.get(i);
                if (offset != null) {
                    int x = right + offset.x;
                    int y = top + offset.y;
                    int v = 0 * iconSize;
                    int u = iconStartOffset + 4 * iconSize;
                    int ub = iconStartOffset + 1 * iconSize;
                    if (i * 2 + 1 == fixedModifiedHealth) {
                        u += 1 * iconSize;
                    }

                    if (isHardcore) {
                        v = 5 * iconSize;
                    }

                    float listScale = AnyscaleConfig.loadOrCreate().hotbar_scale;
                    context.getMatrices().push();
                    context.getMatrices().scale(listScale, listScale, 2);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha * 0.25F);
                    context.drawTexture(TextureHelper.MC_ICONS, x, y, ub, v, iconSize, iconSize);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
                    context.drawTexture(TextureHelper.MC_ICONS, x, y, u, v, iconSize, iconSize);
                    context.getMatrices().pop();
                }
            }

//            START instance.disableAlpha(alpha);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                if (needDisableBlend)
                    RenderSystem.disableBlend();
//            END instance.disableAlpha(alpha);
        }
    }
    @Redirect(method = "onRender", at = @At(value = "INVOKE", target = "Lsqueek/appleskin/client/HUDOverlayHandler;drawHungerOverlay(Lsqueek/appleskin/api/event/HUDOverlayEvent$HungerRestored;Lnet/minecraft/client/MinecraftClient;IFZ)V"))
    private void Anyscale$appleSkinHunger(HUDOverlayHandler instance, HUDOverlayEvent.HungerRestored event, MinecraftClient mc, int hunger, float alpha, boolean useRottenTextures) {

    }
    @Redirect(method = "onRender", at = @At(value = "INVOKE", target = "Lsqueek/appleskin/client/HUDOverlayHandler;drawSaturationOverlay(Lsqueek/appleskin/api/event/HUDOverlayEvent$Saturation;Lnet/minecraft/client/MinecraftClient;FF)V"))
    private void Anyscale$appleSkinSaturation(HUDOverlayHandler instance, HUDOverlayEvent.Saturation event, MinecraftClient mc, float saturationGained, float alpha) {

    }

}
