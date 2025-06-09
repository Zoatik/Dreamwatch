package scripts.game.actors.instantiables

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Component
import scripts.dreamwatch_engine.actors.instantiables.{CollisionObject2D, CollisionSprite2D, Particle2D}
import scripts.dreamwatch_engine.physics.{Area2D, Collider2D, Movement2D}
import scripts.game.actors.abstracts.Nightmare
import scripts.game.actors.instantiables.Bullet.{baseBulletRadius, baseBulletSpeed, baseExplosionRadius, bulletTrajectory}
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer


class Bullet(pos: Vector2,
             targetPos: Vector2,
             bulletType: Bullet.Type,
             lifeTime: Option[Float] = None
            )
  extends CollisionSprite2D(
    pos,
    Bullet.loadImagesFor(bulletType),
    Globals.BULLET_G_LAYER,
    Globals.BULLET_C_LAYER,
    Globals.BULLET_C_LAYERMASK,
    Area2D.Circle,
    lifeTime = lifeTime
  ) with Movement2D {

  override var speed: Float = baseBulletSpeed(bulletType)
  override var target: Vector2 = targetPos.cpy()

  initMovement(bulletTrajectory(bulletType))

  width = baseBulletRadius(bulletType)

  var damage: Float = 1.0f
  var explosionRadius: Float = baseExplosionRadius(bulletType)
  var explosionDamage: Float = 1.0f
  private val explosionCollider = new CollisionObject2D(pos, Area2D.Circle, explosionRadius, 0, cLayerZ, cLayerMask, lifeTime = Some(0.1f)) with Component[Bullet] {
    override val parent: Bullet = Bullet.this
  }

  val bulletParticle = new Particle2D("res/shaders/electrical_ball.fp", pos)
  bulletParticle.setUniform("u_radius", baseBulletRadius(bulletType))
  bulletParticle.setUniform("u_center", pos)

  override def instantiate(): Bullet = {
    super.instantiate()
    //val partTest = new Particle2D("res/shaders/electrical_ball.fp", pos)

    bulletParticle.instantiate()
    this
  }

  override protected def onCollision(other: Collider2D): Unit = other match {
    case _: Nightmare =>
      this.explode()
      this.destroy()
    case _: Boss =>
      this.explode()
      this.destroy()
    case _ =>
  }

  override protected def onTargetReached(): Unit = {
    this.explode()
    this.destroy()
  }


  private def explode(): Unit = {
    explosionCollider.instantiate()
    val exploParticle = new Particle2D(
      "res/shaders/explosion.fp",
      pos,
      Some(2.0f))

    exploParticle.setUniform("u_center", new Vector2(pos.x, pos.y))
    exploParticle.setUniform("u_radius", explosionRadius)
    exploParticle.setUniform("u_duration", 0.2f)
    exploParticle.instantiate()
  }

  override def destroy(): Unit = {
    super.destroy()
    bulletParticle.destroy()
  }



}

/** Sealed trait pour les types de projectiles */
object Bullet {

  /** Charge le sprite correspondant à chaque type de bullet */
  private def loadImagesFor(bulletType: Type): ArrayBuffer[BitmapImage] = bulletType match {
    case Small => ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png"))
    case Big => ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png"))
    case Laser => ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png"))
    case _ => ArrayBuffer.fill(1)(new BitmapImage("res/sprites/soccer.png"))

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




