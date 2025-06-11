package scripts.game.actors.abstracts

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Component
import scripts.dreamwatch_engine.actors.instantiables.{CollisionObject2D, CollisionSprite2D}
import scripts.dreamwatch_engine.physics.{Area2D, Collider2D, Movement2D}
import scripts.game.actors.instantiables.Bullet
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

abstract class Nightmare (pos: Vector2,
                          targetPos: Vector2,
                          images: ArrayBuffer[String],
                          animDuration: Float,
                          scale: Float,
                          lifeTime: Option[Float] = None,
                          )
  extends CollisionSprite2D(
    pos,
    0,
    images,
    Globals.NIGHTMARE_G_LAYER,
    Globals.NIGHTMARE_C_LAYER,
    Globals.NIGHTMARE_C_LAYERMASK,
    Area2D.Circle,
    animDuration,
    spriteScale = scale,
    lifeTime = lifeTime
  ) with Movement2D {

  override var speed: Float
  override var target: Vector2 = targetPos.cpy


  override protected def onCollision(other: Collider2D): Unit = other match{
    case _: Bullet => this.destroy()
    case _: CollisionObject2D with Component[Bullet] => this.destroy()
    case _ =>
  }

}

