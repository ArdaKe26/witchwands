# Witch Wands

A Fabric mod for Minecraft 1.21.1 that adds magical witch wands, each with its own abilities.

> ✨ All four wands and the rare Wand Witches who carry them are complete!

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

### 🌀 Teleport Wand

- **Teleport** (right-click): teleports you to the block you're looking at, up to 30 blocks away. Checks there's room to stand and cancels fall damage. 7 second cooldown.
- **Pull** (Shift + right-click): pulls the mob or player you're looking at (up to 30 blocks) to right in front of you. Can't pull through walls, but tall grass and flowers don't block it. Pulled players can't use ender pearls for 30 seconds. 35 second cooldown, even if you miss.
- **Melee**: hits as hard as an iron shovel.
- Portal sounds and particles where you leave and where you land.

### 🌱 Soil Wand

A peaceful wand for farmers.

- **Food Give** (right-click): look at a wheat, carrot, potato or beetroot crop (up to 10 blocks away) to get 10 of that food. If your inventory is full, it drops at your feet. 10 second cooldown.
- **Grow** (Shift + right-click while standing on farmland): spreads through your whole connected farm, up to 20 blocks away.
  - Every crop grows to full instantly.
  - Empty farmland gets planted with the seeds in your **offhand**, fully grown.
  - **1 seed plants 2 blocks.** When your offhand runs out, it uses the same seeds from your inventory.
  - 15 second cooldown.
- **Melee**: hits like a diamond hoe.
- Bonemeal sounds and green sparkles on every crop.

### ✨ For all wands

- Separate cooldowns for each ability, with messages above the hotbar.
- A sound and message when an ability is ready again.
- A **Witch Wands** creative tab with every wand and the Wand Witch spawn egg in one place.

## 🧙 Wand Witches

In Survival, the wands can only be found by defeating rare **Wand Witches**. Each one carries a wand, fights with its power, and spawns only in a matching place:

| Witch | Where she spawns | How she fights |
|---|---|---|
| 🔥 **Fire Witch** | Inside Nether fortresses | Shoots fireballs at you |
| ❄️ **Ice Witch** | Frozen and jagged mountain peaks | Throws bursts of snowballs that slow you down |
| 🌀 **Teleport Witch** | Inside End cities | Pulls you to her from up to 30 blocks away and locks your ender pearls for 30 seconds, blinks away when you hit her up close, and throws potions |
| 🌱 **Soil Witch** | Abandoned villages (villages with no villagers left) | Heals hurt monsters around her every 5 seconds and throws potions, so take her out first! |

- Wand Witches are **rare**: they only appear near players in their own location, with a small chance every 30 seconds.
- They notice you from up to 32 blocks away.
- They still drink potions like normal witches, then pull their wand back out.
- When defeated, a Wand Witch has a **10% chance** to drop her wand.
- A **Wand Witch spawn egg** is in the creative tab (it spawns a witch with a random wand).

## Roadmap

- [x] Fire Wand
- [x] Ice Wand
- [x] Teleport Wand
- [x] Soil Wand
- [x] Rare Wand Witches that fight with and drop the wands

## Requirements

- Minecraft 1.21.1
- [Fabric Loader](https://fabricmc.net/use/)
- [Fabric API](https://modrinth.com/mod/fabric-api)

## Download

Available on [Modrinth](https://modrinth.com/mod/witch-wands) and on the [Releases](https://github.com/ArdaKe26/witchwands/releases) page.

## Installation

1. **Install Fabric Loader.** Download the installer from [fabricmc.net](https://fabricmc.net/use/installer/), run it, choose **Minecraft 1.21.1** and click **Install**. This adds a "fabric-loader-1.21.1" profile to your Minecraft Launcher.
2. **Download [Fabric API](https://modrinth.com/mod/fabric-api)** for 1.21.1. Witch Wands needs it to run.
3. **Download Witch Wands** (the `.jar` file) from Modrinth or the Releases page.
4. **Put both `.jar` files in your `mods` folder:**
   - Windows: press `Win + R`, type `%appdata%\.minecraft\mods` and press Enter
   - macOS: `~/Library/Application Support/minecraft/mods`
   - Linux: `~/.minecraft/mods`

   If the `mods` folder doesn't exist yet, create it.
5. **Open the Minecraft Launcher**, select the **fabric-loader-1.21.1** profile and click **Play**.

## How to use

### In Creative

Open your inventory and find the **Witch Wands** tab. It has all four wands and the Wand Witch spawn egg.

### In Survival

The wands can't be crafted. You have to defeat **Wand Witches** to get them:

1. Go to the place where the witch for the wand you want spawns (see the table above), for example a Nether fortress for the Fire Wand.
2. Stay in the area. Wand Witches are rare, so it can take a while for one to show up.
3. Defeat her. She has a 10% chance to drop her wand, so you might need to beat a few.

### Controls

| Action | What it does |
|---|---|
| **Right-click** | The wand's main ability |
| **Shift + right-click** | The wand's second ability |
| **Left-click (hit)** | Melee attack |

- The **Soil Wand's Grow** ability works when you're **standing on farmland** with **seeds in your offhand** (press `F` to swap an item to your offhand).
- If an ability is on cooldown, a message above the hotbar tells you how long to wait. You'll hear a sound when it's ready again.

## Building from source

Requires Java 21.

    ./gradlew build

The mod file will be in `build/libs/`.

## License

This project is licensed under the [MIT License](LICENSE).
