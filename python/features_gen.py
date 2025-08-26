# Helper script to generate the FEATURES.md file

HEADER = """# Features

Available features for all current and LTS versions are listed below. Note that not all features are available for all versions.
"""

TWEAKEROO_REQUIREMENT = """
> [!NOTE]
> This feature depends on Tweakeroo functionality. It will only be functional when Tweakeroo is also installed.
"""

SERVER_SIDE_OPT_IN = """
> [!WARNING]
> This feature requires a server-side opt-in. It will automatically be disabled when playing on multiplayer servers.
> A server administrator can install the mod on their server to enable usage of this feature.
"""

MC_ONE_TWENTY = "1.20+1"
MC_ONE_TWENTY_ONE = "1.21+1"
MC_ONE_TWENTY_ONE_FOUR = "1.21.4"
MC_ONE_TWENTY_ONE_SIX = "1.21.6+7+8"
LTS_VERSIONS = [MC_ONE_TWENTY, MC_ONE_TWENTY_ONE, MC_ONE_TWENTY_ONE_FOUR, MC_ONE_TWENTY_ONE_SIX]


class Feature:

    def __init__(self, name: str, description: str, *args, requires_tweakeroo: bool = False, server_side: bool = False):
        self.name = name
        self.description = description
        self.requires_tweakeroo = requires_tweakeroo
        self.server_side = server_side

        self.supported_versions = LTS_VERSIONS if not args else args

    def build(self) -> str:
        return f"""
### `{self.name}`
{SERVER_SIDE_OPT_IN if self.server_side else ""}{TWEAKEROO_REQUIREMENT if self.requires_tweakeroo else ""}
{self.description}
{Feature.__build_supported_versions_table(self.supported_versions)}
"""

    @staticmethod
    def __build_supported_versions_table(supported_versions: list[str]) -> str:
        table = """
#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
"""
        for version in reversed(LTS_VERSIONS):
            table += f"| {version} | {'✔️ Yes' if version in supported_versions else '❌ No'} |\n"

        return table.removesuffix("\n")


class Category:

    def __init__(self, name: str):
        self.name = name
        self.features: list[Feature] = []

    def add(self, name: str, description: str, *args, requires_tweakeroo: bool = False, server_side: bool = False):
        self.features.append(Feature(name, description, *args, requires_tweakeroo=requires_tweakeroo, server_side=server_side))

    def build(self) -> str:
        return f"""
## `{self.name}`
{"".join([feature.build() for feature in self.features])}"""


def generate_generic() -> Category:
    generic = Category("Generic")

    generic.add("handRestockUnstackable", "Toggles whether to restock unstackable items when `tweakHandRestock` is enabled.", requires_tweakeroo=True)
    generic.add("commandOnlyAdultPets", "Toggles whether to only select adult pets with `sitDownNearbyPets` and `standUpNearbyPets` hotkeys.", requires_tweakeroo=True)
    generic.add("fakeSneakingLadder", "Toggles whether `tweakFakeSneaking` should stop you from falling down ladders.", requires_tweakeroo=True)
    generic.add("permanentSneakFreeCamera", "Toggles whether `tweakPermanentSneak` should work while using `tweakFreeCamera`.", requires_tweakeroo=True)
    generic.add("toolSwitchBack", "Whether to switch back to the original hotbar slot and item after breaking blocks with tweakToolSwitch enabled", MC_ONE_TWENTY_ONE_SIX, requires_tweakeroo=True)

    return generic


def generate_fixes() -> Category:
    fixes = Category("Fixes")

    fixes.add("pistonFlexiblePlacementFix", "Fixes inverse rotation placement of pistons, dispensers, droppers and crafters with `tweakFlexibleBlockPlacement` (allows placing these blocks facing away from you).", requires_tweakeroo=True)
    fixes.add("handRestockCreativeFix", "Disables `tweakHandRestock` when the player is in creative mode.", requires_tweakeroo=True)
    fixes.add("`writableBookFormattingFix`", "Fixes [MC-297501](https://bugs.mojang.com/browse/MC/issues/MC-297501), allowing you to use formatting codes in books again, and making them display as they would before 1.21.6. Breaks the editing cursor when placed mid-text.", MC_ONE_TWENTY_ONE_SIX)

    return fixes


