## [2.5.0] - 06-08-2026


New Features

- Add config options for recipe viewer integration and asynchronous cache building. ([#245](https://github.com/Alessandro-Casale/AStages/pull/245))
- Implement EMI integration for item, fluid and recipes. ([#246](https://github.com/Alessandro-Casale/AStages/pull/246))
- Implement REI integration for item, fluid and recipes. ([#247](https://github.com/Alessandro-Casale/AStages/pull/247))
- Refactor collision handling, Alex's Caves compatibility. ([#263](https://github.com/Alessandro-Casale/AStages/pull/263))
- Update item messages for consistency. ([#270](https://github.com/Alessandro-Casale/AStages/pull/270))
- Update item property restrictions and tooltip messages (Introduce new methods). ([#272](https://github.com/Alessandro-Casale/AStages/pull/272))

Bug Fixes

- Fix mod restriction evaluation logic. ([#241](https://github.com/Alessandro-Casale/AStages/pull/241))
- Fix remove_all action and command. ([#242](https://github.com/Alessandro-Casale/AStages/pull/242))
- Finally solved "AStages is forgetting Player Data". ([#243](https://github.com/Alessandro-Casale/AStages/pull/243))
- Handle corrupted legacy simple_restrictions.json. ([#244](https://github.com/Alessandro-Casale/AStages/pull/244))
- Improve player validation in block breaking and harvesting events. ([#266](https://github.com/Alessandro-Casale/AStages/pull/266))
- Add player validation in entity interaction event. ([#267](https://github.com/Alessandro-Casale/AStages/pull/267))
- Add player validation in player attack event. ([#268](https://github.com/Alessandro-Casale/AStages/pull/268))

API Changes

- Refactor reload handling to use ClientReloadPhase for improved clarity and consistency. ([#248](https://github.com/Alessandro-Casale/AStages/pull/248))
- Enhance client reload handling with new phases and improved model management. ([#249](https://github.com/Alessandro-Casale/AStages/pull/249))
- Introduce RecipeViewerManager and enhance JeiItemStagesPlugin with caching functionality. ([#250](https://github.com/Alessandro-Casale/AStages/pull/250))
- Replace HashMap with ConcurrentHashMap for thread-safe caching in OfflinePlayerStage. ([#251](https://github.com/Alessandro-Casale/AStages/pull/251))
- Add client-side structure restriction handling. ([#262](https://github.com/Alessandro-Casale/AStages/pull/262))
- Replace List with Set for collections for structure restrictions. ([#264](https://github.com/Alessandro-Casale/AStages/pull/264))
- Immutable server stages view in AStagesUtils. ([#265](https://github.com/Alessandro-Casale/AStages/pull/265))
- Mark update events as not yet implemented and deprecate UnpackLootTableEvent. ([#269](https://github.com/Alessandro-Casale/AStages/pull/269))
- Update attribute names for consistency across restrictions. ([#271](https://github.com/Alessandro-Casale/AStages/pull/271))
- Simplify client restriction reload state management. ([#273](https://github.com/Alessandro-Casale/AStages/pull/273))
- Add composite StreamCodec methods for variable argument handling (1.21.X only). ([#274](https://github.com/Alessandro-Casale/AStages/pull/274))


