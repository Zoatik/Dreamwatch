package scripts.World.BaseClass

import com.badlogic.gdx.math.Vector2
import scripts.World.Process
import scripts.{Globals, Layer, Layers, Sprite}

import scala.collection.mutable.ArrayBuffer

object Collider2D {
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

class CollisionObject2D(pos: Vector2, sprite: Sprite,
                        override val collisionArea2D: Area2D,
                        override val parent: Object2D,
                        override var layerZ: Int)
  extends Object2D(pos, sprite) with Collider2D{

}

trait Collider2D{
  val collisionArea2D: Area2D
  val parent: Object2D
  var layerZ: Int
  Scene2D.addToCurrentScene(this, layerZ)

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

  def destroy(): Unit = {
    Collider2D.collisionLayers.remove(this)
  }

}


