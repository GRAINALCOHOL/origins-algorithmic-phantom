package cn.grainalcohol.config;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class OAPConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAPConfig.class);
    public static final File CONFIG_DIR = new File(FabricLoader.getInstance().getConfigDir().toFile(), "oap");

    private static OAPConfig INSTANCE;

    private CommonConfig common = new CommonConfig();
    private PityConfig pity = new PityConfig();

    public CommonConfig getCommonConfig() {
        return common;
    }

    public PityConfig getPityConfig() {
        return pity;
    }

    public static OAPConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = load();
        }
        return INSTANCE;
    }

//    private static ModConfig load() {
//        try {
//            if (!CONFIG_DIR.exists()) {
//                Files.createDirectories(CONFIG_DIR.toPath());
//            }
//
//            if (COMMON.exists()) {
//                try (FileReader reader = new FileReader(COMMON)) {
//                    return GSON.fromJson(reader, ModConfig.class);
//                } catch (IOException e) {
//                    LOGGER.error("Failed to load config file", e);
//                }
//            }
//
//            ModConfig config = new ModConfig();
//            config.save();
//            return config;
//        } catch (IOException e) {
//            LOGGER.error("Failed to create config directory", e);
//            return new ModConfig();
//        }
//    }
//
//    public void save() {
//        try {
//            if (!CONFIG_DIR.exists()) {
//                Files.createDirectories(CONFIG_DIR.toPath());
//            }
//
//            try (FileWriter writer = new FileWriter(COMMON)) {
//                GSON.toJson(this, writer);
//            } catch (IOException e) {
//                LOGGER.error("Failed to save config file", e);
//            }
//        } catch (IOException e) {
//            LOGGER.error("Failed to create config directory", e);
//        }
//    }

    private static OAPConfig load() {
        try {
            if (!CONFIG_DIR.exists()) {
                Files.createDirectories(CONFIG_DIR.toPath());
            }

            OAPConfig config = new OAPConfig();

            config.common = CommonConfig.load();
            config.pity = PityConfig.load();

            return config;
        } catch (IOException e) {
            LOGGER.error("Failed to create config directory", e);
            return new OAPConfig();
        }
    }

    public void save() {
        common.save();
        pity.save();
    }
}
