package me.pog5.anyscale.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import me.pog5.anyscale.client.AnyscaleClient;
import me.pog5.anyscale.client.config.AnyscaleConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Objects;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    @Final
    private Window window;

    @Shadow
    @Nullable
    public Screen screen;

    @Shadow
    public abstract RenderTarget getMainRenderTarget();

    @Shadow
    @Final
    public GameRenderer gameRenderer;

    @Shadow
    @Final
    public MouseHandler mouseHandler;

    @Shadow
    @Final
    public Options options;

    @Shadow
    public abstract boolean isEnforceUnicode();

    @WrapMethod(method = "resizeDisplay")
    void overwriteResizeDisplay(Operation<Void> original) {
//        int i = this.window.calculateScale((int) AnyscaleClient.config.base_scale, isEnforceUnicode());
//        System.out.println("i: " + i + " base_scale: " + AnyscaleClient.config.base_scale);
//        this.window.setGuiScale(i);
//        this.window.setGuiScale((int) AnyscaleClient.config.base_scale);
        if (screen != null) {
            screen.resize(window.getGuiScaledWidth(), window.getGuiScaledHeight());
        }

        RenderTarget renderTarget = getMainRenderTarget();
        renderTarget.resize(window.getWidth(), window.getHeight());
        gameRenderer.resize(window.getWidth(), window.getHeight());
        mouseHandler.setIgnoreFirstMove();
    }

    @Inject(method = "setScreen", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;", opcode = Opcodes.PUTFIELD))
    public void Anyscale$handleSetScreen(Screen screen, CallbackInfo ci) {
        if (
                screen != null &&
                        Minecraft.getInstance().screen == null
                        && !screen.getTitle().contains(Component.translatable("chat_screen.title")) // dont scale up chat
                        && !Objects.equals(screen.getTitle(), Component.empty())
        ) {
            if (screen instanceof AbstractContainerScreen) {
                if (AnyscaleConfig.loadOrCreate().container_disables_chat) {
                    AnyscaleClient.container_open = true;
                }
            } else {
                AnyscaleClient.container_open = false;
            }
        }
        if (screen == null) {
            AnyscaleClient.container_open = false;
        }
    }
}
