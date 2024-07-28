package me.pog5.anyscale.client;

import me.pog5.anyscale.client.config.AnyscaleConfig;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnyscaleClient implements ClientModInitializer {
    public static boolean IS_ENABLEED = false;
    public static final Logger LOGGER = LoggerFactory.getLogger("Anyscale");
    public static AnyscaleConfig config = AnyscaleConfig.loadOrCreate();
    public static boolean inventory_open = false;
    @Override
    public void onInitializeClient() {
        IS_ENABLEED = true;
    }
}
