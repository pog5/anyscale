package me.pog5.anyscale.client.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.pog5.anyscale.client.AnyscaleClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class AnyscaleConfig {
    // The options
    public float base_scale = 2;
    public float menu_scale = 1;
    public float playerlist_scale = 1;
    public float scoreboard_scale = 1;
    public float hotbar_scale = 1;
    public float chat_scale = 1;
    public float debug_scale = 1;
    public float title_scale = 1;
    public float actionbar_scale = 1;
    public boolean container_disables_chat = false;

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
        Path configPath = getConfigPath();
        Path tempPath = configPath.resolveSibling(configPath.getFileName().toString() + ".tmp");

        try {
            Path parentDir = configPath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            String json = GSON.toJson(this);
            Files.writeString(tempPath, json, StandardCharsets.UTF_8);
            Files.move(tempPath, configPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            AnyscaleClient.LOGGER.debug("Successfully saved config to {}", configPath);
        } catch (IOException e) {
            AnyscaleClient.LOGGER.error("Failed to save config file: {}", configPath, e);
            try {
                Files.deleteIfExists(tempPath);
            } catch (IOException cleanupEx) {
                AnyscaleClient.LOGGER.error("Failed to delete temporary config file: {}", tempPath, cleanupEx);
                e.addSuppressed(cleanupEx);
            }
        }
    }

    public static Path getConfigPath() {
        return FabricLoader.getInstance()
                .getConfigDir()
                .resolve("anyscale-config.json");
    }
}
