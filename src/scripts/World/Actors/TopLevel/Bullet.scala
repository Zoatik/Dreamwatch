package scripts.World.Actors.TopLevel

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.Sprite
import scripts.World.Actors.Base.CollisionObject2D
import scripts.World.Actors.TopLevel.Bullet.baseExplosionRadius
import scripts.World.Physics.{Area2D, CircleArea2D, Movement2D}

import scala.collection.mutable.ArrayBuffer


class Bullet(pos: Vector2,
             bulletType: Bullet.Type,
             graphicLayerZ: Int,
             collisionLayerZ: Int,
             override var _speed: Vector2 = new Vector2(0,0)
            )
  extends CollisionObject2D(pos,
    Bullet.loadSpriteFor(bulletType, pos),
    graphicLayerZ,
    Bullet.baseCollisionArea2D(bulletType, pos),
    collisionLayerZ
  ) with Movement2D {

  var explosionRadius: Float = baseExplosionRadius(bulletType)
  var damage: Float = 1.0f
  private val bulletArea = new CircleArea2D(pos, explosionRadius)
  private val explosionCollider = new CollisionComponent(bulletArea, this, 0)


}

/** Sealed trait pour les types de projectiles */
object Bullet {

  /** Charge le sprite correspondant à chaque type de bullet */
  private def loadSpriteFor(bulletType: Type, pos: Vector2): Sprite = bulletType match {
    case Small => {
      val s: Sprite = Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)
      //s.setWidth(2 * baseBulletRadius(bulletType))
      s
    }
    case Big => {
      val s: Sprite = Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)
      //s.setWidth(2 * baseBulletRadius(bulletType))
      s
    }
    case Laser => {
      val s: Sprite = Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)
      //s.setWidth(2 * baseBulletRadius(bulletType))
      s
    }
  }

  private def baseExplosionRadius(bulletType: Type): Float = bulletType match {
    case Small => 10.0f
    case Big => 20.0f
    case Laser => 15.0f
  }

  private def baseBulletRadius(bulletType: Type): Float = bulletType match {
    case Small => 8.0f
    case Big => 12.0f
    case Laser => 15.0f
  }

  private def baseBulletSpeed(bulletType: Type): Float = bulletType match {
    case Small => 300.0f
    case Big => 250.0f
    case Laser => 150.0f
  }

  private def baseCollisionArea2D(bulletType: Type, pos: Vector2): Area2D = {
    new CircleArea2D(pos, baseBulletRadius(bulletType))
  }

  def spawn(pos: Vector2, target: Vector2, bulletType: Type): Bullet = {
    val newBullet: Bullet = new Bullet(pos, bulletType, 0, 0)
    val direction: Vector2 = target.sub(pos).nor()
    val speed: Float = baseBulletSpeed(bulletType)
    newBullet.speed = direction.scl(speed)
    newBullet.spawn().asInstanceOf[Bullet]
  }

  sealed trait Type

  case object Small extends Type

  case object Big extends Type

  case object Laser extends Type
}




