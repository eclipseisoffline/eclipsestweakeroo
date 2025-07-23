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
but can run fully independent of it as of version `0.6.3`.

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

For versions below `0.6.3`, Tweakeroo is a required dependency as well.

For versions `0.6.0` and above, the mod has its own configuration screen,
which can be opened by default by pressing `C + E`. This hotkey can be configured. The configuration screen can
also be opened via [Mod Menu](https://modrinth.com/mod/modmenu). For versions below `0.6.0`, the mod will add its config options to Tweakeroo's
config.

All the mod's modules and configuration options, along with supported MC versions, are listed [here](FEATURES.md).
You can use your browser's search functionality (usually CTRL+F) to quickly look for a feature.
See the table below for more information about Minecraft version support.

### Server-side opt-in

As of version `0.6.2`, the mod requires a server-side opt-in for certain modules. When a module requires a server-side
opt-in, it is noted as such on the previously mentioned features page, and also on the in-game description.

Modules requiring a server-side opt-in will automatically be disabled when playing on servers that do not have this mod installed. Fabric servers
can install this mod and configure which modules to enable for players in the configuration file, which can be found under
`<server>/config/eclipsestweakeroo-server.json`. You can always allow all options for operators using the `operators_exempt`
option in the aforementioned file.

The mod doesn't require MaLiLib or Tweakeroo server-side. You'll still be able to use all features when playing on singleplayer worlds.

## Version support

| Minecraft Version | Status          |
|-------------------|-----------------|
| 1.21.6+7+8        | ✅ Current       |
| 1.21.5            | 🚫️ Unavailable |
| 1.21.4            | ❄️ LTS          |
| 1.21.2+3          | ✔️ Available    |
| 1.21+1            | ❄️ LTS          |
| 1.20.5+6          | ✔️ Available    |
| 1.20.4            | ✔️ Available    |
| 1.20+1            | ❄️ LTS          |
| 1.19.4            | ✔️ Available    |

I try to keep support up as much as possible for the latest release of Minecraft. This release usually has all modules.
LTS versions (currently 1.20+1, 1.21+1, and 1.21.4) usually also receive newly added features, however it may take some time
for these to be backported.

Unsupported versions are still available to download, but they won't receive new features (such as new modules) or bugfixes.
As such, not all modules listed on the features page may be available to you if you're not on a current or LTS version of the mod.
