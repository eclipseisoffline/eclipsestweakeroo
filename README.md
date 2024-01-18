# Eclipse's Tweakeroo Additions

This mod contains some basic modifications/additions I made to [Tweakeroo](https://www.curseforge.com/minecraft/mc-mods/tweakeroo).

## License

This mod is licensed under GNU GPLv3.

## Usage

This mod is currently available for Minecraft 1.20.1 and 1.20.4, with Tweakeroo versions 0.17.1 and 0.19.1 respectively.
All added config entries are integrated into Tweakeroo's config, the mod doesn't have a config itself.

## Developing

Tweakeroo and the Malilib version it requires are to be downloaded manually and put in the `lib`
directory, named `tweakeroo-{version}.jar` and `malilib-{version}.jar` respectively.

## Modifications

At the moment, this mod makes the following modifications:

- Added `disableEntityCollisions` yeet, which disables entity collisions. You can still push other entities, but they can't push you.
- Added `disableTabHeader` yeet, which hides the header in the player list.
- Added `disableTabFooter` yeet, which hides the footer in the player list.
- Added `handRestockUnstackable` generic option, which toggles whether to restock unstackable items with `tweakHandRestock`.
