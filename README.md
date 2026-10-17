# Witch Wands

A Fabric mod for Minecraft 1.21.1 that adds magical witch wands, each with its own abilities.

> 🚧 Early release (v0.1.0): the Fire Wand is complete, and more wands are on the way!

## Features

### 🔥 Fire Wand

- **Fireball** (right-click): launches a fireball that explodes on impact. Damages and ignites mobs it hits, deals splash damage nearby, and **doesn't break blocks**. 5 second cooldown.
- **Inferno** (Shift + right-click): sets every mob within 25 blocks on fire. 20 second cooldown.
- **Melee**: hits as hard as an iron shovel.
- Separate cooldowns for each ability, with messages above the hotbar.
- A sound and message when an ability is ready again.
- Custom sounds and flame particles.

## Roadmap

- [x] Fire Wand
- [ ] Ice Wand
- [ ] Teleport Wand
- [ ] Soil Wand
- [ ] Rare witches that drop the wands

## Requirements

- Minecraft 1.21.1
- [Fabric Loader](https://fabricmc.net/use/)
- [Fabric API](https://modrinth.com/mod/fabric-api)

## Download

Available on [Modrinth](https://modrinth.com/mod/witch-wands).

## Building from source

Requires Java 21.

    ./gradlew build

The mod file will be in `build/libs/`.

## License

This project is licensed under the [MIT License](LICENSE).
