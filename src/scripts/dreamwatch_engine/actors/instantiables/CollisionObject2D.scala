package scripts.dreamwatch_engine.actors.instantiables

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Object2D
import scripts.dreamwatch_engine.physics.{Area2D, Collider2D}
import scripts.game.GameManager

import scala.collection.mutable.ArrayBuffer

/**
 * Abstract base for any 2D collision‐enabled object in the world.
 * Extends Object2D and mixes in Collider2D to handle physics collisions.
 *
 * @param pos              Initial position of the object (passed to Object2D).
 * @param sprite           The Sprite used for rendering this object.
 * @param graphicLayerZ    Z-index for rendering layer (handled by Graphics2D).
 * @param collisionArea2D  Area2D defining the collision shape (circle or box).
 * @param collisionLayerZ  Index of the collision layer this object belongs to.
 * @param lifeTime         Optional lifetime (in seconds). If defined, the object will be destroyed after expiration.
 * @param parent           Optional parent Object2D (for hierarchical transforms), default is null.
 */
class CollisionObject2D(
  pos: Vector2,
  angle: Float,
  override var areaType: Area2D.Type,
  var width: Float ,
  var height: Float ,
  override var cLayerZ: Int,
  override var cLayerMask: ArrayBuffer[Int],
  lifeTime: Option[Float] = None
) extends Object2D(pos, angle, lifeTime) with Collider2D {



  /**
   * Override spawn to run a collision check immediately upon creation.
   * This ensures that if the object spawns overlapping another, the collision
   * event is fired right away.
   *
   * @return This entity, for chaining if needed.
   */
  override def instantiate(): CollisionObject2D = {
    super.instantiate()
    // Perform an immediate collision check with all colliders in the same layer
    //val cLayer: Layer[Collider2D] = GameManager.currentScene.cLayers.get(cLayerZ).get
    Collider2D.checkAndNotifyCollisions(this, GameManager.currentScene.cLayers)
    this
  }

  /**
   * Callback invoked when this object collides with another Collider2D.
   * Subclasses implement this to respond to collision events.
   *
   * @param other The other Collider2D involved in the collision.
   */
  override protected def onCollision(other: Collider2D): Unit = {}

  /**
   * Remove this entity from the current scene via the ScenesManager.
   * Called when the entity should no longer exist (e.g., lifetime expired or explicit destroy).
   */

  override def destroy(): Unit = {
    super.destroy()
    unbindEvents()
  }



}