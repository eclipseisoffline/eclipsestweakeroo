package xyz.eclipseisoffline.eclipsestweakeroo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent.Literal;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

public class FancyName {

    private static final Map<GameMode, MutableText> GAMEMODE_TEXT = Map.of(
            GameMode.SURVIVAL, MutableText.of(new Literal("S")).setStyle(Style.EMPTY.withColor(
                    Formatting.RED)),
            GameMode.CREATIVE, MutableText.of(new Literal("C")).setStyle(Style.EMPTY.withColor(
                    Formatting.GREEN)),
            GameMode.ADVENTURE, MutableText.of(new Literal("A")).setStyle(Style.EMPTY.withColor(
                    Formatting.YELLOW)),
            GameMode.SPECTATOR, MutableText.of(new Literal("SP")).setStyle(Style.EMPTY.withColor(
                    Formatting.BLUE))
    );

    private static final Map<String, BiFunction<LivingEntity, PlayerListEntry, Text>> PLACEHOLDERS = Map.of(
            "name", (livingEntity, playerListEntry) -> {
                if (livingEntity != null) {
                    return livingEntity.getDisplayName();
                } else if (playerListEntry != null) {
                    return Team.decorateName(playerListEntry.getScoreboardTeam(),
                            Text.literal(playerListEntry.getProfile().getName()));
                }
                return null;
            },
            "gamemode",
            (livingEntity, playerListEntry) -> GAMEMODE_TEXT.get(playerListEntry.getGameMode()),
            "ping", (livingEntity, playerListEntry) -> getPingText(playerListEntry.getLatency()),
            "health", (livingEntity, playerListEntry) -> MutableText.of(
                            new Literal(String.valueOf(Math.floor(livingEntity.getHealth()))))
                    .setStyle(Style.EMPTY.withColor(Formatting.RED)),
            "uuid", (livingEntity, playerListEntry) -> {
                if (livingEntity != null) {
                    return Text.of(livingEntity.getUuidAsString());
                } else if (playerListEntry != null) {
                    return Text.of(playerListEntry.getProfile().getId().toString());
                }
                return null;
            },
            "team", (livingEntity, playerListEntry) -> {
                if (livingEntity != null) {
                    return Objects.requireNonNull(livingEntity.getScoreboardTeam()).getDisplayName();
                } else if (playerListEntry != null) {
                    return Objects.requireNonNull(playerListEntry.getScoreboardTeam()).getDisplayName();
                }
                return null;
            },
            "key", (livingEntity, playerListEntry) -> playerListEntry.hasPublicKey()
                    ? MutableText.of(new Literal("KEY")).setStyle(Style.EMPTY.withColor(Formatting.GREEN))
                    : MutableText.of(new Literal("NO KEY")).setStyle(Style.EMPTY.withColor(Formatting.RED))
    );

    public static Text applyFancyName(LivingEntity entity, PlayerListEntry player) {
        MutableText fancyName = MutableText.of(Literal.EMPTY);
        List<Text> elements = new ArrayList<>();

        for (String element : AdditionalGenericConfig.FANCY_NAME_ELEMENTS.getStrings()) {
            Text elementValue = Text.of(element);
            if (element.startsWith("{")) {
                String placeholder = element.replace("{", "").replace("}", "");
                try {
                    elementValue = PLACEHOLDERS.get(placeholder).apply(entity, player);
                } catch (NullPointerException exception) {
                    continue;
                }
            }
            elements.add(elementValue);
        }

        for (int i = 0; i < elements.size(); i++) {
            fancyName.append(elements.get(i));
            if (i < elements.size() - 1) {
                fancyName.append(" - ");
            }
        }

        return fancyName;
    }

    private static Text getPingText(int ping) {
        return MutableText.of(new Literal(ping + "ms")).setStyle(getPingStyle(ping));
    }

    private static Style getPingStyle(int ping) {
        if (ping <= 0) {
            return Style.EMPTY.withColor(Formatting.DARK_GRAY);
        } else if (ping <= 150) {
            return Style.EMPTY.withColor(Formatting.GREEN);
        } else if (ping <= 300) {
            return Style.EMPTY.withColor(Formatting.YELLOW);
        } else if (ping <= 600) {
            return Style.EMPTY.withColor(Formatting.GOLD);
        } else if (ping <= 1000) {
            return Style.EMPTY.withColor(Formatting.RED);
        }

        return Style.EMPTY.withColor(Formatting.DARK_RED);
    }
}
