package xyz.eclipseisoffline.eclipsestweakeroo.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.util.JsonUtils;
import net.fabricmc.loader.api.FabricLoader;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EclipsesConfigs implements IConfigHandler {

    private static final String TWEAKEROO_MOD_ID = "tweakeroo";
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(EclipsesTweakeroo.MOD_ID + ".json");
    private static final Path TWEAKEROO_CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(TWEAKEROO_MOD_ID + ".json");

    @Override
    public void load() {
        if (!Files.exists(CONFIG_FILE)) { // Legacy
            // Backwards compatibility - try to read from the Tweakeroo config file, which is where configs were stored up until 1.21.6
            if (Files.exists(TWEAKEROO_CONFIG_FILE) && Files.isReadable(TWEAKEROO_CONFIG_FILE)) {
                EclipsesTweakeroo.LOGGER.info("Config file not found - attempting to read from Tweakeroo file (legacy, will move to new file)");
                JsonElement legacyConfig = JsonUtils.parseJsonFile(TWEAKEROO_CONFIG_FILE.toFile());
                if (legacyConfig != null && legacyConfig.isJsonObject()) {
                    JsonObject root = legacyConfig.getAsJsonObject();

                    ConfigUtils.readConfigBase(root, "Fixes", EclipsesFixesConfig.values());
                    ConfigUtils.readConfigBase(root, "Generic", EclipsesGenericConfig.values());
                    ConfigUtils.readConfigBase(root, "Lists", EclipsesListsConfig.values());
                    ConfigUtils.readHotkeyToggleOptions(root, "DisableHotkeys", "DisableToggles", EclipsesDisableConfig.hotkeys());
                    ConfigUtils.readHotkeyToggleOptions(root, "TweakHotkeys", "TweakToggles", EclipsesTweaksConfig.hotkeys());
                    EclipsesTweakeroo.LOGGER.info("Read config options from Tweakeroo file - moving to new separate config file");
                    save();
                } else {
                    EclipsesTweakeroo.LOGGER.warn("Failed to read from legacy config file!");
                }
            } else {
                EclipsesTweakeroo.LOGGER.info("Config file not found - will create one after change");
            }
        } else if (Files.isReadable(CONFIG_FILE)) {
            JsonElement config = JsonUtils.parseJsonFile(CONFIG_FILE.toFile());
            if (config != null && config.isJsonObject()) {
                JsonObject root = config.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "fixes", EclipsesFixesConfig.values());
                ConfigUtils.readConfigBase(root, "generic", EclipsesGenericConfig.values());
                ConfigUtils.readConfigBase(root, "hotkeys", EclipsesHotkeys.values());
                ConfigUtils.readConfigBase(root, "lists", EclipsesListsConfig.values());
                ConfigUtils.readHotkeyToggleOptions(root, "disable_hotkeys", "disable", EclipsesDisableConfig.hotkeys());
                ConfigUtils.readHotkeyToggleOptions(root, "tweak_hotkeys", "tweaks", EclipsesTweaksConfig.hotkeys());
            } else {
                EclipsesTweakeroo.LOGGER.warn("Failed to read from config file!");
            }
        }
    }

    @Override
    public void save() {
        if (!Files.isDirectory(FabricLoader.getInstance().getConfigDir())) {
            try {
                Files.createDirectories(FabricLoader.getInstance().getConfigDir());
            } catch (IOException exception) {
                EclipsesTweakeroo.LOGGER.warn("Failed to create config directory to save config!", exception);
                return;
            }
        }

        JsonObject root = new JsonObject();
        ConfigUtils.writeConfigBase(root, "fixes", EclipsesFixesConfig.values());
        ConfigUtils.writeConfigBase(root, "generic", EclipsesGenericConfig.values());
        ConfigUtils.writeConfigBase(root, "hotkeys", EclipsesHotkeys.values());
        ConfigUtils.writeConfigBase(root, "lists", EclipsesListsConfig.values());
        ConfigUtils.writeHotkeyToggleOptions(root, "disable_hotkeys", "disable", EclipsesDisableConfig.hotkeys());
        ConfigUtils.writeHotkeyToggleOptions(root, "tweak_hotkeys", "tweaks", EclipsesTweaksConfig.hotkeys());

        JsonUtils.writeJsonToFile(root, CONFIG_FILE.toFile());
    }
}
