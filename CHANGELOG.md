- Updated to 1.21.7.

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

These features will be automatically disabled when playing on servers that do not have this mod installed. Fabric servers
can install this mod and configure which features to enable for players in the configuration file, which can be found under
`<server>/config/eclipsestweakeroo-server.json`. You can always allow all features for operators using the `operators_exempt`
option in the aforementioned file.

You'll still be able to use all features when playing on singleplayer worlds.
