package scripts.World.Physics

import com.badlogic.gdx.math.Vector2
import scripts.Managers.SceneManager
import scripts.World.Actors.Base.{Entity, Object2D}
import scripts.World.Actors.TopLevel.Scene2D
import scripts.{Layer, Sprite}

import scala.collection.mutable.ArrayBuffer


trait Collider2D{ self: Entity =>
  val collisionArea2D: Area2D
  val parent: Object2D
  var collisionLayerZ: Int

  private val collisionEventListeners: ArrayBuffer[Collider2D => Unit] = ArrayBuffer()

  def onCollision(listener: Collider2D => Unit): Unit = {
    collisionEventListeners += listener
  }

  def collided(other: Collider2D): Unit = {
    collisionEventListeners.foreach(_(other))
  }

  def collidesWith(other: Collider2D): Boolean = {
    collisionArea2D.intersects(other.collisionArea2D)
  }



}