def generate_tweaks() -> Category:
    tweaks = Category("Tweaks")

    tweaks.add("tweakPlayerList", """Modifies the player (tab) list in various ways, depending on how configured in the `Generic` category:

- If `playerListHideHeader` is enabled, it hides the additional player list header some servers use.
- If `playerListHideFooter` is enabled, it hides the additional player list footer some servers use.
- If `playerListHidePing` is enabled, it hides the ping bars from the player list.
- If `playerListHideObjective` is enabled, it hides the scoreboard objective from the player list.
- If `playerListNames` is enabled, it uses fancy names for the names that appear in the list.
- If `playerListBelowBossbar` is enabled, the player list will be moved below all bossbars rendering.
- `playerListOrder` configures the order in which players appear in the player list.""")
    tweaks.add("tweakPlayerNames", "Enables using fancy names for the names rendered above players.")
    tweaks.add("tweakMobNames", "Enables using fancy names for the names rendered above mobs, and makes these always render.")
    tweaks.add("tweakChatMessages", "Tries to turn custom formatted chat messages into vanilla ones. It is a bit buggy at the moment, issues can occur.")

    tweaks.add("tweakSlippery", """Mostly for fun, overrides the slipperiness of every block, can be configured using the `slipperiness` generic option.

- Can also be used to disable slipperiness altogether (including for ice blocks and such), by setting to the minimal value.
- Also affects vehicles (such as horses and boats) you're driving if `slipperinessAffectVehicles` is enabled.""", server_side=True)
    tweaks.add("tweakJumpVelocity", """Mostly for fun, overrides the jump velocity setting of every block. Can be configured using the `jumpVelocity` generic option.

- This option is set to `1.15` by default, is just enough to allow jumping over fences, walls, etc.""", server_side=True)

    tweaks.add("tweakDurabilityCheck", """Displays a message when an item you're using, or a piece of armour you're wearing, is close to breaking.
- Can also disable using items that are close to breaking by setting the generic option `durabilityCheckPreventUse` to `true`.
  - When to start disabling item usage can be configured using the `durabilityCheckPreventUseThreshold` option.
- Displays a message whenever an item's durability is 10% of it's maximum durability, with a cooldown that can be configured using the `durabilityWarningCooldown` option.""")
    tweaks.add("tweakStatusEffectHud", "Modifies the way status effect icons are rendered to include duration time.")
    tweaks.add("tweakAutoReconnect", "Automatically reconnects to a server after being disconnected from it (with a small delay, can be configured using the `autoReconnectTime` generic option).")

    tweaks.add("tweakPlayerInfoNotifications", """Posts notifications in chat when the client receives a [Player Info Update](https://minecraft.wiki/w/Java_Edition_protocol/Packets#Player_Info_Update) or a [Player Info Remove](https://minecraft.wiki/w/Java_Edition_protocol/Packets#Player_Info_Remove) packet. Can be configured in `Generic`:

- If `playerAddRemoveNotification` is enabled, notifications will be posted in chat when a player info entry is added or removed.
- If `playerGamemodeNotification` is enabled, notifications will be posted in chat when a player info entry changes gamemode.
- If `playerListedNotification` is enabled, notifications will be posted in chat when a player info entry is added or removed from the player list.
- If `playerDisplayNameNotification` is enabled, notifications will be posted in chat when a player info entry changes display name.""")

    tweaks.add("tweakBoats", """Disables the momentum boats gain on turning and allows boats to jump 1 block high. Can be further configured in `Generic`:

- If `spiderBoat` is enabled, boats can climb walls like spiders. This is a bit buggy, and was mostly added for fun.
- If `boatPlayerYaw` is enabled, boats follow the crosshair of the player controlling them.

The boat jumping and spider boat features require a server-side opt-in, other features can be used without one.""", server_side=True)

    tweaks.add("tweakCreativeElytraFlight", "Allows creative elytra flight in survival while using an elytra.", server_side=True)
    tweaks.add("tweakRenderOperatorBlocks", "Makes operator blocks (barrier, light and structure void blocks) have a texture.")

    tweaks.add("tweakNumberHud", """Replaces parts of the HUD with a number-based design. Can be further configured in `Generic`:

- `healthWarningThreshold` determines when to start flashing the health value.
- `hungerWarningThreshold` determines when to start flashing the hunger value.
- `airWarningThreshold` determines when to start flashing the air value.
- If `numberHudDurabilityWarning` is enabled, a flashing text is shown when items in your hotbar or armour slots are at low durability.""")

    tweaks.add("tweakLodestoneCompass", "Allows you to see the exact position of a lodestone compass by right-clicking it with the tweak enabled.")
    tweaks.add("tweakGravity", "Allows you to override your gravity attribute. Can be configured using the `gravityOverride` generic option.", server_side=True)
    tweaks.add("tweakStepHeight", "Allows you to override your step height attribute. Can be configured using the `stepHeightOverride` generic option.", server_side=True)

    tweaks.add("tweakHappyGhast", """Doesn't do anything by default, but has various settings in `Generic` to configure:

- `happyGhastCreativeFlight`: changes flight controls to match the ones from creative flight. Uses the same speed as normal (so compatible with the `flying_speed` attribute). Double-tap jump to dismount the ghast.
- `hideHappyGhast`: disables rendering of the Happy Ghast when you're controlling it, so that you can see more when looking down.
- `noHappyGhastRotation`: disables rotating the Happy Ghast when controlling it and not moving, so that you can look at other players too.
- `happyGhastRotationLerpSpeed`: changes the rotating speed of the Happy Ghast, the higher, the faster. Default is vanilla, `1.0` is instant rotation.""", MC_ONE_TWENTY_ONE_SIX)

    tweaks.add("tweakLocatorBar", "Displays faces of players on the locator bar when applicable.", MC_ONE_TWENTY_ONE_SIX)
    tweaks.add("tweakPersistentChat", "Keeps past chat messages across server/world switches.")
    tweaks.add("tweakShowFormattingCodes", "Renders legacy text formatting codes with a grey colour instead of hiding them. Still displays the formatting they set. Useful when writing books with formatting codes.")

    tweaks.add("tweakMusicToast", """Makes a few tweaks to the music toasts added in 1.21.6, depending on how configured in `Generic`:

- `musicToastMusic`: shows the music toast for game music. When disabled, shows an actionbar text instead, like is normally done for jukebox records. Enabled by default.
- `musicToastRecords`: shows the music toast for jukebox records. When disabled, shows an actionbar text instead, like is normally done. Enabled by default.
- `musicToastPauseMenu`: whether to show music toasts in the pause menu for game music or jukebox records, when enabled for those. Enabled by default.""", MC_ONE_TWENTY_ONE_SIX)
    tweaks.add("tweakInvertMouseX", "Inverts mouse movement on the X-axis. Backport of the similar option introduced in 25w31a", MC_ONE_TWENTY_ONE_SIX)

    return tweaks


