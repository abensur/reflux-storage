# Publishing kit — Reflux Storage

Copy-paste-ready text and assets for CurseForge, Modrinth, and similar platforms.
Update the version/links before each release.

---

## Assets

| File | Use |
|------|-----|
| `icon.png` (512x512) | Project icon / logo (CurseForge & Modrinth both accept square PNG) |
| `publishing/reflux-storage-states.png` (1376x370) | Side-by-side charge-state showcase image for galleries/screenshots |

---

## Project name

> Reflux Storage

---

## One-line summary (<= 80 chars)

> Store reflux, tune burp power, and launch yourself skyward.

Alternates:
> A portable reflux tank that turns stored pressure into burp jumps.
> Charge a portable reflux tank and burp-jump across your world.

---

## Short description (CurseForge "Summary" / Modrinth "Summary")

> Reflux Storage adds a portable reflux tank that charges while carried, stores up to 8000 mB, and spends that pressure to launch you upward with configurable burp power. Equip it with Curios or keep it in your inventory.

---

## Full description (Markdown — works on both platforms)

~~~markdown
# Reflux Storage

Reflux Storage adds one beautifully cursed item: a portable reflux tank that slowly fills while you carry it, then converts that stored pressure into a vertical burp jump.

## How it works

Keep **Reflux Storage** in your inventory, hotbar, or an optional **Curios** belt/charm slot. It passively charges over time, storing up to **8000 mB** of reflux. Right-click the item, or bind the **Use Reflux Storage** key, to spend stored reflux and launch upward.

## Burp power

Burp power ranges from **1** to **20**. Higher power launches harder and costs more reflux.

- Sneak + scroll while holding the item to tune power.
- Bind **Adjust Burp Power** to tune it even while the item is in Curios or your inventory.
- Hold Sneak while pressing the adjust key to decrease power.

## Visual charge states

The item texture changes as it fills, with five visible charge states: **0%**, **25%**, **50%**, **75%**, and **100%**. The durability bar also shows stored reflux.

## Sounds

Custom burp sounds are split into weak, medium, and strong tiers, plus an empty sound when you do not have enough reflux for the selected power.

## Crafting

Reflux Storage is crafted with rotten flesh, sugar, and a bucket.

```text
rotten_flesh  sugar  rotten_flesh
rotten_flesh  sugar  rotten_flesh
rotten_flesh  bucket rotten_flesh
```

## Requirements

- **NeoForge** for Minecraft **1.21.1**
- **Curios API** is optional. With Curios installed, Reflux Storage can be equipped in belt or charm slots. Without Curios, it works from vanilla inventory and hand use.
~~~

---

## Dependencies (set these in the platform's dependency UI)

### Required
| Mod | Platform slug | Notes |
|-----|---------------|-------|
| NeoForge 21.1+ | (loader) | Minecraft 1.21.1 |

### Optional
| Mod | Platform slug | Notes |
|-----|---------------|-------|
| Curios API | CurseForge: `curios` · Modrinth: `curios` | Enables belt/charm equip support; vanilla inventory behavior works without it |

---

## Platform metadata

- **Minecraft versions:** 1.21.1
- **Mod loaders:** NeoForge
- **Environment:** Client and server (`side = BOTH`)
- **License:** MIT
- **Categories / tags:** Equipment, Utility, Magic, Transportation, Curios-compatible, Fun

### Suggested CurseForge categories
`Armor, Tools, and Weapons` · `Player Transport` · `Cosmetic`

### Suggested Modrinth tags
`equipment` · `transportation` · `utility` · `magic`

---

## Pre-release checklist

- [x] Bump `mod_version` in `gradle.properties` and the version in `neoforge.mods.toml`
- [x] `./gradlew clean build` -> grab the jar from `build/libs/`
- [x] Test the jar in a real instance without Curios
- [x] Test the jar in a real instance with Curios installed
- [x] Upload `icon.png` as the project icon
- [x] Set Curios as an optional dependency
- [x] Tag the Minecraft version (1.21.1) and loader (NeoForge)
