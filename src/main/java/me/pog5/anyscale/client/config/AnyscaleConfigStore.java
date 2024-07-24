package me.pog5.anyscale.client.config;

import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import me.pog5.anyscale.client.AnyscaleClient;

public class AnyscaleConfigStore implements OptionStorage<AnyscaleConfig> {
    private final AnyscaleConfig config;

    public AnyscaleConfigStore() {
        config = AnyscaleClient.config;
    }

    @Override
    public AnyscaleConfig getData() {
        return config;
    }

    @Override
    public void save() {
        config.save();
    }
}
