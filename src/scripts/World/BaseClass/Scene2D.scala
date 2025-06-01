package scripts.World.BaseClass

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.World.Process
import scripts.{Globals, Layers}

import scala.collection.mutable.ArrayBuffer

object Scene2D extends Process[GdxGraphics] {
  val scenes: ArrayBuffer[Scene2D] = ArrayBuffer.fill(Globals.NB_OF_SCENES)(new Scene2D)
  var currentScene: Scene2D = scenes(0)

  override def init(): Unit = {}

  override def update(deltaT: Float, arg: GdxGraphics): Unit = {}

  def addToCurrentScene(elem: Any, layerZ: Int = 0): Unit = {
    if (currentScene != null) currentScene.add(elem, layerZ)
  }

  def addToScene(sceneIdx: Int, elem: Any, layerZ: Int = 0): Unit = {
    if (0 <= sceneIdx && sceneIdx < scenes.length)
      scenes(sceneIdx).add(elem, layerZ)
  }

  def removeFromScene(): Unit = ???


}


class Scene2D {
  val gLayers: Layers[Graphics2D] = new Layers(Globals.G_LAYERS_SIZE)
  val cLayers: Layers[Collider2D] = new Layers(Globals.C_LAYERS_SIZE)
  val movableObjects: ArrayBuffer[Movement2D] = ArrayBuffer()


  def add(elem: Any, layerZ: Int = 0): Unit = elem match {
    case g: Graphics2D if layerZ >= 0 && layerZ < Globals.G_LAYERS_SIZE =>
      gLayers.add(layerZ, g)

    case c: Collider2D if layerZ >= 0 && layerZ < Globals.C_LAYERS_SIZE =>
      cLayers.add(layerZ, c)

    case m: Movement2D =>
      movableObjects += m

    case _ =>

  }


  def init(): Unit = {

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

}
