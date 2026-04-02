package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import java.util.Arrays;
import java.util.Comparator;

import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.util.Util;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;

public enum PlayerListOrder implements IConfigOptionListEntry {
    DEFAULT("default", null),
    PING("ping", Comparator.comparingInt(PlayerInfo::getLatency)),
    GAMEMODE("gamemode", Comparator.comparingInt(entry -> entry.getGameMode().getId())),
    NAME("name",
            Comparator.comparing(entry -> entry.getProfile().name(), String::compareTo));

    private final String name;
    private final String displayName;
    private final Comparator<PlayerInfo> comparator;

    PlayerListOrder(String name, Comparator<PlayerInfo> comparator) {
        this.name = name;
        this.displayName = Util.makeDescriptionId("config.player_list_order", EclipsesTweakeroo.getModdedIdentifier(name));
        this.comparator = comparator;
    }

    @Override
    public String getStringValue() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return StringUtils.translate(displayName);
    }

    public Comparator<PlayerInfo> getComparator() {
        return comparator;
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
        int index = ordinal();
        if (forward) {
            index++;
            if (index >= values().length) {
                index = 0;
            }
        } else {
            index--;
            if (index < 0) {
                index = values().length - 1;
            }
        }

        return values()[index];
    }

    @Override
    public IConfigOptionListEntry fromString(String name) {
        return Arrays.stream(values()).filter(orderType -> orderType.name.equals(name))
                .findFirst().orElse(null);
    }
}
