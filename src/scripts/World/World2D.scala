package scripts.World

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.World.BaseClass.{Collider2D, Movement2D}
import scripts.{Layers, Sprite}

import scala.collection.mutable.ArrayBuffer

object World2D extends Process[GdxGraphics]{_

  override def init(): Unit = {}


  override def update(deltaT: Float, arg: GdxGraphics): Unit = ???
}



  def updatePhysics(deltaT: Float): Unit = {
    Movement2D.update(deltaT)
    Collider2D.update()
    // TODO: gérer la physique (déplacement + collisions)
  }

}
