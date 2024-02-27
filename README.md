# Eclipse's Tweakeroo Additions

This mod contains some basic modifications/additions I made to [Tweakeroo](https://www.curseforge.com/minecraft/mc-mods/tweakeroo).

Feel free to report any bugs, or suggest new features, such as tweaks, yeets, or fixes, [here](https://github.com/eclipseisoffline/eclipsestweakeroo/issues/new/choose).

## License

This mod is licensed under GNU GPLv3.

## Donating

If you like this mod, consider [donating](https://ko-fi.com/eclipseisoffline)!
It really helps me a ton!

## Usage

Mod builds can be found [here](https://github.com/eclipseisoffline/eclipsestweakeroo/packages/2043865/versions)
and on [Modrinth](https://modrinth.com/mod/eclipses-tweakeroo-additions).

This mod is currently available for Minecraft 1.19.4, 1.20.1 and 1.20.4, with Tweakeroo versions 0.16.0,
0.17.1 and 0.19.2 respectively.  All added config entries are integrated into Tweakeroo's config,
the mod doesn't have a config itself.

## Modifications

At the moment, this mod makes the following modifications:

- Added `handRestockUnstackable` generic option, which toggles whether to restock unstackable items with `tweakHandRestock`.
- Added `commandOnlyAdultPets` generic option, which toggles whether to only select adult pets with `sitDownNearbyPets` and `standUpNearbyPets` hotkeys.
- Added `gammaOverrideFix` fix, which fixes gamma override not applying when relaunching the game.
- Added `tweakPlayerList` tweak, which modifies the player (tab) list in various ways, depending on how configured in the `Generic` category:
  - If `playerListHideHeader` is enabled, it hides the additional player list header some servers use.
  - If `playerListHideFooter` is enabled, it hides the additional player list footer some servers use.
  - If `playerListHideObjective` is enabled, it hides the scoreboard objective from the player list.
  - If `playerListNames` is enabled, it uses fancy names for the names that appear in the list.
  - `playerListOrder` configures the order in which players appear in the player list.
- Added `tweakPlayerNames` tweak, which enables using fancy names for the names rendered above players.
- Added `tweakMobNames` tweak, which enables using fancy names for the names rendered above mobs, and makes these always render.
- Added `tweakChatMessages` tweak, which tries to turn custom formatted chat messages into vanilla ones. It is a bit buggy at the moment, issues can occur.
- Added `tweakSlippery` tweak, mostly for fun, which overrides the slipperiness of every block, can be configured using the `slipperiness` generic option.
  - Can also be used to disable slipperiness altogether (including for ice blocks and such), by setting to the minimal value.
  - Also affects vehicles (such as horses and boats) you're driving if `slipperinessAffectVehicles` is enabled.
- Added `tweakJumpVelocity` tweak, also mostly for fun, which overrides the jump velocity setting of every block. Can be configured using the `jumpVelocity` generic option.
  - This option is set to `1.15` by default, which is just enough to allow jumping over fences, walls, etc.
- Added `tweakDurabilityCheck` tweak, which displays a message when an item you're using, or a piece of armour you're wearing, is close to breaking.
  - Can also disable using items that are close to breaking by setting the generic option `durabilityCheckPreventUse` to `true`.
    - When to start disabling item usage can be configured using the `durabilityCheckPreventUseThreshold` option.
  - Displays a message whenever an item's durability is 10% of it's maximum durability, with a cooldown that can be configured using the `durabilityWarningCooldown` option.
- Added `tweakStatusEffectHud` tweak, which modifies the way status effect icons are rendered to include duration time.
- Added `tweakAutoReconnect` tweak, which automatically reconnects to a server after being disconnected from it (with a small delay, can be configured using the `autoReconnectTime` generic option).
- Added `tweakPlayerInfoNotifications` tweak, which posts notifications in chat when the client receives a [Player Info Update](https://wiki.vg/Protocol#Player_Info_Update) or a [Player Info Remove](https://wiki.vg/Protocol#Player_Info_Remove) packet. Can be configured in `Generic`:
  - If `playerAddRemoveNotification` is enabled, notifications will be posted in chat when a player info entry is added or removed.
  - If `playerGamemodeNotification` is enabled, notifications will be posted in chat when a player info entry changes gamemode.
  - If `playerListedNotification` is enabled, notifications will be posted in chat when a player info entry is added or removed from the player list.
  - If `playerDisplayNameNotification` is enabled, notifications will be posted in chat when a player info entry changes display name.
- Added `tweakBoats`, which disables the momentum boats gain on turning and allows boats to jump 1 block high.
- Added `disableEntityCollisions` yeet, which disables entity collisions. You can still push other entities, but they can't push you.
- Added `disableKnockback` yeet, which disables taking knockback.
- Added `disableFogModifiers` yeet, which disables all fog modifiers (water, lava, powdered snow, darkness, etc.).
- Added `disableArmorRestriction` yeet, which disables the restriction of armour slots in the inventory. May not work on servers.
- Added `disableBindingCurse` yeet, which disables Curse of Binding. May not work on servers.
- Added `disableItemCooldown` yeet, which disables the cooldown on ender pearls, goat horns, and more. May not work on servers.
- Added `disableIllegalCharacterCheck` yeet, which allows using special characters as U+00A7 (§), U+007F and more in all text boxes. May not work on servers.
- Added `disableWorldBorder` yeet, which disables all world border restrictions.
- Added `disableBedExplosions` yeet, which disables bed explosions in dimensions where beds don't work, by disabling clicking on them.

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
- `{armor}` - replaces with the current value of the `generic.armor` attribute of the mob/player.
- `{distance}` - replaces with the distance to the mob/player.
- `{statuseffect}` - replaces with the status effects of a player/entity.
  - As of right now, only works in singleplayer.
- `{horsestats}` - replaces with the speed and jump attributes of a horse entity.
  - Speed is converted to m/s, the jump value is raw.
- `{rawhorsestats}` - replaces with the raw speed and jump attributes of a horse entity.

Certain placeholders, like `{healh}`, `{attack}`, etc., may only work in the player list when the player is in render distance range.
