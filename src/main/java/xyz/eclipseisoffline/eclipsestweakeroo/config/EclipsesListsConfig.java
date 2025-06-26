package xyz.eclipseisoffline.eclipsestweakeroo.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigStringList;

import java.util.ArrayList;
import java.util.List;

public class EclipsesListsConfig {

    private static final List<IConfigBase> CONFIGS = new ArrayList<>();

    public static final ConfigStringList CHAT_MESSAGE_SEPARATORS = create("chatMessageSeparators",
            ImmutableList.of("» ", ": ", "> "),
            "A list of chat player name-message separators, used by tweakChatMessages to detect when a message body starts.");

    private static ConfigStringList create(String name, ImmutableList<String> values, String comment) {
        ConfigStringList config = new ConfigStringList(name, values, comment);
        CONFIGS.add(config);
        return config;
    }

    public static List<IConfigBase> values() {
        return List.copyOf(CONFIGS);
    }
}
