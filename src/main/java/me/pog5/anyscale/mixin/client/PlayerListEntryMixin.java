package me.pog5.anyscale.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin {
    @Inject(method = "getScoreboardTeam", at = @At("HEAD"), cancellable = true)
    public void getTeam(CallbackInfoReturnable<Team> cir) {
        if (MinecraftClient.getInstance().player == null) cir.setReturnValue(null);
    }
}
