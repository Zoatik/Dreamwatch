package scripts.World.Physics

import com.badlogic.gdx.math.Vector2
import scripts.World.Actors.Base.{Entity, Object2D}
import scripts.World.Scene2D
import scripts.{Layer, Sprite}

import scala.collection.mutable.ArrayBuffer

object Collider2D {
  def checkAndNotifyCollisions(layer: Layer[Collider2D]): Unit = {
    val size = layer.size
    val elements = layer.elements
    var i = 0
    while (i < size) {
      var j = i + 1
      while (j < size) {
        val a = elements(i)
        val b = elements(j)
        if (a.collidesWith(b)) {
          a.collided(b)
          b.collided(a)
        }
        j += 1
      }
      i += 1
    }
  }

  def checkAndNotifyCollisions(collider2D: Collider2D, layer: Layer[Collider2D]): Unit = {
    layer.elements.foreach(_.collidesWith(collider2D))
  }

  def checkAndNotifyCollisions(a: Collider2D, b: Collider2D): Unit = {
    if (checkCollision(a, b))
      notifyCollision(a, b)
  }

  def checkCollision(a: Collider2D, b: Collider2D): Boolean = {
    a.collidesWith(b)
  }

  private def notifyCollision(a: Collider2D, b: Collider2D): Unit = {
    a.collided(b)
    b.collided(a)
  }

}


trait Collider2D{ self: Entity =>
  val collisionArea2D: Area2D
  val parent: Object2D
  var collisionLayerZ: Int
  //Scene2D.addToCurrentScene(this, layerZ)

  private val collisionEventListeners: ArrayBuffer[Collider2D => Unit] = ArrayBuffer()

  def onCollision(listener: Collider2D => Unit): Unit = {
    collisionEventListeners += listener
  }

  protected def collided(other: Collider2D): Unit = {
    collisionEventListeners.foreach(_(other))
  }

  def collidesWith(other: Collider2D): Boolean = {
    collisionArea2D.intersects(other.collisionArea2D)
  }

  override def destroy(): Unit = {
    Scene2D.removeFromCurrentScene(this)
  }

}


