package me.pog5.anyscale.mixin.client;

import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import me.pog5.anyscale.client.AnyscaleClient;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
// Add imports for GuiRenderState, BufferSource, Texture, etc. based on your environment

@Mixin(PictureInPictureRenderer.class)
public abstract class PictureInPictureRendererMixin<T extends PictureInPictureRenderState> { // Replace T bounds with actual class if known

    // 1. Shadow all fields and methods used in the original method
    @Shadow protected abstract boolean textureIsReadyToBlit(T state);
    @Shadow protected abstract void blitTexture(T state, GuiRenderState guiState);
    @Shadow protected abstract void prepareTexturesAndProjection(boolean bl, int width, int height);
    @Shadow protected abstract float getTranslateY(int height, int scale);
    @Shadow protected abstract void renderToTexture(T state, PoseStack poseStack);

    @Shadow @Final protected MultiBufferSource.BufferSource bufferSource;
    @Shadow private GpuTexture texture;
    @Shadow private GpuTextureView textureView;
    @Shadow private GpuTextureView depthTextureView;

    /**
     * @author Nightly
     * @reason To allow floating point scaling instead of integer scaling
     */
    @Overwrite
    public void prepare(T pictureInPictureRenderState, GuiRenderState guiRenderState, int originalI) {
        // We ignore 'originalI' and use 'myCustomScale'

        // Use float math for calculation, then cast to int because texture dimensions must be integers
        int widthPip = (int) ((pictureInPictureRenderState.x1() - pictureInPictureRenderState.x0()) * AnyscaleClient.config.base_scale);
        int heightPip = (int) ((pictureInPictureRenderState.y1() - pictureInPictureRenderState.y0()) * AnyscaleClient.config.base_scale);

        boolean bl = this.texture == null || this.texture.getWidth(0) != widthPip || this.texture.getHeight(0) != heightPip;

        if (!bl && this.textureIsReadyToBlit(pictureInPictureRenderState)) {
            this.blitTexture(pictureInPictureRenderState, guiRenderState);
        } else {
            this.prepareTexturesAndProjection(bl, widthPip, heightPip);

            RenderSystem.outputColorTextureOverride = this.textureView;
            RenderSystem.outputDepthTextureOverride = this.depthTextureView;

            PoseStack poseStack = new PoseStack();

            // Note: We cast myCustomScale to int for getTranslateY to satisfy the original signature
            // If getTranslateY needs the float precision, you will need to Mixin that method too.
            poseStack.translate(widthPip / 2.0F, this.getTranslateY(heightPip, (int)AnyscaleClient.config.base_scale), 0.0F);

            // HERE is where the float shines: Smooth scaling
            float f = AnyscaleClient.config.base_scale * pictureInPictureRenderState.scale();
            poseStack.scale(f, f, -f);

            this.renderToTexture(pictureInPictureRenderState, poseStack);
            this.bufferSource.endBatch();

            RenderSystem.outputColorTextureOverride = null;
            RenderSystem.outputDepthTextureOverride = null;

            this.blitTexture(pictureInPictureRenderState, guiRenderState);
        }
    }
}