package scripts.World

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.World.Actors.Base.{Entity, Object2D}
import scripts.World.Physics.{Collider2D, Movement2D}
import scripts.World.graphics.Graphics2D
import scripts.{Globals, Layers}

import scala.collection.mutable.ArrayBuffer

object SceneManager extends Manager[GdxGraphics] {
  val scenes: ArrayBuffer[Scene2D] = ArrayBuffer.fill(Globals.NB_OF_SCENES)(new Scene2D)
  var currentScene: Scene2D = scenes(0)

  override def init(): Unit = {
    CollisionsManager.init()
    MovementsManager.init()
    RenderingManager.init()
  }

  override def update(deltaT: Float, g: GdxGraphics): Unit = {
    currentScene.updateCollisions(deltaT)
    currentScene.updateMovement(deltaT)
    currentScene.updateGraphics(deltaT, g)
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



