package io.github.squid233.elevator.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.file.Files.*;

/**
 * @author squid233
 * @since 0.2.0
 */
public class EModConfigs {
    public static final Path CONFIG_DIR = Path.of("config", "elevator233");
    public static final Path CONFIG_FILE = CONFIG_DIR.resolve("configs.json");
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Configurator.class, new Configurator.Serializer())
        .create();
    public static Configurator configurator;

    public static void loadAllCfg() {
        try {
            var dir404 = notExists(CONFIG_DIR);
            var file404 = dir404 || notExists(CONFIG_FILE);
            if (dir404) {
                createDirectories(CONFIG_DIR);
            }
            if (file404) {
                configurator = new Configurator();
                writeAllCfg(StandardOpenOption.CREATE_NEW);
            } else {
                configurator = GSON.fromJson(readString(CONFIG_FILE),
                    Configurator.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeAllCfg(OpenOption... options) {
        try {
            writeString(CONFIG_FILE,
                GSON.toJson(configurator),
                options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
