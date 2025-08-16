package grainalcohol.oap.config;

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

    public CommonConfig getCommonConfig() {
        return common;
    }

    public static OAPConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = load();
        }
        return INSTANCE;
    }

    private static OAPConfig load() {
        try {
            if (!CONFIG_DIR.exists()) {
                Files.createDirectories(CONFIG_DIR.toPath());
            }

            OAPConfig config = new OAPConfig();

            config.common = CommonConfig.load();

            return config;
        } catch (IOException e) {
            LOGGER.error("Failed to create config directory", e);
            return new OAPConfig();
        }
    }
}
