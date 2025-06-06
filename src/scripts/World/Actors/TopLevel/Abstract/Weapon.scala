package scripts.World.Actors.TopLevel.Abstract

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.World.Actors.BaseClass.Instantiable.Sprite2D
import scripts.World.Actors.TopLevel.Bullet
import scripts.World.Physics.{Area2D, Movement2D}

import scala.collection.mutable.ArrayBuffer

abstract class Weapon(pos: Vector2, images: ArrayBuffer[BitmapImage], gLayerZ: Int)
  extends Sprite2D(pos, images, gLayerZ, Area2D.Box) with Movement2D{
  protected val baseBulletSpeed: Float
  protected val baseBulletCooldown: Float
  protected val baseBulletSize: Float
  protected val baseBulletExplosionSize: Float
  protected val baseBulletDamage: Float

  protected val bulletType: Bullet.Type

  protected var canShoot: Boolean = true
  protected var lastShot: Float = 0.0f
  var weaponUpgrades: ArrayBuffer[Weapon.Upgrade] = ArrayBuffer.empty
  var weaponEvolution: Weapon.Evolution = Weapon.Phase0


  override def update(deltaT: Float): Unit = {
    super.update(deltaT)

    if (lastShot > timeFromCreation + modifiedBulletCooldown)
      canShoot = true
  }


  def shoot(target: Vector2): Unit = {
    new Bullet(pos, target, bulletType).instantiate() // TODO: Add modifiers
    lastShot = timeFromCreation
    canShoot = false
  }

  def modifiedBulletCooldown: Float = {
    val cooldownModifiers = 1 + weaponUpgrades.count(upgrade => {
      upgrade.equals(Weapon.Cooldown)                 // TODO: Controler que equals fonctionne correctement
    }) * Weapon.Cooldown.reduction

    baseBulletCooldown * cooldownModifiers
  }

}


object Weapon {

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
