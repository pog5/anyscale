package me.pog5.anyscale.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.PoseStack;
import me.pog5.anyscale.client.AnyscaleClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.pip.OversizedItemRenderer;
import net.minecraft.client.gui.render.state.GuiItemRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.render.state.pip.OversizedItemRenderState;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Map;

@Mixin(GuiRenderer.class)
public abstract class GuiRendererMixin {
    @Shadow protected abstract void invalidateItemAtlas();
    @Shadow @Final private Map<Object, OversizedItemRenderer> oversizedItemRenderers;
    @Shadow @Final private GuiRenderState renderState;
    @Shadow private @Nullable GpuTexture itemsAtlas;
    @Shadow protected abstract void createAtlasTextures(int i);
    @Shadow protected abstract int calculateAtlasSizeInPixels(int i);
    @Shadow private @Nullable GpuTextureView itemsAtlasView;
    @Shadow private @Nullable GpuTextureView itemsAtlasDepthView;
    @Shadow @Final private CachedOrthoProjectionMatrixBuffer itemsProjectionMatrixBuffer;
    @Shadow @Final private Map<Object, GuiRenderer.AtlasPosition> atlasPositions;
    @Shadow private int itemAtlasX;
    @Shadow private int itemAtlasY;
    @Shadow @Final private static Logger LOGGER;
    @Shadow private @Nullable GpuTexture itemsAtlasDepth;
    @Shadow private int frameNumber;
    @Shadow protected abstract void submitBlitFromItemAtlas(GuiItemRenderState guiItemRenderState, float f, float g, int i, int j);
    @Shadow protected abstract void renderItemToAtlas(TrackingItemStackRenderState trackingItemStackRenderState, PoseStack poseStack, int i, int j, int k);

    @Shadow
    @Final
    private MultiBufferSource.BufferSource bufferSource;

