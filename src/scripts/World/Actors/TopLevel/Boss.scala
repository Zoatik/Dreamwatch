package scripts.World.Actors.TopLevel

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.{Circle, Vector2}
import scripts.{Globals, Sprite}
import scripts.World.Actors.Base.CollisionObject2D
import scripts.World.Physics.{Area2D, BoxArea2D, CircleArea2D, Collider2D, Movement2D}

import scala.collection.mutable.ArrayBuffer


class Boss(pos: Vector2,
           bossType: Boss.Type,
           graphicLayerZ: Int = Globals.BOSS_G_LAYER,
           collisionLayerZ: Int = Globals.BOSS_C_LAYER
          )
  extends CollisionObject2D(
    pos,
    Boss.loadSpriteFor(bossType, pos),
    graphicLayerZ,
    Boss.baseCollisionArea2D(bossType, pos),
    collisionLayerZ
  ) with Movement2D {

  override var speed: Float = 0
  override var target: Vector2 = new Vector2(0,0)


  override protected def onCollision(other: Collider2D): Unit = {
    // Does nothing atm
  }
}

object Boss{
  private def loadSpriteFor(bossType: Type, pos: Vector2): Sprite = bossType match {
    case UneAraignee => Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/UneAraignee.png")), pos)
    case Ghost => Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/Ghost.png")), pos)
    case TheGrimReaper => Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/TheGrimReaper.png")), pos)
    case _ => Sprite(ArrayBuffer.fill(1)(new BitmapImage("res/sprites/UneAraignee.png")), pos)
  }
  private def baseBossRadius(bossType: Type): Float = bossType match {
    case UneAraignee => 500.0f
    case Ghost => 500.0f
    case TheGrimReaper => 500.0f
    case _ => 500.0f
  }
  private def baseCollisionArea2D(bossType: Type, pos: Vector2): Area2D = {
    new BoxArea2D(pos, baseBossRadius(bossType), baseBossRadius(bossType))
  }



  sealed trait Type

  case object UneAraignee extends Type
  case object Ghost extends Type
  case object TheGrimReaper extends Type
  case object ZeMudry extends Type
}