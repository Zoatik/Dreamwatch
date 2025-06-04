package scripts.World.Physics

import scripts.World.Actors.Base.{Entity, Object2D}
import scala.collection.mutable.ArrayBuffer

/**
 * Trait for any object that has a collision area and participates in collision detection.
 * Classes mixing in Collider2D must be Entities and provide a collision shape and layer.
 */
trait Collider2D { self: Entity =>
  /**
   * The Area2D shape used for collision tests for this object.
   */
  val collisionArea2D: Area2D

  /**
   * Reference to this object's parent Object2D, used for hierarchical transforms.
   */
  val parent: Object2D

  /**
   * Index of the collision layer this Collider2D belongs to.
   * Collisions are only checked between objects in the same layer.
   */
  var collisionLayerZ: Int

  private val collisionEventListeners: ArrayBuffer[Collider2D => Unit] =
    ArrayBuffer(other => onCollision(other))

  /**
   * Callback invoked when this object collides with another Collider2D.
   * Subclasses implement this to respond to collision events.
   *
   * @param other The other Collider2D involved in the collision.
   */
  protected def onCollision(other: Collider2D): Unit

  /**
   * Register a listener function to be invoked when a collision occurs.
   *
   * @param listener Function that takes the other Collider2D as argument.
   */
  def addCollisionListener(listener: Collider2D => Unit): Unit = {
    collisionEventListeners += listener
  }

  /**
   * Invoke all registered collision listeners, notifying them of a collision.
   *
   * @param other The other Collider2D involved in the collision.
   */
  def collided(other: Collider2D): Unit = {
    collisionEventListeners.foreach(_(other))
  }

  /**
   * Check whether this Collider2D overlaps with another, based on their collision shapes.
   * Returns false if the other is the same instance.
   *
   * @param other The other Collider2D to test against.
   * @return True if the shapes intersect, false otherwise.
   */
  def collidesWith(other: Collider2D): Boolean = {
    if (other != this)
      return collisionArea2D.intersects(other.collisionArea2D)
    false
  }
}


