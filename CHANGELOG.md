- Fixed the options `durabilityCheckPreventUseThreshold`, `healthWarningThreshold`, `hungerWarningThreshold`, `airWarningThreshold`, `gravityOverride`, `stepHeightOverride` not being registered properly.
- Updated to 1.21.6.
- Moved stored configuration of added tweaks, yeets, and other options into a separate file.
    - These settings are no longer stored into Tweakeroo's own configuration file, rather, the mod now creates a configuration file for itself.
    - This leads to less compatibility issues and prevents the mod's settings from being erased when loading the game without the mod.
- Moved all configuration settings into own configuration screen, which appears similar as Tweakeroo's configuration screen.
    - The new configuration screen can now be accessed by default by pressing `C + E`. This keybind can be changed in the `Hotkeys` section.
    - The configuration screen can also be accessed via ModMenu.
- Added `tweakHappyGhast`:
    - Doesn't do anything by default, but has various settings in `Generic` to configure:
    - `happyGhastCreativeFlight`: changes flight controls to match the ones from creative flight. Uses the same speed as normal (so compatible with the `flying_speed` attribute). Double-tap jump to dismount the ghast.
    - `hideHappyGhast`: disables rendering of the Happy Ghast when you're controlling it, so that you can see more when looking down.
    - `noHappyGhastRotation`: disables rotating the Happy Ghast when controlling it and not moving, so that you can look at other players too.
    - `happyGhastRotationLerpSpeed`: changes the rotating speed of the Happy Ghast, the higher, the faster. Default is vanilla, `1.0` is instant rotation.
- Added `tweakLocatorBar`, which displays faces of players on the locator bar when applicable.
- Added `disableUseItemSlowdown`, which disables slowing down when using items, like eating food.
- Added `disableSwiftSneak`, which disables sneak speed modifiers like swift sneak, because sometimes slow sneaking is nice.
- Fixed issues with `tweakCreativeElytraFlight`, and added proper switching between creative and Elytra flight:
    - When enabling the tweak whilst flying with Elytra, you'll automatically switch to creative flight.
    - When disabling the tweak or cancelling creative flight, you'll automatically switch back to Elytra flight.
- Fixed `tweakSlippery` not working correctly with vehicles.
- The `{distance}` name placeholder now properly rounds to one decimal.
- Cleaned up a lot of code, leading to a cleaner codebase to allow faster updates.

**Please note!** The configuration file for this mod was moved out of Tweakeroo's own configuration file. All of your old
settings should move over properly when updating, but it's always good to take backups!
