package scripts.World.Actors.TopLevel

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.Sprite
import scripts.World.Actors.Base.{CollisionEntity, CollisionObject2D, Object2D}
import scripts.World.Actors.TopLevel.Bullet.baseExplosionRadius
import scripts.World.Physics.{Area2D, CircleArea2D, Collider2D, Movement2D}

import scala.collection.mutable.ArrayBuffer


class Bullet(pos: Vector2,
             bulletType: Bullet.Type,
             graphicLayerZ: Int,
             parent: Object2D,
             collisionLayerZ: Int
            )
  extends CollisionObject2D(pos,
    Bullet.loadSpriteFor(bulletType, pos),
    graphicLayerZ,
    Bullet.baseCollisionArea2D(bulletType, pos),
    parent,
    collisionLayerZ
  ) with Movement2D {

  var explosionRadius: Float = baseExplosionRadius(bulletType)
  var damage: Float = 1.0f
  private val bulletArea = new CircleArea2D(pos, explosionRadius)
  private val explosionCollider = new CollisionEntity(bulletArea, this, 0)


  override def destroy(): Unit = super.destroy()
}

/** Sealed trait pour les types de projectiles */
object Bullet {

  /** Charge le sprite correspondant à chaque type de bullet */
  def loadSpriteFor(bulletType: Type, pos: Vector2): Sprite = bulletType match {
    case Small => new Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)
    case Big => new Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)
    case Laser => new Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)
  }

  def baseExplosionRadius(bulletType: Type): Float = bulletType match {
    case Small => 10.0f
    case Big => 20.0f
    case Laser => 15.0f
  }

  def baseBulletRadius(bulletType: Type): Float = bulletType match {
    case Small => 10.0f
    case Big => 20.0f
    case Laser => 15.0f
  }

  def baseCollisionArea2D(bulletType: Type, pos: Vector2): Area2D = {
    new CircleArea2D(pos, baseBulletRadius(bulletType))
  }

  sealed trait Type

  case object Small extends Type

  case object Big extends Type

  case object Laser extends Type
}




