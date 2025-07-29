package grainalcohol.oap.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class PityConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConfig.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG = new File(OAPConfig.CONFIG_DIR, "pity.json");

    public Map<String, Map<String, Map<String, Integer>>> pityData = new HashMap<>();

    public int getPityCount(String worldName, String playerUuid, String poolId) {
        return pityData.getOrDefault(worldName, new HashMap<>())
                .getOrDefault(playerUuid, new HashMap<>())
                .getOrDefault(poolId, 0);
    }

    public void incrementPity(String worldName, String playerUuid, String poolId) {
        pityData.computeIfAbsent(worldName, k -> new HashMap<>())
                .computeIfAbsent(playerUuid, k -> new HashMap<>())
                .merge(poolId, 1, Integer::sum);
        save();
    }

    public void resetPity(String worldName, String playerUuid, String poolId) {
        pityData.computeIfAbsent(worldName, k -> new HashMap<>())
                .computeIfAbsent(playerUuid, k -> new HashMap<>())
                .put(poolId, 0);
        save();
    }

    public void clearAllData() {
        pityData.clear();
        save();
    }

    public void removeSaveData(String worldName) {
        if (pityData.containsKey(worldName)) {
            pityData.remove(worldName);
            save();
        }
    }

    public static PityConfig load() {
        try {
            if (!OAPConfig.CONFIG_DIR.exists()) {
                Files.createDirectories(OAPConfig.CONFIG_DIR.toPath());
            }

            if (CONFIG.exists()) {
                try (FileReader reader = new FileReader(CONFIG)) {
                    return GSON.fromJson(reader, PityConfig.class);
                }
            }
            return new PityConfig();
        } catch (IOException e) {
            LOGGER.error("Failed to load pity config", e);
            return new PityConfig();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save pity config", e);
        }
    }
}
