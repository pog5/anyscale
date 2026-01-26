package me.pog5.anyscale.mixin.client.fixes;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.UUID;

@Mixin(PlayerTabOverlay.class)
public class PlayerlistMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "getPlayerInfos", at = @At("HEAD"), cancellable = true)
    public void modifySingleplay(CallbackInfoReturnable<List<PlayerInfo>> cir) {
        if (this.minecraft.player == null) {
            cir.setReturnValue(Lists.newArrayList(
                    new PlayerInfo(this.minecraft.player.getGameProfile(), false)
            ));
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isLocalServer()Z"))
    public boolean modifySingleplay(Minecraft instance, Operation<Boolean> original) {
        if (this.minecraft.player == null) return true;
        return original.call(instance);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getPlayerByUUID(Ljava/util/UUID;)Lnet/minecraft/world/entity/player/Player;"))
    public Player modifySingleplay(ClientLevel instance, UUID uuid, Operation<Player> original) {
        if (this.minecraft.player == null) return null;
        return original.call(instance, uuid);
    }
}
