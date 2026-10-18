# Witch Wands

A Fabric mod for Minecraft 1.21.1 that adds magical witch wands, each with its own abilities.

> 🚧 Early release (v0.2.0): the Fire Wand and Ice Wand are complete, and more wands are on the way!

## Features

### 🔥 Fire Wand

- **Fireball** (right-click): launches a fireball that explodes on impact. Damages and ignites mobs it hits, deals splash damage nearby, and **doesn't break blocks**. 5 second cooldown.
- **Inferno** (Shift + right-click): sets every mob within 25 blocks on fire. 20 second cooldown.
- **Melee**: hits as hard as an iron shovel.
- Fire sounds and flame particles.

### ❄️ Ice Wand

- **Snowball Spray** (right-click, hold to spray): shoots snowballs that deal small damage and give Slowness I for 3 seconds.
- **Ammo**: 30 snowballs, then a 15 second reload, with a live counter above the hotbar.
- **Freeze** (Shift + right-click): freezes everyone within 25 blocks for 5 seconds; they can't move and get covered in frost. 30 second cooldown.
- **Melee**: hits as hard as an iron shovel.
- Snow and ice sounds, snowflake particles.

### ✨ For all wands

- Separate cooldowns for each ability, with messages above the hotbar.
- A sound and message when an ability is ready again.
- A **Witch Wands** creative tab with every wand in one place.

## Roadmap

- [x] Fire Wand
- [x] Ice Wand
- [ ] Teleport Wand
- [ ] Soil Wand
- [ ] Rare witches that drop the wands

## Requirements

- Minecraft 1.21.1
- [Fabric Loader](https://fabricmc.net/use/)
- [Fabric API](https://modrinth.com/mod/fabric-api)

## Download

Available on [Modrinth](https://modrinth.com/mod/witch-wands) and on the [Releases](https://github.com/ArdaKe26/witchwands/releases) page.

## Building from source

Requires Java 21.

    ./gradlew build

The mod file will be in `build/libs/`.

## License

This project is licensed under the [MIT License](LICENSE).
