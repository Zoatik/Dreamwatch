package scripts.World.Actors.Base

import com.badlogic.gdx.math.Vector2
import scripts.Managers.{CollisionsManager, ScenesManager}
import scripts.Sprite
import scripts.World.Physics.{Area2D, BoxArea2D, CircleArea2D, Collider2D}

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
abstract class CollisionObject2D(
  pos: Vector2,
  sprite: Sprite,
  graphicLayerZ: Int,
  override val collisionArea2D: Area2D,
  override var collisionLayerZ: Int,
  lifeTime: Option[Float] = None,
  override val parent: Object2D = null
) extends Object2D(pos, sprite, graphicLayerZ, lifeTime) with Collider2D {

  // Calculate sprite width based on collision shape:
  // - If CircleArea2D, width is diameter.
  // - If BoxArea2D, width is rectangle width.
  private val spriteWidth: Float = collisionArea2D match {
    case c: CircleArea2D => 2 * c.radius
    case b: BoxArea2D    => b.width
  }
  // Apply computed width to the sprite for proper rendering scale
  sprite.setWidth(spriteWidth)

  /**
   * Override spawn to run a collision check immediately upon creation.
   * This ensures that if the object spawns overlapping another, the collision
   * event is fired right away.
   *
   * @return This entity, for chaining if needed.
   */
  override def spawn(): Entity = {
    // Perform an immediate collision check with all colliders in the same layer
    CollisionsManager.checkAndNotifyCollisions(
      this,
      ScenesManager.currentScene
        .getCollisionLayers
        .get(this.collisionLayerZ)
        .get
    )
    // Delegate to Object2D.spawn, which adds to scene and returns `this`
    super.spawn()
  }
}