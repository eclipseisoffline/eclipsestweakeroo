package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.client.multiplayer.PlayerInfo;

public enum PlayerListOrder implements IConfigOptionListEntry {
    DEFAULT("default", "Default", null),
    PING("ping", "Ping", Comparator.comparingInt(PlayerInfo::getLatency)),
    GAMEMODE("gamemode", "Gamemode", Comparator.comparingInt(entry -> entry.getGameMode().getId())),
    NAME("name", "Name",
            Comparator.comparing(entry -> entry.getProfile().getName(), String::compareTo));

    private final String stringName;
    private final String displayName;
    private final Comparator<PlayerInfo> comparator;

    PlayerListOrder(String stringName, String displayName,
            Comparator<PlayerInfo> comparator) {
        this.stringName = stringName;
        this.displayName = displayName;
        this.comparator = comparator;
    }

    @Override
    public String getStringValue() {
        return stringName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
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
        return Arrays.stream(values()).filter(orderType -> orderType.stringName.equals(name))
                .findFirst().orElse(null);
    }
}
