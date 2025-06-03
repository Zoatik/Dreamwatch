package scripts.World.Actors.TopLevel

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.{Globals, Sprite}
import scripts.World.Actors.Base.CollisionObject2D
import scripts.World.Actors.TopLevel.Nightmare.baseNightmareSpeed
import scripts.World.Physics.{Area2D, CircleArea2D, Collider2D, Movement2D}

import scala.collection.mutable.ArrayBuffer

class Nightmare (pos: Vector2,
                 targetPos: Vector2,
                 nightmareType: Nightmare.Type,
                 lifeTime: Option[Float] = None,
                 graphicLayerZ: Int = Globals.NIGHTMARE_G_LAYER,
                 collisionLayerZ: Int = Globals.NIGHTMARE_C_LAYER
                )
  extends CollisionObject2D(pos,
    Nightmare.loadSpriteFor(nightmareType, pos),
    graphicLayerZ,
    Nightmare.baseCollisionArea2D(nightmareType, pos),
    collisionLayerZ,
    lifeTime
  ) with Movement2D {

  override var speed: Float = baseNightmareSpeed(nightmareType)
  override var target: Vector2 = targetPos.cpy

  override protected def onCollision(other: Collider2D): Unit = other match{
    case _: Bullet => this.destroy()
    case _: CollisionComponent => this.destroy()
    case _ =>
  }

}

object Nightmare {
  private def loadSpriteFor(nightmareType: Type, pos: Vector2): Sprite = nightmareType match {
    case Small => Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/cloud.png")), pos)
    case Big => Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/cloud.png")), pos)
    case Laser => Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/cloud.png")), pos)
    case _ => Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/cloud.png")), pos)
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

  private def baseCollisionArea2D(nightmareType: Type, pos: Vector2): Area2D = {
    new CircleArea2D(pos, baseNightmareRadius(nightmareType))
  }


  sealed trait Type

  case object Small extends Type

  case object Big extends Type

  case object Laser extends Type
}
