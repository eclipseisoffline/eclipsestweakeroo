package xyz.eclipseisoffline.eclipsestweakeroo.toggle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record ToggleConfig(Map<ServerSideToggle, Boolean> toggles, boolean operatorsExempt) {
    public static final Codec<ToggleConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(ServerSideToggle.CODEC, Codec.BOOL).fieldOf("toggles").forGetter(ToggleConfig::toggles),
                    Codec.BOOL.optionalFieldOf("operators_exempt").forGetter(config -> Optional.of(config.operatorsExempt()))
            ).apply(instance, ToggleConfig::new)
    );
    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(EclipsesTweakeroo.MOD_ID + "-server.json");
    public static final ToggleConfig EMPTY = new ToggleConfig(Map.of(), false);

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final ToggleConfig DEFAULT = new ToggleConfig(Map.of(), Optional.empty());

    public ToggleConfig(Map<ServerSideToggle, Boolean> toggles, Optional<Boolean> operatorsExempt) {
        this(fillToggleMap(toggles), operatorsExempt.orElse(false));
    }

    public List<ServerSideToggle> enabledToggles() {
        return toggles.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<ServerSideToggle> disabledToggles() {
        return toggles.entrySet().stream()
                .filter(entry -> !entry.getValue())
                .map(Map.Entry::getKey)
                .toList();
    }

    public void save() {
        try {
            Files.writeString(PATH, GSON.toJson(CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow()));
        } catch (IOException exception) {
            EclipsesTweakeroo.LOGGER.error("Failed to save config file!", exception);
        }
    }

    public static ToggleConfig read() {
        try {
            return CODEC.parse(JsonOps.INSTANCE, GSON.fromJson(Files.readString(PATH), JsonElement.class)).getOrThrow();
        } catch (IOException exception) {
            EclipsesTweakeroo.LOGGER.error("Failed to read config file!", exception);
            return DEFAULT;
        }
    }

    private static Map<ServerSideToggle, Boolean> fillToggleMap(Map<ServerSideToggle, Boolean> map) {
        Map<ServerSideToggle, Boolean> filled = new HashMap<>(map);
        for (ServerSideToggle toggle : ServerSideToggle.ALL) {
            filled.putIfAbsent(toggle, false);
        }
        return Map.copyOf(filled);
    }
}
