# Dreamwatch
Rogue-lite shoot ’em up game for an HEI project.

## Context
Dreamwatch is a **rogue-lite, shoot ’em up** game developed as part of an HEI project.  
You play as the defender of Timmy’s dreams, battling “nightmares” in the form of projectiles and “night terrors” (bosses) in a world inspired by **Missile Command** and the upgrade system of **Ravenswatch**.

## Gameplay
- Each round lasts **1 minute**.  
- Every **5 levels**, you face a **boss round** (no time limit).  
- Goal: survive **20 rounds** to complete a session.  
- *All values can be adjusted in the `Globals.scala` file.*

During each round:  
1. **Destroy** enemy projectiles (“nightmares”) to collect **dream fragments**.  
2. Spend fragments mid-round to trigger an **instant weapon evolution** (3 choices) — **(not implemented)**.  
3. At the end of the round, choose one of three upgrades: permanent stat boosts or the ultimate weapon evolution if unlocked.

Starting weapons/toys:  
- **Sniper** – primary: fast, piercing shot; secondary: sinusoidal shot with explosion  
- **Machine Gun** – high rate of fire **(not implemented)**  
- **Mortar** – area-of-effect shots **(not implemented)**

## Controls
- **Aim**: mouse cursor  
- **Fire**: left and right mouse buttons  
- **Pause**: Esc  

## Key Features
- **Rogue-lite progression**: permanent upgrades and weapon evolutions  
- **Varied shot patterns**: linear, spiral, “spaghetti,” etc.  
- **4 unique bosses**, one every 5 rounds  
- **Upgrade system**:  
  - **Weapon evolutions**: 3 branches × 3 tiers **(not implemented)**  
  - **Stat boosts**: 5 types (speed, cooldown, area, projectile size, damage)

## Project Structure
```text
src
├── res
│   ├── shaders
│   ├── sounds
│   └── sprites
│       ├── bosses
│       ├── game
│       ├── nightmares
│       ├── placeholders
│       └── ui
└── scripts
    ├── dreamwatch_engine
    │   ├── actors
    │   │   ├── abstracts
    │   │   │   ├── Component.scala
    │   │   │   ├── Entity.scala
    │   │   │   ├── Object2D.scala
    │   │   │   └── Scene.scala
    │   │   └── instantiables
    │   │       ├── CollisionObject2D.scala
    │   │       ├── CollisionSprite2D.scala
    │   │       ├── Particle2D.scala
    │   │       ├── Sprite2D.scala
    │   │       └── UiElement.scala
    │   ├── graphics
    │   │   └── Graphics2D.scala
    │   ├── inputs
    │   │   ├── Controller.scala
    │   │   └── InputManager.scala
    │   ├── physics
    │   │   ├── Area2D.scala
    │   │   ├── Collider2D.scala
    │   │   └── Movement2D.scala
    │   └── utils
    │       └── Layers.scala
    ├── DreamWatch.scala
    ├── game
    │   ├── actors
    │   │   ├── abstracts
    │   │   │   ├── Nightmare.scala
    │   │   │   └── Weapon.scala
    │   │   └── instantiables
    │   │       ├── Boss.scala
    │   │       ├── Bullet.scala
    │   │       ├── Card.scala
    │   │       ├── GameScene.scala
    │   │       ├── MainMenuScene.scala
    │   │       ├── nightmares
    │   │       │   └── Ghost.scala
    │   │       ├── Player.scala
    │   │       ├── Toy.scala
    │   │       └── weapons
    │   │           ├── Mortar.scala
    │   │           └── Sniper.scala
    │   ├── GameManager.scala
    │   └── MusicManager.scala
    └── utils
        └── Globals.scala
```
