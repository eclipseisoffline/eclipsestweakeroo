package xyz.eclipseisoffline.eclipsestweakeroo.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigStringList;

public class AdditionalListsConfig {

    public static final ConfigStringList CHAT_MESSAGE_SEPARATORS = new ConfigStringList(
            "chatMessageSeparators",
            ImmutableList.of("» ", ": ", "> "),
            "A list of chat player name-message separators, used by tweakChatMessages to detect when a message body starts.");
}
