## [2.3.2] - 12-07-2026


New Features

- Update AItemTagRestriction class to use `TagKey<Item>` instead of `ResourceLocation`. ([#201](https://github.com/Alessandro-Casale/AStages/pull/201))
- Enhance KubeJS integration with player and server stage management methods. ([#202](https://github.com/Alessandro-Casale/AStages/pull/202))
- Integrate LootJS support for loot modification processing. ([#204](https://github.com/Alessandro-Casale/AStages/pull/204))
- Add living drops check for enhanced loot control, flag must be enabled in common config. ([#208](https://github.com/Alessandro-Casale/AStages/pull/208))

Bug Fixes

- Add ignoredBiomes method for AMobRestriction, restricted mobs can spawn with different equipment. ([#199](https://github.com/Alessandro-Casale/AStages/pull/199))
- Refactor stage retrieval logic in AClientHolder to be in line with AHolder implementation. ([#200](https://github.com/Alessandro-Casale/AStages/pull/200))
- Allow event to proceed when equipment restrictions are met. ([#203](https://github.com/Alessandro-Casale/AStages/pull/203))
- Invert stage check logic in AScreenManager for server and player restrictions. ([#205](https://github.com/Alessandro-Casale/AStages/pull/205))
- Update AHolder and AStagesSuggestions to use ServerPlayer collection for command suggestions. ([#206](https://github.com/Alessandro-Casale/AStages/pull/206))

API Changes

- Change item restriction collections from List to Set for improved performance and semantics. ([#207](https://github.com/Alessandro-Casale/AStages/pull/207))


