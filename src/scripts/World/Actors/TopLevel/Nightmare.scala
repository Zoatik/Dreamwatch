package scripts.World.Actors.TopLevel

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.Sprite
import scripts.World.Actors.Base.CollisionObject2D
import scripts.World.Physics.{Area2D, CircleArea2D, Movement2D}

import scala.collection.mutable.ArrayBuffer

class Nightmare (pos: Vector2,
                 nightmareType: Nightmare.Type,
                 graphicLayerZ: Int,
                 collisionLayerZ: Int,
                 override var _speed: Vector2 = new Vector2(0,0)
                )
  extends CollisionObject2D(pos,
    Nightmare.loadSpriteFor(nightmareType, pos),
    graphicLayerZ,
    Nightmare.baseCollisionArea2D(nightmareType, pos),
    collisionLayerZ
  ) with Movement2D {


}

object Nightmare {
  private def loadSpriteFor(nightmareType: Type, pos: Vector2): Sprite = nightmareType match {
    case Small => new Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/cloud.png")), pos)
    case Big => new Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/cloud.png")), pos)
    case Laser => new Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/cloud.png")), pos)
  }

  private def baseNightmareRadius(nightmareType: Type): Float = nightmareType match {
    case Small => 50.0f
    case Big => 60.0f
    case Laser => 45.0f
  }

  private def baseNightmareSpeed(nightmareType: Type): Float = nightmareType match {
    case Small => 100.0f
    case Big => 200.0f
    case Laser => 150.0f
  }

  private def baseCollisionArea2D(nightmareType: Type, pos: Vector2): Area2D = {
    new CircleArea2D(pos, baseNightmareRadius(nightmareType))
  }

  def spawn(pos: Vector2, target: Vector2, nightmareType: Type): Nightmare = {
    val newNightmare: Nightmare = new Nightmare(pos, nightmareType, 0, 0)
    val direction: Vector2 = target.sub(pos).nor()
    val speed: Float = baseNightmareSpeed(nightmareType)
    newNightmare.speed = direction.scl(speed)
    newNightmare.spawn().asInstanceOf[Nightmare]
  }

  sealed trait Type

  case object Small extends Type

  case object Big extends Type

  case object Laser extends Type
}
