package me.pog5.anyscale.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pog5.anyscale.client.AnyscaleClient;
import me.pog5.anyscale.client.config.AnyscaleConfig;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ChatComponent.class, priority = 1050)
public class ChatHudMixin {

    @WrapOperation(method = "render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;getScale()D"))
    public double Anyscale$spoofChatScale(ChatComponent instance, Operation<Double> original) {
        return AnyscaleClient.container_open && AnyscaleClient.config.container_disables_chat ? 0
                : AnyscaleConfig.loadOrCreate().chat_scale;
    }

}
