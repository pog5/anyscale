package me.pog5.anyscale;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.pog5.anyscale.client.config.AnyscaleConfig.getConfigPath;

public class Anyscale implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("anyscale");

    @Override
    public void onInitialize() {
        try {
            Class<?> sodiumclass = Class.forName("net.caffeinemc.mods.sodium.client.SodiumClientMod");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Sodium not found, you will not be able to configure the mod via the Video Options.");
            LOGGER.warn("If you wish to configure it, either install sodium or edit the config manually at:");
            LOGGER.warn(getConfigPath().toString());
        }
    }
}
