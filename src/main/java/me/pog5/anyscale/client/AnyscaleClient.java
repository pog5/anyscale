package me.pog5.anyscale.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import me.pog5.anyscale.AnyscaleRenderPhase;
import me.pog5.anyscale.client.config.AnyscaleConfig;
import me.pog5.anyscale.mixin.client.WindowMixin;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnyscaleClient implements ClientModInitializer {
    public static boolean IS_ENABLEED = false;
    public static final Logger LOGGER = LoggerFactory.getLogger("Anyscale");
    public static AnyscaleConfig config = AnyscaleConfig.loadOrCreate();
    public static boolean container_open = false;
    public static AnyscaleRenderPhase phase = AnyscaleRenderPhase.BASE;
    @Override
    public void onInitializeClient() {
        IS_ENABLEED = true;
    }
}
