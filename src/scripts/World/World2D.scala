package scripts.World

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.World.BaseClass.Collider2D
import scripts.{Layers, Sprite}

import scala.collection.mutable.ArrayBuffer

object World2D {
  val scenes: ArrayBuffer[Scene] = ArrayBuffer.fill(3)(new Scene)
  var currentScene : Scene = _

  def init(): Unit = {
    currentScene = scenes(0)
    currentScene.init()
  }

  def update(g: GdxGraphics, deltaT: Float): Unit = {
    currentScene.updatePhysics(deltaT)
    currentScene.render(g)
  }

}

class Scene {
  val gLayers: Layers[Sprite] = new Layers(3)
  //val uiLayers: Layers[UiElement] = new Layers(3)
  //val cLayers: Layers[CollisionObject] = new Layers(3)

  def init(): Unit = {

  }

  def render(g: GdxGraphics): Unit = {
    // renders the world elements first
    for (layer <- gLayers.get()){
      layer.elements.foreach(sprite => g.drawTransformedPicture(
        sprite.pos.x,
        sprite.pos.y,
        sprite.angle,
        sprite.scale,
        sprite.current())
      )
    }

    // then renders the UI elements
    /*for (layer <- uiLayers.get()){
      layer.elements.foreach(uiElement => return) // TODO: handle ui elements
    }*/
  }

  def updatePhysics(deltaT: Float): Unit = {
    //updateMovement
    Collider2D.update()
    // TODO: gérer la physique (déplacement + collisions)
  }

}
