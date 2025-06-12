package scripts.dreamwatch_engine.actors.instantiables

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.physics.{Area2D, Collider2D}

import scala.collection.mutable.ArrayBuffer

class CollisionSprite2D(pos: Vector2,
                        angle: Float,
                        images: ArrayBuffer[String],
                        gLayerZ: Int,
                        override var cLayerZ: Int,
                        override var cLayerMask: ArrayBuffer[Int],
                        area2DType: Area2D.Type,
                        animDuration: Float = 0f,
                        spriteScale: Float = 1f,
                        lifeTime: Option[Float] = None)
  extends Sprite2D(pos, angle, images, gLayerZ, area2DType, animDuration, spriteScale, lifeTime) with Collider2D{

  override def instantiate(): CollisionSprite2D = {
    super.instantiate()
    this
  }
  /**
   * Callback invoked when this object collides with another Collider2D.
   * Subclasses implement this to respond to collision events.
   *
   * @param other The other Collider2D involved in the collision.
   */
  override protected def onCollision(other: Collider2D): Unit = {}

  override def destroy(): Unit = {
    super.destroy()
    unbindEvents()
  }

}
