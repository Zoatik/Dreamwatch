package scripts.World

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.World.Actors.Base.Entity
import scripts.World.Physics.{Collider2D, Movement2D}
import scripts.World.graphics.Graphics2D
import scripts.{Globals, Layers}

import scala.collection.mutable.ArrayBuffer

class Scene2D {
  private val gLayers: Layers[Graphics2D] = new Layers(Globals.G_LAYERS_SIZE)
  private val cLayers: Layers[Collider2D] = new Layers(Globals.C_LAYERS_SIZE)
  private val movableObjects: ArrayBuffer[Movement2D] = ArrayBuffer()

  var deltaScale: Float = 1.0f


  def add(entity: Entity): Unit = {
    entity match {
      case g: Graphics2D =>
        val z: Int = g.graphicLayerZ
        if (z >= 0 && z < Globals.G_LAYERS_SIZE)
          gLayers.add(z, g)
    }

    entity match {
      case c: Collider2D =>
        val z: Int = c.collisionLayerZ
        if (z >= 0 && z < Globals.C_LAYERS_SIZE)
          cLayers.add(z, c)

      case _ =>
    }

    entity match {
      case m: Movement2D =>
        movableObjects += m

      case _ =>
    }

  }

  def remove(entity: Entity): Unit = {
    entity match {
      case g: Graphics2D =>
        gLayers.remove(g)
    }

    entity match {
      case c: Collider2D =>
        cLayers.remove(c)

      case _ =>
    }

    entity match {
      case m: Movement2D =>
        movableObjects -= m

      case _ =>
    }
  }


  def updateCollisions(deltaT: Float): Unit = {
    for (layer <- cLayers.get())
      CollisionsManager.update(deltaT, CollisionContext(layer))
  }

  def updateMovement(deltaT: Float): Unit = {
    MovementsManager.update(deltaT, MovementContext(movableObjects, deltaScale))
  }

  def updateGraphics(deltaT: Float, g: GdxGraphics): Unit = {
    for (layer <- gLayers.get()) {
      RenderingManager.update(deltaT, RenderingContext(layer, g))
    }

    // then renders the UI elements
    /*for (layer <- uiLayers.get()){
      layer.elements.foreach(uiElement => return) // TODO: handle ui elements
    }*/
  }

}
