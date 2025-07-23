# Eclipse's Tweakeroo Additions

![Modrinth Version](https://img.shields.io/modrinth/v/6kKLK5i1?logo=modrinth&color=008800)
![Modrinth Game Versions](https://img.shields.io/modrinth/game-versions/6kKLK5i1?logo=modrinth&color=008800)
![Modrinth Downloads](https://img.shields.io/modrinth/dt/6kKLK5i1?logo=modrinth&color=008800)
[![Discord Badge](https://img.shields.io/badge/chat-discord-%235865f2)](https://discord.gg/CNNkyWRkqm)
[![Github Badge](https://img.shields.io/badge/github-eclipsestweakeroo-white?logo=github)](https://github.com/eclipseisoffline/eclipsestweakeroo)
![GitHub License](https://img.shields.io/github/license/eclipseisoffline/eclipsestweakeroo)

Eclipse's Tweakeroo Additions (often referred to as just Eclipse's Tweakeroo) is a modular utility mod for Minecraft.
It contains various tweaks and modifications, all of which can be configured and toggled to your liking. The mod does
little by default, only making the modifications you tell it to make.

Most of these modules were created just because I needed them at a time, which is why they can be quite random sometimes.
Still, I hope others may find good use out of them.

This mod is often used as a companion mod to [Tweakeroo](https://modrinth.com/mod/tweakeroo),
but can run fully independent of it as of version `0.6.3-1.21.7`.

Feel free to report any bugs, or suggest new features, at the [issue tracker](https://github.com/eclipseisoffline/eclipsestweakeroo/issues).

## License

This mod is licensed under GNU LGPLv3.

## Donating

If you like this mod, consider [donating](https://buymeacoffee.com/eclipseisoffline)!

## Discord

For support and/or any questions you may have, feel free to join [my discord](https://discord.gg/CNNkyWRkqm).

## Usage

Mod builds can be found on the releases page, as well as on [Modrinth](https://modrinth.com/mod/eclipses-tweakeroo-additions).
The [Fabric API](https://modrinth.com/mod/fabric-api) and [MaLiLib](https://modrinth.com/mod/malilib) are required dependencies.

For versions below `0.6.0-1.21.6`, Tweakeroo is a required dependency as well. The mod will add its config options to Tweakeroo's
config.

For versions `0.6.0-1.21.6` and above, the mod has its own configuration screen,
which can be opened by default by pressing `C + E`. This hotkey can be configured. The configuration screen can
also be opened via [Mod Menu](https://modrinth.com/mod/modmenu).

All the mod's modules and configuration options are listed below. Note that newer modules are only available on newer mod
versions. If a module listed below does not appear in-game, it's probably not available for your version of Minecraft.
See the table below for Minecraft version support.

### Server-side opt-in

As of version `0.6.2-1.21.7`, the mod requires a server-side opt-in for certain modules. At the moment, these include
the following:

- `tweakSlippery`
- `tweakJumpVelocity`
- `tweakBoats (boat jumping functionality)`
- `tweakBoats (spiderBoat functionality)`
- `tweakCreativeElytraFlight`
- `tweakGravity`
- `tweakStepHeight`
- `disableEntityCollisions`
- `disableKnockback`
- `disableHorseJumpCharge`
- `disableUseItemSlowdown`
- `disableJumpDelay`

These will be automatically disabled when playing on servers that do not have this mod installed. Fabric servers
can install this mod and configure which modules to enable for players in the configuration file, which can be found under
`<server>/config/eclipsestweakeroo-server.json`. You can always allow all options for operators using the `operators_exempt`
option in the aforementioned file.

The mod doesn't require MaLiLib or Tweakeroo server-side. You'll still be able to use all features when playing on singleplayer worlds.

## Version support

| Minecraft Version | Status       |
|-------------------|--------------|
| 1.21.6+7          | ✅ Current    |
| 1.21.2+3          | ✔️ Available |
| 1.21+1            | ✔️ Available |
| 1.20.5+6          | ✔️ Available |
| 1.20.4            | ✔️ Available |
| 1.20.1            | ✔️ Available |
| 1.19.4            | ✔️ Available |

I try to keep support up for the latest major and latest minor release of Minecraft. Updates to newer Minecraft
versions may be delayed from time to time, as I do not always have the time to immediately update my mods.

Unsupported versions are still available to download, but they won't receive new features (such as new modules) or bugfixes.
As such, not all modules listed below may be available to you if you're not on a current version of the mod.

## Modules

At the moment, this mod adds the modules and options described below.

### `Generic` options

The options listed below all configure Tweakeroo modules, as such these will only have an effect when Tweakeroo is installed.

- `handRestockUnstackable`, toggles whether to restock unstackable items with `tweakHandRestock`.
- `commandOnlyAdultPets`, toggles whether to only select adult pets with `sitDownNearbyPets` and `standUpNearbyPets` hotkeys.
- `fakeSneakingLadder`, toggles whether `tweakFakeSneaking` should stop you from falling down ladders.
- `permanentSneakFreeCamera`, toggles whether `tweakPermanentSneak` should work while using `tweakFreeCamera`.

### `Fixes`

- `writableBookFormattingFix`, fixes [MC-297501](https://bugs.mojang.com/browse/MC/issues/MC-297501), allowing you to use formatting codes in books again, and making them display as they would before 1.21.6. Breaks the editing cursor when placed mid-text.

The fixes listed below fix certain Tweakeroo modules, as such these will only have an effect when Tweakeroo is installed.

- `pistonFlexiblePlacementFix`, fixes inverse rotation placement of pistons, dispensers, droppers and crafters with `tweakFlexibleBlockPlacement` (allows placing these blocks facing away from you).
- `handRestockCreativeFix`, disables `tweakHandRestock` when the player is in creative mode.

### `Tweaks` 

- `tweakPlayerList`, modifies the player (tab) list in various ways, depending on how configured in the `Generic` category:
  - If `playerListHideHeader` is enabled, it hides the additional player list header some servers use.
  - If `playerListHideFooter` is enabled, it hides the additional player list footer some servers use.
  - If `playerListHideObjective` is enabled, it hides the scoreboard objective from the player list.
  - If `playerListNames` is enabled, it uses fancy names for the names that appear in the list.
  - If `playerListBelowBossbar` is enabled, the player list will be moved below all bossbars rendering.
  - `playerListOrder` configures the order in which players appear in the player list.
- `tweakPlayerNames`, enables using fancy names for the names rendered above players.
- `tweakMobNames`, enables using fancy names for the names rendered above mobs, and makes these always render.
- `tweakChatMessages`, tries to turn custom formatted chat messages into vanilla ones. It is a bit buggy at the moment, issues can occur.
- `tweakSlippery`, mostly for fun, overrides the slipperiness of every block, can be configured using the `slipperiness` generic option.
  - Can also be used to disable slipperiness altogether (including for ice blocks and such), by setting to the minimal value.
  - Also affects vehicles (such as horses and boats) you're driving if `slipperinessAffectVehicles` is enabled.
- `tweakJumpVelocity`, also mostly for fun, overrides the jump velocity setting of every block. Can be configured using the `jumpVelocity` generic option.
  - This option is set to `1.15` by default, is just enough to allow jumping over fences, walls, etc.
- `tweakDurabilityCheck`, displays a message when an item you're using, or a piece of armour you're wearing, is close to breaking.
  - Can also disable using items that are close to breaking by setting the generic option `durabilityCheckPreventUse` to `true`.
    - When to start disabling item usage can be configured using the `durabilityCheckPreventUseThreshold` option.
  - Displays a message whenever an item's durability is 10% of it's maximum durability, with a cooldown that can be configured using the `durabilityWarningCooldown` option.
- `tweakStatusEffectHud`, modifies the way status effect icons are rendered to include duration time.
- `tweakAutoReconnect`, automatically reconnects to a server after being disconnected from it (with a small delay, can be configured using the `autoReconnectTime` generic option).
- `tweakPlayerInfoNotifications`, posts notifications in chat when the client receives a [Player Info Update](https://minecraft.wiki/w/Java_Edition_protocol/Packets#Player_Info_Update) or a [Player Info Remove](https://minecraft.wiki/w/Java_Edition_protocol/Packets#Player_Info_Remove) packet. Can be configured in `Generic`:
  - If `playerAddRemoveNotification` is enabled, notifications will be posted in chat when a player info entry is added or removed.
  - If `playerGamemodeNotification` is enabled, notifications will be posted in chat when a player info entry changes gamemode.
  - If `playerListedNotification` is enabled, notifications will be posted in chat when a player info entry is added or removed from the player list.
  - If `playerDisplayNameNotification` is enabled, notifications will be posted in chat when a player info entry changes display name.
- `tweakBoats`, disables the momentum boats gain on turning and allows boats to jump 1 block high. Can be further configured in `Generic`:
  - If `spiderBoat` is enabled, boats can climb walls like spiders. This is a bit buggy, and was mostly added for fun.
  - If `boatPlayerYaw` is enabled, boats follow the crosshair of the player controlling them.
- `tweakCreativeElytraFlight`, aA
  - May be considered cheating on some servers, use at your own risk.
- `tweakRenderOperatorBlocks`, makes operator blocks (barrier, light and structure void blocks) have a texture.
- `tweakNumberHud`, replaces parts of the HUD with a number-based design. Can be further configured in `Generic`:
  - `healthWarningThreshold` determines when to start flashing the health value.
  - `hungerWarningThreshold` determines when to start flashing the hunger value.
  - `airWarningThreshold` determines when to start flashing the air value.
  - If `numberHudDurabilityWarning` is enabled, a flashing text is shown when items in your hotbar or armour slots are at low durability.
- `tweakLodestoneCompass`, allows you to see the exact position of a lodestone compass by right-clicking it with the tweak enabled.
- `tweakGravity`, allows you to override your gravity attribute. Can be configured using the `gravityOverride` generic option.
- `tweakStepHeight`, allows you to override your step height attribute. Can be configured using the `stepHeightOverride` generic option.
- `tweakHappyGhast`, doesn't do anything by default, but has various settings in `Generic` to configure:
  - `happyGhastCreativeFlight`: changes flight controls to match the ones from creative flight. Uses the same speed as normal (so compatible with the `flying_speed` attribute). Double-tap jump to dismount the ghast.
  - `hideHappyGhast`: disables rendering of the Happy Ghast when you're controlling it, so that you can see more when looking down.
  - `noHappyGhastRotation`: disables rotating the Happy Ghast when controlling it and not moving, so that you can look at other players too.
  - `happyGhastRotationLerpSpeed`: changes the rotating speed of the Happy Ghast, the higher, the faster. Default is vanilla, `1.0` is instant rotation.
- `tweakLocatorBar`, displays faces of players on the locator bar when applicable.
- `tweakPersistentChat`, keeps past chat messages across server/world switches.
- `tweakShowFormattingCodes`, renders legacy text formatting codes with a grey colour instead of hiding them. Still displays the formatting they set. Useful when writing books with formatting codes.
- `tweakMusicToast`, makes a few tweaks to the music toasts added in 1.21.6, depending on how configured in `Generic`:
  - `musicToastMusic`: shows the music toast for game music. When disabled, shows an actionbar text instead, like is normally done for jukebox records. Enabled by default.
  - `musicToastRecords`: shows the music toast for jukebox records. When disabled, shows an actionbar text instead, like is normally done. Enabled by default.
  - `musicToastPauseMenu`: whether to show music toasts in the pause menu for game music or jukebox records, when enabled for those. Enabled by default.

### `Hotkeys`

- `openConfigGui`, opens the in-game config GUI of the mod. Defaults to `C + E`.
- `insertFormattingCode`, inserts a formatting code symbol (§) on the open screen. Useful when writing formatting codes in books.

### `Yeets`

- `disableEntityCollisions`, disables entity collisions. You can still push other entities, but they can't push you.
- `disableKnockback`, disables taking knockback.
  - When `disableExplosionKnockback` in `Generic` is set to true, also disables knockback from explosions. 
- `disableFogModifiers`, disables all fog modifiers (water, lava, powdered snow, darkness, etc.).
- `disableArmorRestriction`, disables the restriction of armour slots in the inventory. May not work on servers.
- `disableBindingCurse`, disables Curse of Binding. May not work on servers.
- `disableItemCooldown`, disables the cooldown on ender pearls, goat horns, and more. May not work on servers.
- `disableIllegalCharacterCheck`, allows using special characters as U+00A7 (§), U+007F and more in all text boxes. May not work on servers.
- `disableWorldBorder`, disables all world border restrictions.
- `disableBedExplosions`, disables bed explosions in dimensions where beds don't work, by disabling clicking on them.
- `disableAllayItemUse`, disables giving items to allays.
  - When allays are dancing, you can still give them amethyst shards to duplicate them.
- `disableOffhandUse`, disables being able to use items in the offhand.
- `disableHorseJumpCharge`, disables the horse jump charge and cooldown (pressing space once immediately uses the maximum horse jump height). Also disables the camel dash cooldown.
- `disableOverlayRender` to disable rendering various overlays, such as the vignette, spyglass, carved pumpkin and powdered snow overlays.
- `disableAllNamesInF1`, hides all entity names when the HUD is disabled (vanilla Minecraft still renders the names of entities in teams).
- `disableUseItemSlowdown`, disables slowing down when using items, like eating food.
- `disableSwiftSneak`, disables sneak speed modifiers like swift sneak, because sometimes slow sneaking is nice.
- `disableJumpDelay`, disables the 10-tick delay between jumps. Allows you to jump very fast when in a low area and holding down space.
- `disableBookLineLimit`, disables the line limit of books, allowing you to write until the full limit of 1024 characters. A scroll bar will appear when writing enough lines. Note that lines will be cut off in signed books.

### Fancy names

Fancy names are modifications to names to make them include more information. They can be configured
using the `fancyNameElements` setting in the `Generic` category. Each element can either contain one placeholder, or a string to display.
The following placeholders are available:

- `{name}` - replaces with the (display)name of the mob/player.
- `{rawname}` - replaces with the raw, unformatted name/IGN of the mob/player.
- `{gamemode}` - replaces with the gamemode of the player.
- `{ping}` - replaces with the ping/latency of the player.
- `{health}` - replaces with the amount of HP of the mob/player.
- `{uuid}` - replaces with the UUID of the mob/player.
- `{team}` - replaces with the name of the team the mob/player is in, or is omitted if they aren't in any team.
- `{key}` - replaces with `KEY` or `NO KEY`, depending on whether the player has sent their public key used for signing chat messages to the server.
- `{attack}` - replaces with the current value of the `generic.attack_damage` attribute of the mob/player.
  - Shows critical damage addition (players only) as well by default, but can be disabled using the `attackPlaceholderShowCritical` generic option.
- `{armor}` - replaces with the current value of the `generic.armor` attribute, the `generic.armor_toughness` attribute, and the EPF value of the mob/player.
- `{distance}` - replaces with the distance to the mob/player.
- `{statuseffect}` - replaces with the status effects of a player/entity. Only effects with particles are visible.
- `{horsestats}` - replaces with the speed and jump attributes of a horse entity.
  - Speed is converted to m/s, the jump value is raw.
- `{rawhorsestats}` - replaces with the raw speed and jump attributes of a horse entity.

Certain placeholders, like `{healh}`, `{attack}`, etc., may only work in the player list when the player is in render distance range.
