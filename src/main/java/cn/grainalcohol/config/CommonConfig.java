package cn.grainalcohol.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class CommonConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConfig.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG = new File(OAPConfig.CONFIG_DIR, "common.json");

    public PowerConfig power = new PowerConfig();
    public EnhancedConfig enhanced = new EnhancedConfig();

    public static class PowerConfig {
        public boolean enableDamageTakenPowerFix = true;
    }

    public static class EnhancedConfig {
        public boolean grantPowerSoundEffect = true;
        public int grantPowerSoundEffectCooldown = 20;
        public float grantPowerVolume = 1.0f;

        public boolean revokePowerSoundEffect = true;
        public int revokePowerSoundEffectCooldown = 20;
        public float revokePowerVolume = 1.0f;
    }

    public static CommonConfig load() {
        try {
            if (!OAPConfig.CONFIG_DIR.exists()) {
                Files.createDirectories(OAPConfig.CONFIG_DIR.toPath());
            }

            if (CONFIG.exists()) {
                try (FileReader reader = new FileReader(CONFIG)) {
                    return GSON.fromJson(reader, CommonConfig.class);
                } catch (IOException e) {
                    LOGGER.error("Failed to load common config file", e);
                }
            }

            CommonConfig config = new CommonConfig();
            config.save();
            return config;
        } catch (IOException e) {
            LOGGER.error("Failed to create config directory", e);
            return new CommonConfig();
        }
    }

    public void save() {
        try {
            if (!OAPConfig.CONFIG_DIR.exists()) {
                Files.createDirectories(OAPConfig.CONFIG_DIR.toPath());
            }

            try (FileWriter writer = new FileWriter(CONFIG)) {
                GSON.toJson(this, writer);
            } catch (IOException e) {
                LOGGER.error("Failed to save common config file", e);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to create config directory", e);
        }
    }
}
