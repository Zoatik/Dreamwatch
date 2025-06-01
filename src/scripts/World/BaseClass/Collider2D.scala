package scripts.World.BaseClass

import com.badlogic.gdx.math.Vector2
import scripts.{Globals, Layer, Layers, Sprite}

import scala.collection.mutable.ArrayBuffer



object Collider2D {
  val collisionLayers: Layers[Collider2D] = new Layers[Collider2D](1)

  def update(layerZ: Int = -1): Unit = {
    if (layerZ >= 0)
      checkCollisions(collisionLayers.get(layerZ).get)

    else {
      for (layer <- collisionLayers.get()) {
        checkCollisions(layer)
      }
    }
  }

  def create(area2D: Area2D, parent: Object2D, layerZ: Int): Collider2D = {
    val newCollider = new Collider2D(area2D, parent)
    collisionLayers.add(layerZ, newCollider)
    newCollider
  }

  def destroy(collider2D: Collider2D): Unit = {
    collisionLayers.remove(collider2D)
  }

  private def checkCollisions(layer: Layer[Collider2D]): Unit = {
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

}

private class Collider2D(val collisionArea2D: Area2D, val parent: Object2D) {
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
}