def generate_hotkeys() -> Category:
    hotkeys = Category("Hotkeys")

    hotkeys.add("openConfigGui", "Opens the in-game config GUI of the mod. Defaults to `C + E`.")
    hotkeys.add("insertFormattingCode", "Inserts a formatting code symbol (§) on the open screen. Useful when writing formatting codes in books.")

    return hotkeys


def generate_disables() -> Category:
    disables = Category("Yeets")

    disables.add("disableEntityCollisions", "Disables entity collisions. You can still push other entities, but they can't push you.", server_side=True)
    disables.add("disableKnockback", """Disables taking knockback.

- When `disableExplosionKnockback` in `Generic` is set to true, also disables knockback from explosions. """, server_side=True)
    disables.add("disableFogModifiers", "Disables all fog modifiers (water, lava, powdered snow, darkness, etc.).")
    disables.add("disableArmorRestriction", "Disables the restriction of armour slots in the inventory. May not work on servers.")
    disables.add("disableBindingCurse", "Disables Curse of Binding. May not work on servers.")
    disables.add("disableItemCooldown", "Disables the cooldown on ender pearls, goat horns, and more. May not work on servers.")
    disables.add("disableIllegalCharacterCheck", "Allows using special characters as U+00A7 (§), U+007F and more in all text boxes. May not work on servers.")
    disables.add("disableWorldBorder", "Disables all world border restrictions.")
    disables.add("disableBedExplosions", "Disables bed explosions in dimensions where beds don't work, by disabling clicking on them.")
    disables.add("disableAllayItemUse", """Disables giving items to allays.

- When allays are dancing, you can still give them amethyst shards to duplicate them.""")
    disables.add("disableOffhandUse", "Disables being able to use items in the offhand.")
    disables.add("disableHorseJumpCharge", "Disables the horse jump charge and cooldown (pressing space once immediately uses the maximum horse jump height). Also disables the camel dash cooldown.", server_side=True)
    disables.add("disableOverlayRender", "Disables rendering various overlays, such as the vignette, spyglass, carved pumpkin and powdered snow overlays.")
    disables.add("disableAllNamesInF1", "Hides all entity names when the HUD is disabled (vanilla Minecraft still renders the names of entities in teams).")
    disables.add("disableUseItemSlowdown", "Disables slowing down when using items, like eating food.", server_side=True)
    disables.add("disableSwiftSneak", "Disables sneak speed modifiers like swift sneak, because sometimes slow sneaking is nice.")
    disables.add("disableJumpDelay", "Disables the 10-tick delay between jumps. Allows you to jump very fast when in a low area and holding down space.", server_side=True)
    disables.add("disableBookLineLimit", "Disables the line limit of books, allowing you to write until the full limit of 1024 characters. A scroll bar will appear when writing enough lines. Note that lines will be cut off in signed books.", MC_ONE_TWENTY_ONE_SIX)
    disables.add("disableBabyFeeding", "Disables feeding of baby animals, making breeding animals easier when there are a lot of animals in a small space", MC_ONE_TWENTY_ONE_SIX)

    return disables


def generate_all() -> str:
    generated = HEADER

    generated += generate_generic().build()
    generated += generate_fixes().build()
    generated += generate_tweaks().build()
    generated += generate_hotkeys().build()
    generated += generate_disables().build()

    return generated


if __name__ == '__main__':
    with open("FEATURES.md", "w") as output:
        output.write(generate_all())