    @ModifyArgs(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/CachedOrthoProjectionMatrixBuffer;getBuffer(FF)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"))
    void modifyProjectionMatrix(Args args) {
        var trueScale = AnyscaleClient.config.base_scale;
        var window = Minecraft.getInstance().getWindow();
        args.set(0, window.getWidth() / trueScale);
        args.set(1, window.getHeight() / trueScale);
    }

    @Unique
    private float cachedGuiScaleFloat;

    @WrapMethod(method = "getGuiScaleInvalidatingItemAtlasIfChanged")
    int modifyGuiScaleIfAtlasChangedToFloat(Operation<Integer> original) {
        return 0;
    }

    /**
     * @author Nightly
     * @reason To allow floating point scaling instead of integer scaling
     */
    @Overwrite
    private void prepareItemElements() {
        if (!this.renderState.getItemModelIdentities().isEmpty()) {
            float trueScale = AnyscaleClient.config.base_scale;
            if (trueScale != this.cachedGuiScaleFloat) {
                this.invalidateItemAtlas();

                for (OversizedItemRenderer oversizedItemRenderer : this.oversizedItemRenderers.values()) {
                    oversizedItemRenderer.invalidateTexture();
                }

                this.cachedGuiScaleFloat = trueScale;
            }
            int textureSize = (int) (16 * trueScale);
            int atlasSize = this.calculateAtlasSizeInPixels(textureSize);
            if (this.itemsAtlas == null) {
                this.createAtlasTextures(atlasSize);
            }

            RenderSystem.outputColorTextureOverride = this.itemsAtlasView;
            RenderSystem.outputDepthTextureOverride = this.itemsAtlasDepthView;
            RenderSystem.setProjectionMatrix(this.itemsProjectionMatrixBuffer.getBuffer(atlasSize, atlasSize), ProjectionType.ORTHOGRAPHIC);
            Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
            PoseStack poseStack = new PoseStack();
            MutableBoolean skipRenderingItems = new MutableBoolean(false);
            MutableBoolean renderOversizedItems = new MutableBoolean(false);
            this.renderState
                    .forEachItem(
                            guiItemRenderState -> {
                                if (guiItemRenderState.oversizedItemBounds() != null) {
                                    renderOversizedItems.setTrue();
                                } else {
                                    TrackingItemStackRenderState trackingItemStackRenderState = guiItemRenderState.itemStackRenderState();
                                    GuiRenderer.AtlasPosition atlasPosition = (GuiRenderer.AtlasPosition)this.atlasPositions.get(trackingItemStackRenderState.getModelIdentity());
                                    if (atlasPosition == null || trackingItemStackRenderState.isAnimated() && atlasPosition.lastAnimatedOnFrame != this.frameNumber) {
                                        if (this.itemAtlasX + textureSize > atlasSize) {
                                            this.itemAtlasX = 0;
                                            this.itemAtlasY += textureSize;
                                        }

                                        boolean isAnimated = trackingItemStackRenderState.isAnimated() && atlasPosition != null;
                                        if (!isAnimated && this.itemAtlasY + textureSize > atlasSize) {
                                            if (skipRenderingItems.isFalse()) {
                                                LOGGER.warn("Trying to render too many items in GUI at the same time. Skipping some of them.");
                                                skipRenderingItems.setTrue();
                                            }
                                        } else {
                                            int kx = isAnimated ? atlasPosition.x : this.itemAtlasX;
                                            int l = isAnimated ? atlasPosition.y : this.itemAtlasY;
                                            if (isAnimated) {
                                                RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.itemsAtlas, 0, this.itemsAtlasDepth, 1.0, kx, atlasSize - l - textureSize, textureSize, textureSize);
                                            }

                                            this.renderItemToAtlas(trackingItemStackRenderState, poseStack, kx, l, textureSize);
                                            float f = (float)kx / atlasSize;
                                            float g = (float)(atlasSize - l) / atlasSize;
                                            this.submitBlitFromItemAtlas(guiItemRenderState, f, g, textureSize, atlasSize);
                                            if (isAnimated) {
                                                atlasPosition.lastAnimatedOnFrame = this.frameNumber;
                                            } else {
                                                this.atlasPositions
                                                        .put(
                                                                guiItemRenderState.itemStackRenderState().getModelIdentity(),
                                                                new GuiRenderer.AtlasPosition(this.itemAtlasX, this.itemAtlasY, f, g, this.frameNumber)
                                                        );
                                                this.itemAtlasX += textureSize;
                                            }
                                        }
                                    } else {
                                        this.submitBlitFromItemAtlas(guiItemRenderState, atlasPosition.u, atlasPosition.v, textureSize, atlasSize);
                                    }
                                }
                            }
                    );
            RenderSystem.outputColorTextureOverride = null;
            RenderSystem.outputDepthTextureOverride = null;
            if (renderOversizedItems.booleanValue()) {
                this.renderState
                        .forEachItem(
                                guiItemRenderState -> {
                                    if (guiItemRenderState.oversizedItemBounds() != null) {
                                        TrackingItemStackRenderState trackingItemStackRenderState = guiItemRenderState.itemStackRenderState();
                                        OversizedItemRenderer oversizedItemRenderer = (OversizedItemRenderer)this.oversizedItemRenderers
                                                .computeIfAbsent(trackingItemStackRenderState.getModelIdentity(), object -> new OversizedItemRenderer(this.bufferSource));
                                        ScreenRectangle screenRectangle = guiItemRenderState.oversizedItemBounds();
                                        OversizedItemRenderState oversizedItemRenderState = new OversizedItemRenderState(
                                                guiItemRenderState, screenRectangle.left(), screenRectangle.top(), screenRectangle.right(), screenRectangle.bottom()
                                        );
                                        oversizedItemRenderer.prepare(oversizedItemRenderState, this.renderState, (int) trueScale);
                                    }
                                }
                        );
            }
        }
    }
}
