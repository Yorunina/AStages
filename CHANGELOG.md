## [2.1.0] - 14-06-2026


New Features

- New config flags to enable logs for specific mob types and spawn types. ([#122](https://github.com/Alessandro-Casale/AStages/pull/122))
- Implement criteria trigger and integrate AStages with vanilla Advancements system. ([#123](https://github.com/Alessandro-Casale/AStages/pull/123))
- Limit player access for structures using new borders. ([#126](https://github.com/Alessandro-Casale/AStages/pull/126))
- New methods for Stages and Temporary Stages. ([#127](https://github.com/Alessandro-Casale/AStages/pull/127))
- Meaningful arg names in KubeJS. ([#128](https://github.com/Alessandro-Casale/AStages/pull/128))
- Restrict structure interaction to within the bounding box only. ([#129](https://github.com/Alessandro-Casale/AStages/pull/129))

Bug Fixes

- Fix several bugs regarding mob restrictions. ([#119](https://github.com/Alessandro-Casale/AStages/pull/119))
- Fix mob spawning. ([#121](https://github.com/Alessandro-Casale/AStages/pull/121))
- Stages files are now overwritten every time an action (add/remove) is performed. ([#124](https://github.com/Alessandro-Casale/AStages/pull/124))
- NullPointerExecption in onPlayerTick when scanning all slots for enchanted books. ([#125](https://github.com/Alessandro-Casale/AStages/pull/125))
- Solve disconnection if a structure is not restricted. ([#130](https://github.com/Alessandro-Casale/AStages/pull/130))
- Solve crash if builder is not correctly ended. ([#132](https://github.com/Alessandro-Casale/AStages/pull/132))

API Changes

- Move player stages and server stages events to proper package. ([#120](https://github.com/Alessandro-Casale/AStages/pull/120))


