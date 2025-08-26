# Features

Available features for all current and LTS versions are listed below. Note that not all features are available for all versions.

## `Generic`

### `handRestockUnstackable`

> [!NOTE]
> This feature depends on Tweakeroo functionality. It will only be functional when Tweakeroo is also installed.

Toggles whether to restock unstackable items when `tweakHandRestock` is enabled.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `commandOnlyAdultPets`

> [!NOTE]
> This feature depends on Tweakeroo functionality. It will only be functional when Tweakeroo is also installed.

Toggles whether to only select adult pets with `sitDownNearbyPets` and `standUpNearbyPets` hotkeys.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `fakeSneakingLadder`

> [!NOTE]
> This feature depends on Tweakeroo functionality. It will only be functional when Tweakeroo is also installed.

Toggles whether `tweakFakeSneaking` should stop you from falling down ladders.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `permanentSneakFreeCamera`

> [!NOTE]
> This feature depends on Tweakeroo functionality. It will only be functional when Tweakeroo is also installed.

Toggles whether `tweakPermanentSneak` should work while using `tweakFreeCamera`.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `toolSwitchBack`

> [!NOTE]
> This feature depends on Tweakeroo functionality. It will only be functional when Tweakeroo is also installed.

Whether to switch back to the original hotbar slot and item after breaking blocks with tweakToolSwitch enabled

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ❌ No |
| 1.21+1 | ❌ No |
| 1.20+1 | ❌ No |

## `Fixes`

### `pistonFlexiblePlacementFix`

> [!NOTE]
> This feature depends on Tweakeroo functionality. It will only be functional when Tweakeroo is also installed.

Fixes inverse rotation placement of pistons, dispensers, droppers and crafters with `tweakFlexibleBlockPlacement` (allows placing these blocks facing away from you).

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `handRestockCreativeFix`

> [!NOTE]
> This feature depends on Tweakeroo functionality. It will only be functional when Tweakeroo is also installed.

Disables `tweakHandRestock` when the player is in creative mode.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### ``writableBookFormattingFix``

