package scripts.World.Actors.Base

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.World.Physics.{Area2D, Collider2D}

import scala.collection.mutable.ArrayBuffer

class CollisionSprite2D(pos: Vector2,
                        images: ArrayBuffer[BitmapImage],
                        gLayerZ: Int,
                        override var cLayerZ: Int,
                        area2DType: Area2D.Type,
                        angle: Float = 0f,
                        spriteScale: Float = 1f,
                        lifeTime: Option[Float] = None)
  extends Sprite2D(pos, images, gLayerZ, area2DType, angle, spriteScale, lifeTime) with Collider2D{


  /**
   * Callback invoked when this object collides with another Collider2D.
   * Subclasses implement this to respond to collision events.
   *
   * @param other The other Collider2D involved in the collision.
   */
  override protected def onCollision(other: Collider2D): Unit = ???
}
