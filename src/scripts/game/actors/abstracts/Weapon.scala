package scripts.game.actors.abstracts

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.instantiables.Sprite2D
import scripts.dreamwatch_engine.physics.{Area2D, Movement2D}
import scripts.game.actors.abstracts.Weapon._
import scripts.game.actors.instantiables.Bullet
import scripts.game.{GameManager, MusicManager}
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

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
      MusicManager.playSound("reload_sound")
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
    val speedModifier = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.Speed)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Speed.amplification

    bulletType.baseBulletSpeed * speedModifier
  }

  protected def modifiedBulletCooldown(bulletType: Bullet.Type): Float = {
    val cooldownModifier = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.Cooldown)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Cooldown.reduction

    bulletType.baseBulletCooldown / cooldownModifier
  }

  protected def modifiedBulletSize(bulletType: Bullet.Type): Float = {
    val bulletSizeModifier = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.BulletSize)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.BulletSize.amplification

    bulletType.baseBulletSize * bulletSizeModifier
  }

  protected def modifiedBulletExplosionSize(bulletType: Bullet.Type): Float = {
    val explosionSizeModifier = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.ExplosionSize)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.ExplosionSize.amplification

    bulletType.baseBulletExplosionSize * explosionSizeModifier
  }

  protected def modifiedBulletDamage(bulletType: Bullet.Type): Float = {
    val damageModifier = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.BossDamage)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.BossDamage.amplification

    bulletType.baseBulletDamage * damageModifier
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
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/ui/bulletSpeedUpgrade.png") //TODO : Change to correct sprites
  }
  case object Phase1 extends Evolution {
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/ui/bulletSpeedUpgrade.png")
  }
  case object Phase2 extends Evolution {
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/ui/bulletSpeedUpgrade.png")
  }
  case object Phase3 extends Evolution {
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/ui/bulletSpeedUpgrade.png")
  }
  case object UltimatePhase extends Evolution{
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/ui/bulletSpeedUpgrade.png")
  }

  sealed trait Upgrade extends Holster
  // all modifiers are summed and then multiplied to base-
  case object Speed extends Upgrade{
    val amplification: Float = 0.2f
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/ui/bulletSpeedUpgrade.png")
  }
  case object Cooldown extends Upgrade{
    val reduction: Float = 0.2f
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/ui/bulletCooldownUpgrade.png")
  }
  case object BulletSize extends Upgrade{
    val amplification: Float = 0.2f
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/ui/bulletSizeUpgrade.png")
  }
  case object ExplosionSize extends Upgrade{
    val amplification: Float = 0.2f
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/ui/explosionSizeUpgrade.png")
  }
  case object BossDamage extends Upgrade{
    val amplification: Float = 0.2f
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/ui/bulletDamageUpgrade.png")
  }
  case object RebuildToy extends Upgrade{
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/ui/bulletSpeedUpgrade.png")
  }
}
