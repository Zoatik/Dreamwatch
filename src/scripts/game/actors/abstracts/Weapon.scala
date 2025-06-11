package scripts.game.actors.abstracts

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.instantiables.Sprite2D
import scripts.dreamwatch_engine.physics.{Area2D, Movement2D}
import scripts.game.GameManager
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
  protected val bulletType: Bullet.Type

  // shoot ability variables
  protected var canShoot: Boolean = true
  protected var lastShot: Float = 0.0f


  // Upgrades + Evolutions
  var weaponUpgrades: ArrayBuffer[Weapon.Upgrade] = ArrayBuffer.empty
  var weaponEvolution: Weapon.Evolution = Weapon.Phase0


  override def update(deltaT: Float): Unit = {
    super.update(deltaT)
  }


  def shoot(target: Vector2, mode: Weapon.Mode): Unit = {
    if(timeFromCreation > lastShot + modifiedBulletCooldown && canShoot) {
      println("shoot")
      new Bullet(pos.cpy(), target, bulletType).instantiate() // TODO: Add modifiers
      lastShot = timeFromCreation
    }
  }

  // modified bullet values getter
  protected def modifiedBulletSpeed: Float = {
    val cooldownModifiers = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.Speed)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Cooldown.reduction

    bulletType.baseBulletSpeed * cooldownModifiers
  }

  protected def modifiedBulletCooldown: Float = {
    val cooldownModifiers = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.Cooldown)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Cooldown.reduction

    bulletType.baseBulletCooldown * cooldownModifiers
  }

  protected def modifiedBulletSize: Float = {
    val cooldownModifiers = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.BulletSize)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Cooldown.reduction

    bulletType.baseBulletSize * cooldownModifiers
  }

  protected def modifiedBulletExplosionSize: Float = {
    val cooldownModifiers = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.ExplosionSize)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Cooldown.reduction

    bulletType.baseBulletExplosionSize * cooldownModifiers
  }

  protected def modifiedBulletDamage: Float = {
    val cooldownModifiers = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.BossDamage)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Cooldown.reduction

    bulletType.baseBulletDamage * cooldownModifiers
  }


}


object Weapon {

  sealed trait Mode
  case object Primary extends Mode
  case object Secondary extends Mode

  sealed trait Evolution
  case object Phase0 extends Evolution
  case object Phase1 extends Evolution
  case object Phase2 extends Evolution
  case object Phase3 extends Evolution

  sealed trait Upgrade
  // all modifiers are summed and then multiplied to base-
  case object Speed extends Upgrade{
    val amplification: Float = 0.1f
  }
  case object Cooldown extends Upgrade{
    val reduction: Float = 0.1f
  }
  case object BulletSize extends Upgrade{
    val amplification: Float = 0.1f
  }
  case object ExplosionSize extends Upgrade{
    val amplification: Float = 0.1f
  }
  case object BossDamage extends Upgrade{
    val amplification: Float = 0.1f
  }

}
