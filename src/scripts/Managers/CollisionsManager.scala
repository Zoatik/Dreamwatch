package scripts.Managers

import scripts.Layer
import scripts.World.Physics.Collider2D


object CollisionsManager extends Manager[CollisionContext]{

  override def init(): Unit = {
    println("CollisionsManager ready")
  }

  override def update(deltaT: Float, ctx: CollisionContext): Unit = {
    checkAndNotifyCollisions(ctx.cLayer)

  }

  private def checkAndNotifyCollisions(layer: Layer[Collider2D]): Unit = {
    val size = layer.size
    val elements = layer.elements.toArray
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

  private def checkAndNotifyCollisions(collider2D: Collider2D, layer: Layer[Collider2D]): Unit = {
    layer.elements.foreach(_.collidesWith(collider2D))
  }

  private def checkAndNotifyCollisions(a: Collider2D, b: Collider2D): Unit = {
    if (checkCollision(a, b))
      notifyCollision(a, b)
  }

  private def notifyCollision(a: Collider2D, b: Collider2D): Unit = {
    a.collided(b)
    b.collided(a)
  }

  def checkCollision(a: Collider2D, b: Collider2D): Boolean = {
    a.collidesWith(b)
  }

  def checkAndNotifyCollision(a: Collider2D, b: Collider2D): Unit = {
    if(checkCollision(a, b))
      notifyCollision(a, b)
  }
}