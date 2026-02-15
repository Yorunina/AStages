## [2.0.0] - 15-02-2026


New Features

- Implement tick method for temporary stages. ([#65](https://github.com/Alessandro-Casale/AStages/pull/65))
- New methods exposed for both Stages and TemporaryStages. ([#75](https://github.com/Alessandro-Casale/AStages/pull/75))

Bug Fixes

- Solve bug which prevents world from loading correctly if a custom stage will be added without setting a custom stack. ([#67](https://github.com/Alessandro-Casale/AStages/pull/67))
- Solved disconnection from servers when client info command is run. ([#68](https://github.com/Alessandro-Casale/AStages/pull/68))
- Solve a bug which keeps server crashing at startup. ([#73](https://github.com/Alessandro-Casale/AStages/pull/73))
- Critical server crash caused by invalid registry setup. ([#74](https://github.com/Alessandro-Casale/AStages/pull/74))

API Changes

- Unify event post/listener system over different loaders. ([#61](https://github.com/Alessandro-Casale/AStages/pull/61))
- New method for adding stages via java code, new tests. ([#66](https://github.com/Alessandro-Casale/AStages/pull/66))
- Moved stage managers and instances to attribute system like restrictions. ([#70](https://github.com/Alessandro-Casale/AStages/pull/70))
- Plugin integration for `Stage` and `TemporaryStage` classes. ([#76](https://github.com/Alessandro-Casale/AStages/pull/76))


