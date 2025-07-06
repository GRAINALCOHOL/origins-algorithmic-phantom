package cn.grainalcohol.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModConfig.class);

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(
            FabricLoader.getInstance().getConfigDir().toFile(),
            "oap.json"
    );

    private static ModConfig INSTANCE;

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

    public static ModConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = load();
        }
        return INSTANCE;
    }

    private static ModConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                LOGGER.error("Failed to load config file", e);
            }
        }

        ModConfig config = new ModConfig();
        config.save();
        return config;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save config file", e);
        }
    }
}
