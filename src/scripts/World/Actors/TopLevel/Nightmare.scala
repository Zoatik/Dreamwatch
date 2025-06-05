package scripts.World.Actors.TopLevel

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.Globals
import scripts.World.Actors.Base.{CollisionObject2D, CollisionSprite2D, Component}
import scripts.World.Actors.TopLevel.Nightmare.baseNightmareSpeed
import scripts.World.Physics.{Area2D, Collider2D, Movement2D}

import scala.collection.mutable.ArrayBuffer

class Nightmare (pos: Vector2,
                 targetPos: Vector2,
                 nightmareType: Nightmare.Type,
                 lifeTime: Option[Float] = None,
                 graphicLayerZ: Int = Globals.NIGHTMARE_G_LAYER,
                 collisionLayerZ: Int = Globals.NIGHTMARE_C_LAYER
                )
  extends CollisionSprite2D(
    pos,
    Nightmare.loadImagesFor(nightmareType),
    graphicLayerZ,
    collisionLayerZ,
    Area2D.Circle,
    lifeTime = lifeTime
  ) with Movement2D {

  override var speed: Float = baseNightmareSpeed(nightmareType)
  override var target: Vector2 = targetPos.cpy

  override protected def onCollision(other: Collider2D): Unit = other match{
    case _: Bullet => this.destroy()
    case _: CollisionObject2D with Component => this.destroy()
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
