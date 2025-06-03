package scripts.World.Actors.TopLevel

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.Managers.CollisionsManager
import scripts.{Globals, Sprite}
import scripts.World.Actors.Base.CollisionObject2D
import scripts.World.Actors.TopLevel.Bullet.{baseBulletSpeed, baseExplosionRadius, bulletTrajectory}
import scripts.World.Physics.{Area2D, CircleArea2D, Collider2D, Movement2D}

import scala.collection.mutable.ArrayBuffer


class Bullet(pos: Vector2,
             targetPos: Vector2,
             bulletType: Bullet.Type,
             lifeTime: Option[Float] = None,
             graphicLayerZ: Int = Globals.BULLET_G_LAYER,
             collisionLayerZ: Int = Globals.BULLET_C_LAYER,
            )
  extends CollisionObject2D(
    pos,
    Bullet.loadSpriteFor(bulletType, pos),
    graphicLayerZ,
    Bullet.baseCollisionArea2D(bulletType, pos),
    collisionLayerZ,
    lifeTime
  ) with Movement2D {

  override var speed: Float = baseBulletSpeed(bulletType)
  override var target: Vector2 = targetPos.cpy()

  initMovement(bulletTrajectory(bulletType))

  var explosionRadius: Float = baseExplosionRadius(bulletType)
  var damage: Float = 1.0f
  var explosionDamage: Float = 1.0f
  private val explosionArea = new CircleArea2D(pos, explosionRadius)
  private val explosionCollider = new CollisionComponent(explosionArea, this, 0, Some(0.1f))


  override protected def onCollision(other: Collider2D): Unit = other match {
    case _: Nightmare =>
      this.explode()
      this.destroy()
    case _ =>
  }

  override protected def onTargetReached(): Unit = {
    this.explode()
    this.destroy()
  }


  private def explode(): Unit = {
    explosionCollider.spawn()
  }



}

/** Sealed trait pour les types de projectiles */
object Bullet {

  /** Charge le sprite correspondant à chaque type de bullet */
  private def loadSpriteFor(bulletType: Type, pos: Vector2): Sprite = bulletType match {
    case Small => Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)
    case Big => Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)
    case Laser => Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)
    case _ => Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png")), pos)

  }

  private def baseExplosionRadius(bulletType: Type): Float = bulletType match {
    case Small => 300.0f
    case Big => 400.0f
    case Laser => 500.0f
    case _ => 300.0f
  }

  private def baseBulletRadius(bulletType: Type): Float = bulletType match {
    case Small => 8.0f
    case Big => 12.0f
    case Laser => 15.0f
    case _ => 8.0f
  }

  private def baseBulletSpeed(bulletType: Type): Float = bulletType match {
    case Small => 300.0f
    case Big => 400.0f
    case Laser => 400.0f
    case _ => 300.0f
  }

  private def baseCollisionArea2D(bulletType: Type, pos: Vector2): Area2D = {
    new CircleArea2D(pos, baseBulletRadius(bulletType))
  }

  private def bulletTrajectory(bulletType: Type): Movement2D.Trajectory = bulletType match {
    case Small => Movement2D.Linear
    case Big => Movement2D.Sinus
    case Laser => Movement2D.Spiral
    case _ => Movement2D.Linear
  }


  sealed trait Type

  case object Small extends Type

  case object Big extends Type

  case object Laser extends Type
}




