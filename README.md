# Eclipse's Tweakeroo Additions

This mod contains some basic modifications/additions I made to [Tweakeroo](https://www.curseforge.com/minecraft/mc-mods/tweakeroo).

Feel free to report any bugs, or suggest new features, such as Tweaks, yeets, or fixes, [here](https://github.com/eclipseisoffline/eclipsestweakeroo/issues/new/choose).

## License

This mod is licensed under GNU GPLv3.

## Usage

Mod builds can be found [here](https://github.com/eclipseisoffline/eclipsestweakeroo/packages/2043865/versions).

This mod is currently available for Minecraft 1.20.1 and 1.20.4, with Tweakeroo versions 0.17.1 and 0.19.1 respectively.
All added config entries are integrated into Tweakeroo's config, the mod doesn't have a config itself.

## Modifications

At the moment, this mod makes the following modifications:

- Added `disableEntityCollisions` yeet, which disables entity collisions. You can still push other entities, but they can't push you.
- Added `tweakPlayerList` tweak, which modifies the player (tab) list in various ways, depending on how configured in the `Generic` category:
  - If `playerListHideHeader` is enabled, it hides the additional player list header some servers use.
  - If `playerListHideFooter` is enabled, it hides the additional player list footer some servers use.
  - If `playerListNames` is enabled, it uses fancy names for the names that appear in the list.
- Added `handRestockUnstackable` generic option, which toggles whether to restock unstackable items with `tweakHandRestock`.
- Added `tweakPlayerNames` tweak, which enables using fancy names for the names rendered above players.
- Added `tweakMobNames` tweak, which enables using fancy names for the names rendered above mobs, and makes these always render.
- Added `disableKnockback` yeet, which disables taking knockback.
- Added `tweakChatMessages` tweak, which tries to turn custom formatted chat messages into vanilla ones. It is a bit buggy at the moment, issues can occur.

### Fancy names

Fancy names are modifications to names to make them include more information. It can be configured
using the `fancyNameElements` setting in the `Generic` category. Each element can either contain one placeholder, or a string to display.
The following placeholders are available:

- `{name}` - replaces with the name of the mob/player.
- `{gamemode}` - replaces with the gamemode of the player.
- `{ping}` - replaces with the ping/latency of the player.
- `{health}` - replaces with the amount of HP of the mob/player.
- `{uuid}` - replaces with the UUID of the player.
