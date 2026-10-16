# Two-hit enemies

Stage 6 starts with one yellow enemy at the bottom of the leftmost column.
A standard hit turns it white; the next standard hit defeats it. Surviving hits
do not award points, drops or kill achievements. Movement and shooting are unchanged.

The last argument of `SETTINGS_LEVEL_6` in `engine/Core.java` configures the count:
`new GameSettings(7, 7, 10, 1000, 1)`. Zero disables the variation; the count must
leave at least one ordinary enemy. Other stages default to zero.

This uses the current seven-stage configuration. When the ten-stage progression
is integrated, connect the count to its Stage 6 settings. No boss behaviour or
optional Stage 9 combination is included.

## Validation

Java 17 compilation with `-Xlint:all` and 41 standalone regression checks passed.
Stage 6 was also checked manually by the feature requester. Full item interaction
and difficulty testing remains to be done.

Compile `src` and `tests/TwoHitEnemyTest.java` together into an isolated build
directory, then run `TwoHitEnemyTest` with that directory and `res` on the classpath.
The tests use the game's profile persistence, so do not use an existing game build
directory containing real player progress.

For manual testing, temporarily change the initial `GameState` level in `Core`
from 1 to 6. Check both hits, rewards, ordinary enemies, restart and stage advance,
then restore the initial level to 1 before committing.
