package scripts.game.actors.instantiables

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Component
import scripts.dreamwatch_engine.actors.instantiables.{CollisionObject2D, CollisionSprite2D, Particle2D}
import scripts.dreamwatch_engine.physics.{Area2D, Collider2D, Movement2D}
import scripts.game.MusicManager
import scripts.game.actors.abstracts.Nightmare
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer


class Bullet(pos: Vector2,
             targetPos: Vector2,
             bulletType: Bullet.Type,
             var speed: Float = 400.0f,
             var bulletSize: Float = 8.0f,
             var explosionSize: Float = 8.0f,
             var bulletDamage: Float = 10.0f,
             lifeTime: Option[Float] = None
            )
  extends CollisionSprite2D(
    pos,
    0,
    bulletType.images,
    Globals.BULLET_G_LAYER,
    Globals.BULLET_C_LAYER,
    Globals.BULLET_C_LAYERMASK,
    Area2D.Circle,
    lifeTime = lifeTime
  ) with Movement2D {

  override var target: Vector2 = targetPos.cpy()

  stopAtTarget = if(bulletType == Bullet.Piercing) false else true
  initMovement(bulletType.bulletTrajectory)


  width = bulletSize

  var explosionDamage: Float = bulletDamage * 0.5f
  private val explosionCollider = new CollisionObject2D(pos, 0, Area2D.Circle, explosionSize, 0, cLayerZ, cLayerMask, lifeTime = Some(0.1f)) with Component[Bullet] {
    override val parent: Bullet = Bullet.this
  }


  override def update(deltaT: Float): Unit = {
    super.update(deltaT)
    Particle2D.spawnParticles(pos, bulletSize/2, 0.3f, 500.0f, 1, "res/sprites/game/star.png", gLayerZ)
  }

  override def instantiate(): Bullet = {
    super.instantiate()
    this
  }

  override protected def onCollision(other: Collider2D): Unit = other match {
    case _: Nightmare =>

      if(this.bulletType != Bullet.Piercing) {
        this.destroy()
        this.explode()
      }
    case _: Boss =>
      if(this.bulletType != Bullet.Piercing) {
        this.explode()
        this.destroy()
      }
    case _ =>
  }

  override protected def onTargetReached(): Unit = {
    if(this.bulletType != Bullet.Piercing) {
      this.explode()
      this.destroy()
    }
  }


  private def explode(): Unit = {
    explosionCollider.instantiate()
    Particle2D.spawnParticles(pos, bulletSize, 1.0f, 500.0f, 20, "res/sprites/game/texture.png", gLayerZ)
    MusicManager.playSound("explosion_sound")
  }

  override def destroy(): Unit = {
    super.destroy()
  }



}

/** Sealed trait pour les types de projectiles */
object Bullet {


  sealed trait Type{
    val images: ArrayBuffer[String]
    val baseBulletSpeed: Float
    val baseBulletCooldown: Float
    val baseBulletSize: Float
    val baseBulletExplosionSize: Float
    val baseBulletDamage: Float
    val bulletTrajectory: Movement2D.Trajectory
  }


  case object Piercing extends Type{
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/ui/card.png")
    override val baseBulletSpeed: Float = 2000.0f
    override val baseBulletCooldown: Float = 0.5f
    override val baseBulletSize: Float = 4.0f
    override val baseBulletExplosionSize: Float = 0.1f
    override val baseBulletDamage: Float = 50.0f
    override val bulletTrajectory: Movement2D.Trajectory = Movement2D.Linear
  }

  case object Explosive extends Type {
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/placeholders/soccer.png")
    override val baseBulletSpeed: Float = 400.0f
    override val baseBulletCooldown: Float = 0.5f
    override val baseBulletSize: Float = 10.0f
    override val baseBulletExplosionSize: Float = 150.0f
    override val baseBulletDamage: Float = 30.0f
    override val bulletTrajectory: Movement2D.Trajectory = Movement2D.Sinus
  }

  case object Bomb extends Type {
    override val images: ArrayBuffer[String] = ArrayBuffer("res/sprites/placeholders/soccer.png")
    override val baseBulletSpeed: Float = 300.0f
    override val baseBulletCooldown: Float = 1.0f
    override val baseBulletSize: Float = 50.0f
    override val baseBulletExplosionSize: Float = 400.0f
    override val baseBulletDamage: Float = 70.0f
    override val bulletTrajectory: Movement2D.Trajectory = Movement2D.Spiral
  }
}




