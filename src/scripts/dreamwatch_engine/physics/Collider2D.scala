package scripts.dreamwatch_engine.physics

import scripts.dreamwatch_engine.utils.{Layer, Layers}

import scala.collection.mutable.ArrayBuffer

/**
 * Trait for any object that has a collision area and participates in collision detection.
 * Classes mixing in Collider2D must be Entities and provide a collision shape and layer.
 */
trait Collider2D extends Area2D {

  /**
   * Index of the collision layer this Collider2D belongs to.
   * Collisions are only checked between objects in the same layer.
   */
  var cLayerZ: Int
  var cLayerMask: ArrayBuffer[Int]

  private val otherColliders: ArrayBuffer[Collider2D] = ArrayBuffer()

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
  private def collided(other: Collider2D): Unit = {
    if (!otherColliders.contains(other)) {
      otherColliders.addOne(other)
      collisionEventListeners.foreach(_(other))
    }
  }

  /**
   * Check whether this Collider2D overlaps with another, based on their collision shapes.
   * Returns false if the other is the same instance.
   *
   * @param other The other Collider2D to test against.
   * @return True if the shapes intersect, false otherwise.
   */
  def collidesWith(other: Collider2D): Boolean = {
    if (other != this && this.cLayerMask.contains(other.cLayerZ))
      return this.intersects(other)
    false
  }
}

object Collider2D{

  def checkAndNotifyCollisions(c: Collider2D, layers: Layers[Collider2D]): Unit = {
    for (z <- c.cLayerMask){
      val layer: Option[Layer[Collider2D]] = layers.get(z)
      if(layer.isDefined){
        for (other <- layer.get.elements.toArray){
          if(c.collidesWith(other)){
            c.collided(other)
            other.collided(c)
          }
        }
      }
    }

  }

  def checkAndNotifyCollisions(layers: Layers[Collider2D]): Unit = {
    layers.get().foreach(layer => {
      layer.elements.foreach(collider => {
        checkAndNotifyCollisions(collider, layers)
      })
    })
  }

  def checkAndNotifyCollisions(layer: Layer[Collider2D]): Unit = {
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


