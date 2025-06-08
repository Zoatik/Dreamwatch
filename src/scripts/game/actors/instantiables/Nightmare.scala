package scripts.game.actors.instantiables

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Component
import scripts.dreamwatch_engine.actors.instantiables.{CollisionObject2D, CollisionSprite2D}
import scripts.dreamwatch_engine.physics.{Area2D, Collider2D, Movement2D}
import scripts.game.actors.instantiables.Nightmare.baseNightmareSpeed
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

class Nightmare (pos: Vector2,
                 targetPos: Vector2,
                 nightmareType: Nightmare.Type,
                 lifeTime: Option[Float] = None,
                )
  extends CollisionSprite2D(
    pos,
    Nightmare.loadImagesFor(nightmareType),
    Globals.NIGHTMARE_G_LAYER,
    Globals.NIGHTMARE_C_LAYER,
    Globals.NIGHTMARE_C_LAYERMASK,
    Area2D.Circle,
    lifeTime = lifeTime
  ) with Movement2D {

  override var speed: Float = baseNightmareSpeed(nightmareType)
  override var target: Vector2 = targetPos.cpy

  override def instantiate(): Nightmare = {
    super.instantiate()
    this
  }

  override protected def onCollision(other: Collider2D): Unit = other match{
    case _: Bullet => this.destroy()
    case _: CollisionObject2D with Component[Bullet] => this.destroy()
    case _ =>
  }

}

object Nightmare {
  private def loadImagesFor(nightmareType: Type): ArrayBuffer[BitmapImage] = nightmareType match {
    case Small => ArrayBuffer.fill(1)(new BitmapImage("res/sprites/cloud.png"))
    case Big => ArrayBuffer.fill(1)(new BitmapImage("res/sprites/cloud.png"))
    case Laser => ArrayBuffer.fill(1)(new BitmapImage("res/sprites/cloud.png"))
    case _ => ArrayBuffer.fill(1)(new BitmapImage("res/sprites/cloud.png"))

  }

  private def baseNightmareRadius(nightmareType: Type): Float = nightmareType match {
    case Small => 50.0f
    case Big => 60.0f
    case Laser => 45.0f
    case _ => 50.0f
  }

  private def baseNightmareSpeed(nightmareType: Type): Float = nightmareType match {
    case Small => 100.0f
    case Big => 200.0f
    case Laser => 150.0f
    case _ => 100.0f
  }


  sealed trait Type

  case object Small extends Type

  case object Big extends Type

  case object Laser extends Type
}
