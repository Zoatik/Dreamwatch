package scripts.game.actors.instantiables

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Component
import scripts.dreamwatch_engine.actors.instantiables.{CollisionObject2D, CollisionSprite2D, Particle2D}
import scripts.dreamwatch_engine.physics.{Area2D, Collider2D, Movement2D}
import scripts.game.actors.abstracts.Nightmare
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer


class Bullet(pos: Vector2,
             targetPos: Vector2,
             bulletType: Bullet.Type,
             lifeTime: Option[Float] = None
            )
  extends CollisionSprite2D(
    pos,
    bulletType.images,
    Globals.BULLET_G_LAYER,
    Globals.BULLET_C_LAYER,
    Globals.BULLET_C_LAYERMASK,
    Area2D.Circle,
    lifeTime = lifeTime
  ) with Movement2D {

  override var speed: Float = bulletType.baseBulletSpeed
  override var target: Vector2 = targetPos.cpy()

  initMovement(bulletType.bulletTrajectory)

  width = bulletType.baseBulletSize

  var damage: Float = 1.0f
  var explosionRadius: Float = bulletType.baseBulletExplosionSize
  var explosionDamage: Float = bulletType.baseBulletDamage * 0.5f
  private val explosionCollider = new CollisionObject2D(pos, Area2D.Circle, explosionRadius, 0, cLayerZ, cLayerMask, lifeTime = Some(0.1f)) with Component[Bullet] {
    override val parent: Bullet = Bullet.this
  }

  val bulletParticle = new Particle2D("res/shaders/electrical_ball.fp", pos)
  bulletParticle.setUniform("u_radius", bulletType.baseBulletSize)
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


  sealed trait Type{
    val images: ArrayBuffer[BitmapImage]
    val baseBulletSpeed: Float
    val baseBulletCooldown: Float
    val baseBulletSize: Float
    val baseBulletExplosionSize: Float
    val baseBulletDamage: Float
    val bulletTrajectory: Movement2D.Trajectory
  }


  case object Piercing extends Type{
    override val images: ArrayBuffer[BitmapImage] = ArrayBuffer(new BitmapImage("res/sprites/soccer.png"))
    override val baseBulletSpeed: Float = 400.0f
    override val baseBulletCooldown: Float = 1.0f
    override val baseBulletSize: Float = 8.0f
    override val baseBulletExplosionSize: Float = 8.0f
    override val baseBulletDamage: Float = 10.0f
    override val bulletTrajectory: Movement2D.Trajectory = Movement2D.Linear
  }

  case object Explosive extends Type {
    override val images: ArrayBuffer[BitmapImage] = ArrayBuffer(new BitmapImage("res/sprites/soccer.png"))
    override val baseBulletSpeed: Float = 150.0f
    override val baseBulletCooldown: Float = 0.1f
    override val baseBulletSize: Float = 10.0f
    override val baseBulletExplosionSize: Float = 100.0f
    override val baseBulletDamage: Float = 1.0f
    override val bulletTrajectory: Movement2D.Trajectory = Movement2D.Sinus
  }

  case object Bomb extends Type {
    override val images: ArrayBuffer[BitmapImage] = ArrayBuffer(new BitmapImage("res/sprites/soccer.png"))
    override val baseBulletSpeed: Float = ???
    override val baseBulletCooldown: Float = 2.0f
    override val baseBulletSize: Float = ???
    override val baseBulletExplosionSize: Float = ???
    override val baseBulletDamage: Float = ???
    override val bulletTrajectory: Movement2D.Trajectory = Movement2D.Spiral
  }
}