Fixes [MC-297501](https://bugs.mojang.com/browse/MC/issues/MC-297501), allowing you to use formatting codes in books again, and making them display as they would before 1.21.6. Breaks the editing cursor when placed mid-text.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ❌ No |
| 1.21+1 | ❌ No |
| 1.20+1 | ❌ No |

## `Tweaks`

### `tweakPlayerList`

Modifies the player (tab) list in various ways, depending on how configured in the `Generic` category:

- If `playerListHideHeader` is enabled, it hides the additional player list header some servers use.
- If `playerListHideFooter` is enabled, it hides the additional player list footer some servers use.
- If `playerListHidePing` is enabled, it hides the ping bars from the player list.
- If `playerListHideObjective` is enabled, it hides the scoreboard objective from the player list.
- If `playerListNames` is enabled, it uses fancy names for the names that appear in the list.
- If `playerListBelowBossbar` is enabled, the player list will be moved below all bossbars rendering.
- `playerListOrder` configures the order in which players appear in the player list.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakPlayerNames`

Enables using fancy names for the names rendered above players.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakMobNames`

Enables using fancy names for the names rendered above mobs, and makes these always render.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakChatMessages`

Tries to turn custom formatted chat messages into vanilla ones. It is a bit buggy at the moment, issues can occur.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakSlippery`

> [!WARNING]
> This feature requires a server-side opt-in. It will automatically be disabled when playing on multiplayer servers.
> A server administrator can install the mod on their server to enable usage of this feature.

Mostly for fun, overrides the slipperiness of every block, can be configured using the `slipperiness` generic option.

- Can also be used to disable slipperiness altogether (including for ice blocks and such), by setting to the minimal value.
- Also affects vehicles (such as horses and boats) you're driving if `slipperinessAffectVehicles` is enabled.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakJumpVelocity`

> [!WARNING]
> This feature requires a server-side opt-in. It will automatically be disabled when playing on multiplayer servers.
> A server administrator can install the mod on their server to enable usage of this feature.

Mostly for fun, overrides the jump velocity setting of every block. Can be configured using the `jumpVelocity` generic option.

- This option is set to `1.15` by default, is just enough to allow jumping over fences, walls, etc.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakDurabilityCheck`

Displays a message when an item you're using, or a piece of armour you're wearing, is close to breaking.
- Can also disable using items that are close to breaking by setting the generic option `durabilityCheckPreventUse` to `true`.
  - When to start disabling item usage can be configured using the `durabilityCheckPreventUseThreshold` option.
- Displays a message whenever an item's durability is 10% of it's maximum durability, with a cooldown that can be configured using the `durabilityWarningCooldown` option.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakStatusEffectHud`

Modifies the way status effect icons are rendered to include duration time.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakAutoReconnect`

Automatically reconnects to a server after being disconnected from it (with a small delay, can be configured using the `autoReconnectTime` generic option).

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakPlayerInfoNotifications`

Posts notifications in chat when the client receives a [Player Info Update](https://minecraft.wiki/w/Java_Edition_protocol/Packets#Player_Info_Update) or a [Player Info Remove](https://minecraft.wiki/w/Java_Edition_protocol/Packets#Player_Info_Remove) packet. Can be configured in `Generic`:

- If `playerAddRemoveNotification` is enabled, notifications will be posted in chat when a player info entry is added or removed.
- If `playerGamemodeNotification` is enabled, notifications will be posted in chat when a player info entry changes gamemode.
- If `playerListedNotification` is enabled, notifications will be posted in chat when a player info entry is added or removed from the player list.
- If `playerDisplayNameNotification` is enabled, notifications will be posted in chat when a player info entry changes display name.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakBoats`

> [!WARNING]
> This feature requires a server-side opt-in. It will automatically be disabled when playing on multiplayer servers.
> A server administrator can install the mod on their server to enable usage of this feature.

Disables the momentum boats gain on turning and allows boats to jump 1 block high. Can be further configured in `Generic`:

- If `spiderBoat` is enabled, boats can climb walls like spiders. This is a bit buggy, and was mostly added for fun.
- If `boatPlayerYaw` is enabled, boats follow the crosshair of the player controlling them.

The boat jumping and spider boat features require a server-side opt-in, other features can be used without one.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakCreativeElytraFlight`

> [!WARNING]
> This feature requires a server-side opt-in. It will automatically be disabled when playing on multiplayer servers.
> A server administrator can install the mod on their server to enable usage of this feature.

Allows creative elytra flight in survival while using an elytra.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakRenderOperatorBlocks`

Makes operator blocks (barrier, light and structure void blocks) have a texture.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakNumberHud`

Replaces parts of the HUD with a number-based design. Can be further configured in `Generic`:

- `healthWarningThreshold` determines when to start flashing the health value.
- `hungerWarningThreshold` determines when to start flashing the hunger value.
- `airWarningThreshold` determines when to start flashing the air value.
- If `numberHudDurabilityWarning` is enabled, a flashing text is shown when items in your hotbar or armour slots are at low durability.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakLodestoneCompass`

Allows you to see the exact position of a lodestone compass by right-clicking it with the tweak enabled.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakGravity`

> [!WARNING]
> This feature requires a server-side opt-in. It will automatically be disabled when playing on multiplayer servers.
> A server administrator can install the mod on their server to enable usage of this feature.

Allows you to override your gravity attribute. Can be configured using the `gravityOverride` generic option.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakStepHeight`

> [!WARNING]
> This feature requires a server-side opt-in. It will automatically be disabled when playing on multiplayer servers.
> A server administrator can install the mod on their server to enable usage of this feature.

Allows you to override your step height attribute. Can be configured using the `stepHeightOverride` generic option.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakHappyGhast`

Doesn't do anything by default, but has various settings in `Generic` to configure:

- `happyGhastCreativeFlight`: changes flight controls to match the ones from creative flight. Uses the same speed as normal (so compatible with the `flying_speed` attribute). Double-tap jump to dismount the ghast.
- `hideHappyGhast`: disables rendering of the Happy Ghast when you're controlling it, so that you can see more when looking down.
- `noHappyGhastRotation`: disables rotating the Happy Ghast when controlling it and not moving, so that you can look at other players too.
- `happyGhastRotationLerpSpeed`: changes the rotating speed of the Happy Ghast, the higher, the faster. Default is vanilla, `1.0` is instant rotation.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ❌ No |
| 1.21+1 | ❌ No |
| 1.20+1 | ❌ No |

### `tweakLocatorBar`

Displays faces of players on the locator bar when applicable.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ❌ No |
| 1.21+1 | ❌ No |
| 1.20+1 | ❌ No |

### `tweakPersistentChat`

Keeps past chat messages across server/world switches.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakShowFormattingCodes`

Renders legacy text formatting codes with a grey colour instead of hiding them. Still displays the formatting they set. Useful when writing books with formatting codes.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `tweakMusicToast`

Makes a few tweaks to the music toasts added in 1.21.6, depending on how configured in `Generic`:

- `musicToastMusic`: shows the music toast for game music. When disabled, shows an actionbar text instead, like is normally done for jukebox records. Enabled by default.
- `musicToastRecords`: shows the music toast for jukebox records. When disabled, shows an actionbar text instead, like is normally done. Enabled by default.
- `musicToastPauseMenu`: whether to show music toasts in the pause menu for game music or jukebox records, when enabled for those. Enabled by default.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ❌ No |
| 1.21+1 | ❌ No |
| 1.20+1 | ❌ No |

### `tweakInvertMouseX`

Inverts mouse movement on the X-axis. Backport of the similar option introduced in 25w31a

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ❌ No |
| 1.21+1 | ❌ No |
| 1.20+1 | ❌ No |

## `Hotkeys`

### `openConfigGui`

Opens the in-game config GUI of the mod. Defaults to `C + E`.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `insertFormattingCode`

Inserts a formatting code symbol (§) on the open screen. Useful when writing formatting codes in books.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

## `Yeets`

### `disableEntityCollisions`

> [!WARNING]
> This feature requires a server-side opt-in. It will automatically be disabled when playing on multiplayer servers.
> A server administrator can install the mod on their server to enable usage of this feature.

Disables entity collisions. You can still push other entities, but they can't push you.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableKnockback`

> [!WARNING]
> This feature requires a server-side opt-in. It will automatically be disabled when playing on multiplayer servers.
> A server administrator can install the mod on their server to enable usage of this feature.

Disables taking knockback.

- When `disableExplosionKnockback` in `Generic` is set to true, also disables knockback from explosions. 

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableFogModifiers`

Disables all fog modifiers (water, lava, powdered snow, darkness, etc.).

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableArmorRestriction`

Disables the restriction of armour slots in the inventory. May not work on servers.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableBindingCurse`

Disables Curse of Binding. May not work on servers.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableItemCooldown`

Disables the cooldown on ender pearls, goat horns, and more. May not work on servers.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableIllegalCharacterCheck`

Allows using special characters as U+00A7 (§), U+007F and more in all text boxes. May not work on servers.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableWorldBorder`

Disables all world border restrictions.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableBedExplosions`

Disables bed explosions in dimensions where beds don't work, by disabling clicking on them.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableAllayItemUse`

Disables giving items to allays.

- When allays are dancing, you can still give them amethyst shards to duplicate them.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableOffhandUse`

Disables being able to use items in the offhand.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableHorseJumpCharge`

> [!WARNING]
> This feature requires a server-side opt-in. It will automatically be disabled when playing on multiplayer servers.
> A server administrator can install the mod on their server to enable usage of this feature.

Disables the horse jump charge and cooldown (pressing space once immediately uses the maximum horse jump height). Also disables the camel dash cooldown.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableOverlayRender`

Disables rendering various overlays, such as the vignette, spyglass, carved pumpkin and powdered snow overlays.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableAllNamesInF1`

Hides all entity names when the HUD is disabled (vanilla Minecraft still renders the names of entities in teams).

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableUseItemSlowdown`

> [!WARNING]
> This feature requires a server-side opt-in. It will automatically be disabled when playing on multiplayer servers.
> A server administrator can install the mod on their server to enable usage of this feature.

Disables slowing down when using items, like eating food.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableSwiftSneak`

Disables sneak speed modifiers like swift sneak, because sometimes slow sneaking is nice.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableJumpDelay`

> [!WARNING]
> This feature requires a server-side opt-in. It will automatically be disabled when playing on multiplayer servers.
> A server administrator can install the mod on their server to enable usage of this feature.

Disables the 10-tick delay between jumps. Allows you to jump very fast when in a low area and holding down space.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ✔️ Yes |
| 1.21+1 | ✔️ Yes |
| 1.20+1 | ✔️ Yes |

### `disableBookLineLimit`

Disables the line limit of books, allowing you to write until the full limit of 1024 characters. A scroll bar will appear when writing enough lines. Note that lines will be cut off in signed books.

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ❌ No |
| 1.21+1 | ❌ No |
| 1.20+1 | ❌ No |

### `disableBabyFeeding`

Disables feeding of baby animals, making breeding animals easier when there are a lot of animals in a small space

#### Availability

| Minecraft Version | Supported |
|-------------------|-----------|
| 1.21.6+7+8 | ✔️ Yes |
| 1.21.4 | ❌ No |
| 1.21+1 | ❌ No |
| 1.20+1 | ❌ No |
