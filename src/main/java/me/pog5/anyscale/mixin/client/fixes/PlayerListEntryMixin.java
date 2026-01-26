package me.pog5.anyscale.mixin.client.fixes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public class PlayerListEntryMixin {
    @Inject(method = "getTeam", at = @At("HEAD"), cancellable = true)
    public void getTeam(CallbackInfoReturnable<PlayerTeam> cir) {
        if (Minecraft.getInstance().player == null) cir.setReturnValue(null);
    }
}
