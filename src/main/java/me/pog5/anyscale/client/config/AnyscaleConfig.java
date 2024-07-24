package me.pog5.anyscale.client.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.pog5.anyscale.client.AnyscaleClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

public class AnyscaleConfig {
    //The options
    public float menu_scale = 1;
    public float playerlist_scale = 1;
    public float scoreboard_scale = 1;
    public float hotbar_scale = 1;
    public float chat_scale = 1;
    public float scoreboard_offset = 1;

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    private AnyscaleConfig() {}
    public static AnyscaleConfig loadOrCreate() {
        Path path = getConfigPath();
        if (Files.exists(path)) {
            try (FileReader reader = new FileReader(path.toFile())) {
                return GSON.fromJson(reader, AnyscaleConfig.class);
            } catch (IOException e) {
                AnyscaleClient.LOGGER.error("Could not parse config", e);
            }
        }
        return new AnyscaleConfig();
    }

    public void save() {
        //Unsafe, todo: fixme! needs to be atomic!
        try {
            Files.writeString(getConfigPath(), GSON.toJson(this));
        } catch (IOException e) {
            AnyscaleClient.LOGGER.error("Failed to write config file", e);
        }
    }

    private static Path getConfigPath() {
        return FabricLoader.getInstance()
                .getConfigDir()
                .resolve("anyscale-config.json");
    }
}
