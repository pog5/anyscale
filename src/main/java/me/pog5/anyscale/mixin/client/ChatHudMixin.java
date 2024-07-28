package me.pog5.anyscale.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pog5.anyscale.client.config.AnyscaleConfig;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ChatHud.class, priority = 1050)
public class ChatHudMixin {

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;getChatScale()D"))
    public double Anyscale$spoofChatScale(ChatHud instance, Operation<Double> original) {
        return AnyscaleConfig.loadOrCreate().chat_scale;
    }

}
