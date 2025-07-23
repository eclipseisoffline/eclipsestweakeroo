# Please note!

**The configuration file for this mod was moved out of Tweakeroo's own configuration file. All of your old
settings should move over properly when updating, but it's always good to take backups!**

---

Since version `0.5.2-1.21.1`:

- The mod no longer has a hard dependency on Tweakeroo, and can run without it. MaLiLib is still required.
- The mod can now run without MaLiLib and/or Tweakeroo on servers.
- Moved stored configuration of added tweaks, yeets, and other options into a separate file.
  - These settings are no longer stored into Tweakeroo's own configuration file, rather, the mod now creates a configuration file for itself.
  - This leads to less compatibility issues and prevents the mod's settings from being erased when loading the game without the mod.
- Moved all configuration settings into own configuration screen, which appears similar as Tweakeroo's configuration screen.
  - The new configuration screen can now be accessed by default by pressing `C + E`. This keybind can be changed in the `Hotkeys` section.
  - The configuration screen can also be accessed via ModMenu.



- Added the following tweaks:
  - `tweakPersistentChat`: keeps past chat messages across server/world switches.
  - `tweakShowFormattingCodes`: renders legacy text formatting codes with a grey colour instead of hiding them. Still displays the formatting they set. Useful when writing books with formatting codes.
- Added the following yeets:
  - `disableUseItemSlowdown`: disables slowing down when using items, like eating food.
  - `disableSwiftSneak`: disables sneak speed modifiers like swift sneak, because sometimes slow sneaking is nice.
  - `disableJumpDelay`: disables the 10-tick delay between jumps. Allows you to jump very fast when in a low area and holding down space. Requires a server-side opt-in.
- Added the following hotkeys:
  - `insertFormattingCode`: inserts a formatting code symbol (§) on the open screen. Useful when writing formatting codes in books.



- The `{distance}` placeholder now properly rounds to one decimal.
- Fixed `slipperinessAffectVehicles` setting.
- Fixed a crash that could occur when `tweakRenderOperatorBlocks` is enabled.
- Fixed issues with `tweakCreativeElytraFlight`, and added proper switching between creative and Elytra flight:
  - When enabling the tweak whilst flying with Elytra, you'll automatically switch to creative flight.
  - When disabling the tweak or cancelling creative flight, you'll automatically switch back to Elytra flight.
- Cleaned up a lot of code, leading to a cleaner codebase to allow faster updates/backports.


A server-side opt-in is now required for the following features:

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

These features will be automatically disabled when playing on servers that do not have this mod installed. Fabric servers
can install this mod and configure which features to enable for players in the configuration file, which can be found under
`<server>/config/eclipsestweakeroo-server.json`. You can always allow all features for operators using the `operators_exempt`
option in the aforementioned file.

You'll still be able to use all features when playing on singleplayer worlds.
