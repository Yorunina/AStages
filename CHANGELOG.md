## [2.3.0] - 05-07-2026


New Features

- Refactor suggestion providers for player/server stages commands. ([#151](https://github.com/Alessandro-Casale/AStages/pull/151))
- Rename method `disableSpawning` in `disableOverallSpawning` for AMobRestriction. ([#153](https://github.com/Alessandro-Casale/AStages/pull/153))
- Refactor all config flags, both client and common sides. ([#158](https://github.com/Alessandro-Casale/AStages/pull/158))
- Custom messages for stage can be shown in player action bar. ([#160](https://github.com/Alessandro-Casale/AStages/pull/160))
- Change signature for stage methods in KubeJS. ([#161](https://github.com/Alessandro-Casale/AStages/pull/161))

Bug Fixes

- Commands accept string with spaces (must be quoted with single or double quotes). ([#146](https://github.com/Alessandro-Casale/AStages/pull/146))
- Fix in-game logo error. ([#148](https://github.com/Alessandro-Casale/AStages/pull/148))
- Hide original item tooltip while waiting for synchronization. ([#157](https://github.com/Alessandro-Casale/AStages/pull/157))
- Fix ALootRestriction bugs. ([#162](https://github.com/Alessandro-Casale/AStages/pull/162))
- Solve command suggestion not being correct when player or server only stages are involved. ([#164](https://github.com/Alessandro-Casale/AStages/pull/164))
- Update stage alert logic in order to use default settings when no attribute is changed. ([#165](https://github.com/Alessandro-Casale/AStages/pull/165))
- Fix unknown messages for stage checks and warnings in commands. ([#166](https://github.com/Alessandro-Casale/AStages/pull/166))
- Fix validation checks for player and server stages. ([#167](https://github.com/Alessandro-Casale/AStages/pull/167))

API Changes

- Rename parameters in AMobRestriction. ([#154](https://github.com/Alessandro-Casale/AStages/pull/154))
- Refactor all stage alert system for titles, subtitles, chat messages and action bar messages. ([#159](https://github.com/Alessandro-Casale/AStages/pull/159))
- Server and player only stages are synced between server and client. ([#163](https://github.com/Alessandro-Casale/AStages/pull/163))


