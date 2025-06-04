package scripts.Managers

import scripts.Layer
import scripts.World.Physics.Collider2D

/**
 * CollisionsManager is responsible for detecting and notifying collisions between Collider2D objects.
 * It manages collision checks for all colliders within a given collision layer, notifying each collider
 * when an overlap (collision) occurs. This manager is typically invoked once per frame.
 *
 * The collision detection is performed in an efficient pairwise manner, and notification is handled
 * via callbacks on the Collider2D instances.
 */
object CollisionsManager extends Manager[CollisionContext] {

  /**
   * Initializes the CollisionsManager.
   * This method is called once at startup to perform any necessary setup.
   */
  override def init(): Unit = {
    println("CollisionsManager ready")
  }

  /**
   * Called once per frame to update collision detection.
   * Retrieves the collision layer from the provided context and checks for collisions.
   *
   * @param deltaT Time elapsed since the last update (in seconds).
   * @param ctx    The CollisionContext containing the relevant Layer[Collider2D].
   */
  override def update(deltaT: Float, ctx: CollisionContext): Unit = {
    // Check all collisions within the specified collision layer
    checkAndNotifyCollisions(ctx.cLayer)
  }

  /**
   * Checks for collisions among all distinct pairs of colliders in the given layer.
   * If two colliders overlap, notifies both via their `collided` callback.
   *
   * @param layer The Layer containing Collider2D objects to test for collisions.
   */
  private def checkAndNotifyCollisions(layer: Layer[Collider2D]): Unit = {
    // Get the number of colliders in the layer
    val size = layer.size
    // Copy elements to an array for indexed access
    val elements = layer.elements.toArray
    var i = 0
    // Iterate over every unique pair (i, j) where i < j
    while (i < size) {
      var j = i + 1
      while (j < size) {
        val a = elements(i)
        val b = elements(j)
        // If the two colliders overlap, notify both
        if (a.collidesWith(b)) {
          a.collided(b)
          b.collided(a)
        }
        j += 1
      }
      i += 1
    }
  }

  /**
   * Checks and notifies a specific pair of colliders if they overlap.
   *
   * @param a First collider.
   * @param b Second collider.
   */
  private def checkAndNotifyCollisions(a: Collider2D, b: Collider2D): Unit = {
    if (checkCollision(a, b))
      notifyCollision(a, b)
  }

  /**
   * Notifies both colliders of a collision event.
   * Invokes the `collided` callback on both objects.
   *
   * @param a First collider (notified of collision with b).
   * @param b Second collider (notified of collision with a).
   */
  private def notifyCollision(a: Collider2D, b: Collider2D): Unit = {
    a.collided(b)
    b.collided(a)
  }

  /**
   * Determines whether two colliders overlap by delegating to their shapes.
   *
   * @param a First collider.
   * @param b Second collider.
   * @return True if their Area2D shapes intersect.
   */
  def checkCollision(a: Collider2D, b: Collider2D): Boolean = {
    a.collidesWith(b)
  }

  /**
   * Public method to check and notify a single pair of colliders.
   * If the colliders overlap, both are notified via their `collided` callback.
   *
   * @param a First collider.
   * @param b Second collider.
   */
  def checkAndNotifyCollision(a: Collider2D, b: Collider2D): Unit = {
    if (checkCollision(a, b))
      notifyCollision(a, b)
  }

  /**
   * Checks a single Collider2D against every element in the provided layer.
   * Invokes `collidesWith`, but does not trigger notifications here.
   *
   * @param collider2D Collider to test against the layer.
   * @param layer      Layer of Collider2D objects.
   */
  def checkAndNotifyCollisions(collider2D: Collider2D, layer: Layer[Collider2D]): Unit = {
    // Iterate through each collider in the layer and test for overlap
    layer.elements.foreach(_.collidesWith(collider2D))
  }
}