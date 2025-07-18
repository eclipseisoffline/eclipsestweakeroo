- Fix mixin incompatibilities with certain mods.

From `0.6.3-1.21.6`:

- Added the following tweaks:
  - `tweakPersistentChat`: keeps past chat messages across server/world switches.
  - `tweakShowFormattingCodes`: renders legacy text formatting codes with a grey colour instead of hiding them. Still displays the formatting they set. Useful when writing books with formatting codes.
  - `tweakMusicToast`: makes a few tweaks to the music toasts added in 1.21.6, depending on how configured in `Generic`:
    - `musicToastMusic`: shows the music toast for game music. When disabled, shows an actionbar text instead, like is normally done for jukebox records. Enabled by default.
    - `musicToastRecords`: shows the music toast for jukebox records. When disabled, shows an actionbar text instead, like is normally done. Enabled by default.
    - `musicToastPauseMenu`: whether to show music toasts in the pause menu for game music or jukebox records, when enabled for those. Enabled by default.
- Added the following fixes:
  - `writableBookFormattingFix`: fixes [MC-297501](https://bugs.mojang.com/browse/MC/issues/MC-297501), allowing you to use formatting codes in books again, and making them display as they would before 1.21.6. Breaks the editing cursor when placed mid-text.
- Added the following yeets:
  - `disableJumpDelay`: disables the 10-tick delay between jumps. Allows you to jump very fast when in a low area and holding down space. Requires a server-side opt-in.
  - `disableBookLineLimit`: disables the line limit of books, allowing you to write until the full limit of 1024 characters. A scroll bar will appear when writing enough lines. Note that lines will be cut off in signed books.
- Added the following hotkeys:
  - `insertFormattingCode`: inserts a formatting code symbol (§) on the open screen. Useful when writing formatting codes in books.
- Fixed `disableFogModifiers` not disabling fog distance modifiers.
- The mod no longer has a hard dependency on Tweakeroo, and can run without it. MaLiLib is still required.
- The mod can now run without MaLiLib and/or Tweakeroo on servers.
