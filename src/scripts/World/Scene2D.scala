package scripts.World

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.World.Actors.Base.{Entity, Object2D}
import scripts.World.Physics.{Collider2D, Movement2D}
import scripts.World.graphics.Graphics2D
import scripts.{Globals, Layers}

import scala.collection.mutable.ArrayBuffer

object Scene2D extends GraphicProcess {
  val scenes: ArrayBuffer[Scene2D] = ArrayBuffer.fill(Globals.NB_OF_SCENES)(new Scene2D)
  var currentScene: Scene2D = scenes(0)

  override def init(): Unit = {}

  override def update(deltaT: Float): Unit = {
    currentScene.updateCollisions()
    currentScene.updateMovement(deltaT)
  }

  override def updateGraphics(deltaT: Float, g: GdxGraphics): Unit = {
    currentScene.render(g)
  }

  def addToCurrentScene(entity: Entity): Unit = {
    if (currentScene != null) currentScene.add(entity)
  }

  def addToScene(sceneIdx: Int, entity: Entity): Unit = {
    if (0 <= sceneIdx && sceneIdx < scenes.length)
      scenes(sceneIdx).add(entity)
  }

  def removeFromCurrentScene(entity: Entity): Unit = {
    if (currentScene != null) currentScene.remove(entity)
  }

  def removeFromScene(sceneIdx: Int, entity: Entity): Unit = {
    if (0 <= sceneIdx && sceneIdx < scenes.length)
      scenes(sceneIdx).remove(entity)
  }


}


class Scene2D {
  private val gLayers: Layers[Graphics2D] = new Layers(Globals.G_LAYERS_SIZE)
  private val cLayers: Layers[Collider2D] = new Layers(Globals.C_LAYERS_SIZE)
  private val movableObjects: ArrayBuffer[Movement2D] = ArrayBuffer()


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


  def render(g: GdxGraphics): Unit = {
    // renders the world elements first
    for (layer <- gLayers.get()) {
      layer.elements.foreach(graphic2D => graphic2D.draw(g))
    }

    // then renders the UI elements
    /*for (layer <- uiLayers.get()){
      layer.elements.foreach(uiElement => return) // TODO: handle ui elements
    }*/
  }

  def updateCollisions(): Unit = {
    for (layer <- cLayers.get())
      Collider2D.checkAndNotifyCollisions(layer)
  }

  def updateMovement(deltaT: Float): Unit = {
    movableObjects.foreach(_.move(deltaT))
  }

}
