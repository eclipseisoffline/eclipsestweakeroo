package xyz.eclipseisoffline.eclipsestweakeroo.client.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigStringList;

import java.util.ArrayList;
import java.util.List;

public class EclipsesListsConfig {

    private static final List<IConfigBase> CONFIGS = new ArrayList<>();

    public static final ConfigStringList CHAT_MESSAGE_SEPARATORS = create("chatMessageSeparators",
            ImmutableList.of("» ", ": ", "> ")
    );

    private static ConfigStringList create(String name, ImmutableList<String> values) {
        ConfigStringList config = new ConfigStringList(name, values, EclipsesConfigs.getCommentTranslationKey(name));
        CONFIGS.add(config);
        return config;
    }

    public static List<IConfigBase> values() {
        return List.copyOf(CONFIGS);
    }
}
