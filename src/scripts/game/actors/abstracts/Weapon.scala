package scripts.game.actors.abstracts

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.instantiables.Sprite2D
import scripts.dreamwatch_engine.physics.{Area2D, Movement2D}
import scripts.game.GameManager
import scripts.game.actors.abstracts.Weapon.{BossDamage, BulletSize, Cooldown, Evolution, ExplosionSize, Phase0, Phase1, Phase2, Phase3, Primary, Speed, UltimatePhase, Upgrade}
import scripts.game.actors.instantiables.Bullet
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer
import scala.reflect.internal.Mode

abstract class Weapon(pos: Vector2, images: ArrayBuffer[String])
  extends Sprite2D(pos, 0, images, Globals.WEAPON_G_LAYER, Area2D.Box) with Movement2D {
  canRotate = true
  override var speed = 0.0f
  override var target: Vector2 = GameManager.mousePos
  // abstract attributes on bullet specs
  protected var bulletTypePrimary: Bullet.Type
  protected var bulletTypeSecondary: Bullet.Type

  // shoot ability variables
  var canShoot: Boolean = true
  protected var lastShot: Float = 0.0f


  // Upgrades + Evolutions
  var weaponUpgrades: ArrayBuffer[Weapon.Upgrade] = ArrayBuffer.empty
  var weaponEvolution: Weapon.Evolution = Weapon.Phase0
  var upgradePossibilities: Array[Upgrade] = Array(Speed, Cooldown, BulletSize, ExplosionSize, BossDamage)


  override def update(deltaT: Float): Unit = {
    super.update(deltaT)
  }


  def shoot(target: Vector2, mode: Weapon.Mode): Unit = {
    val bulletType = if (mode == Weapon.Primary) bulletTypePrimary else bulletTypeSecondary
    if(timeFromCreation > lastShot + modifiedBulletCooldown(bulletType) && canShoot) {
      GameManager.reloadSound.play()
      new Bullet(pos.cpy(),
        target,
        bulletType,
        modifiedBulletSpeed(bulletType),
        modifiedBulletSize(bulletType),
        modifiedBulletExplosionSize(bulletType),
        modifiedBulletDamage(bulletType)).instantiate()
      lastShot = timeFromCreation
    }
  }

  // modified bullet values getter
  protected def modifiedBulletSpeed(bulletType: Bullet.Type): Float = {
    val cooldownModifiers = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.Speed)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Cooldown.reduction

    bulletType.baseBulletSpeed * cooldownModifiers
  }

  protected def modifiedBulletCooldown(bulletType: Bullet.Type): Float = {
    val cooldownModifiers = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.Cooldown)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Cooldown.reduction

    bulletType.baseBulletCooldown * cooldownModifiers
  }

  protected def modifiedBulletSize(bulletType: Bullet.Type): Float = {
    val cooldownModifiers = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.BulletSize)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Cooldown.reduction

    bulletType.baseBulletSize * cooldownModifiers
  }

  protected def modifiedBulletExplosionSize(bulletType: Bullet.Type): Float = {
    val cooldownModifiers = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.ExplosionSize)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Cooldown.reduction

    bulletType.baseBulletExplosionSize * cooldownModifiers
  }

  protected def modifiedBulletDamage(bulletType: Bullet.Type): Float = {
    val cooldownModifiers = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.BossDamage)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Cooldown.reduction

    bulletType.baseBulletDamage * cooldownModifiers
  }

  def evolveWeapon(): Unit = {
    weaponEvolution match {
      case Phase0 => weaponEvolution = Phase1
      case Phase1 => weaponEvolution = Phase2
      case Phase2 => weaponEvolution = Phase3
      case Phase3 => weaponEvolution = UltimatePhase
      case _ =>
    }
  }

}


object Weapon {

  sealed trait Mode
  case object Primary extends Mode
  case object Secondary extends Mode

  sealed trait Holster{
    val images: ArrayBuffer[String]
  }

  sealed trait Evolution extends Holster
  case object Phase0 extends Evolution {
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/bulletSpeedUpgrade.png") //TODO : Change to correct sprites
  }
  case object Phase1 extends Evolution {
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/bulletSpeedUpgrade.png")
  }
  case object Phase2 extends Evolution {
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/bulletSpeedUpgrade.png")
  }
  case object Phase3 extends Evolution {
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/bulletSpeedUpgrade.png")
  }
  case object UltimatePhase extends Evolution{
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/bulletSpeedUpgrade.png")
  }

  sealed trait Upgrade extends Holster
  // all modifiers are summed and then multiplied to base-
  case object Speed extends Upgrade{
    val amplification: Float = 0.1f
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/bulletSpeedUpgrade.png")
  }
  case object Cooldown extends Upgrade{
    val reduction: Float = 0.1f
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/bulletCooldownUpgrade.png")
  }
  case object BulletSize extends Upgrade{
    val amplification: Float = 0.1f
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/bulletSizeUpgrade.png")
  }
  case object ExplosionSize extends Upgrade{
    val amplification: Float = 0.1f
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/explosionSizeUpgrade.png")
  }
  case object BossDamage extends Upgrade{
    val amplification: Float = 0.1f
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/bulletDamageUpgrade.png")
  }
  case object RebuildToy extends Upgrade{
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/bulletSpeedUpgrade.png")
  }
}
