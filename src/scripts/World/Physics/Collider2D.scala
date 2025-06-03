package scripts.World.Physics

import scripts.World.Actors.Base.{Entity, Object2D}

import scala.collection.mutable.ArrayBuffer


trait Collider2D{ self: Entity =>
  val collisionArea2D: Area2D
  val parent: Object2D
  var collisionLayerZ: Int

  private val collisionEventListeners: ArrayBuffer[Collider2D => Unit] = ArrayBuffer(other => onCollision(other) )

  protected def onCollision(other: Collider2D): Unit

  def addCollisionListener(listener: Collider2D => Unit): Unit = {
    collisionEventListeners += listener
  }

  def collided(other: Collider2D): Unit = {
    collisionEventListeners.foreach(_(other))
  }

  def collidesWith(other: Collider2D): Boolean = {
    if (other != this)
      return collisionArea2D.intersects(other.collisionArea2D)
    false
  }



}


