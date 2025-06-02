package scripts.World.Actors.TopLevel

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.Sprite
import scripts.World.Actors.Base.{CollisionEntity, CollisionObject2D, Entity, Object2D}
import scripts.World.Actors.TopLevel.Bullet.baseExplosionRadius
import scripts.World.Physics.{Area2D, CircleArea2D, Collider2D, Movement2D}
import scripts.World.SceneManager

import scala.collection.mutable.ArrayBuffer


class Bullet(pos: Vector2,
             bulletType: Bullet.Type,
             graphicLayerZ: Int,
             parent: Object2D,
             collisionLayerZ: Int,
             override var _speed: Vector2 = new Vector2(0,0)
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
  private val explosionCollider = new CollisionEntity(bulletArea, this, 0) {
    override def spawn(): Entity = ???
  }


  override def destroy(): Unit = {
    super.destroy()
  }

  override def spawn(): Entity = ???
}

/** Sealed trait pour les types de projectiles */
object Bullet {

  /** Charge le sprite correspondant à chaque type de bullet */
  private def loadSpriteFor(bulletType: Type, pos: Vector2): Sprite = bulletType match {
    case Small => new Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)
    case Big => new Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)
    case Laser => new Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)
  }

  private def baseExplosionRadius(bulletType: Type): Float = bulletType match {
    case Small => 10.0f
    case Big => 20.0f
    case Laser => 15.0f
  }

  private def baseBulletRadius(bulletType: Type): Float = bulletType match {
    case Small => 10.0f
    case Big => 20.0f
    case Laser => 15.0f
  }

  private def baseBulletSpeed(bulletType: Type): Float = bulletType match {
    case Small => 100.0f
    case Big => 200.0f
    case Laser => 150.0f
  }

  private def baseCollisionArea2D(bulletType: Type, pos: Vector2): Area2D = {
    new CircleArea2D(pos, baseBulletRadius(bulletType))
  }

  def spawn(pos: Vector2, target: Vector2, bulletType: Type): Bullet = {
    val newBullet: Bullet = new Bullet(pos, bulletType, 0, null, 0)
    val direction: Vector2 = target.sub(pos).nor()
    val speed: Float = baseBulletSpeed(bulletType)
    newBullet.speed = direction.scl(speed)
    SceneManager.addToCurrentScene(newBullet)
    newBullet
  }

  sealed trait Type

  case object Small extends Type

  case object Big extends Type

  case object Laser extends Type
}




