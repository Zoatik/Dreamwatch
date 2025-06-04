package scripts.Managers

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.GUI.UiElement
import scripts.Globals
import scripts.World.Actors.Base.Entity
import scripts.World.Actors.TopLevel.Scene2D

import scala.collection.mutable.ArrayBuffer

/**
 * == ScenesManager ==
 *
 * Central manager for handling multiple game scenes.
 * Responsible for initializing subsystems (collisions, movements, rendering)
 * and directing per-frame update calls to the active scene.
 */
object ScenesManager extends Manager[GdxGraphics] {

  /**
   * All scenes stored in an ArrayBuffer, with a fixed size defined by Globals.
   * Each index corresponds to a distinct scene slot.
   */
  val scenes: ArrayBuffer[Scene2D] = ArrayBuffer.fill(Globals.NB_OF_SCENES)(new Scene2D)

  /**
   * Reference to the currently active scene. Defaults to the first scene in `scenes`.
   */
  var currentScene: Scene2D = scenes(0)

  /**
   * Initialize all relevant managers before the game loop starts.
   * Called once at application startup.
   */
  override def init(): Unit = {
    // Initialize collision detection subsystem
    CollisionsManager.init()
    // Initialize movement (physics) subsystem
    MovementsManager.init()
    // Initialize rendering subsystem
    RenderingManager.init()
  }

  /**
   * Update method called each frame, passing the delta time and graphics context.
   * Delegates to the active scene to update entities, check collisions, move objects, and render.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   * @param g      GdxGraphics instance used for rendering the scene.
   */
  override def update(deltaT: Float, g: GdxGraphics): Unit = {
    // 1. Update all entities in the current scene (e.g., lifetime, AI)
    currentScene.updateEntities(deltaT)
    // 2. Handle collision checks among entities in the scene
    currentScene.updateCollisions(deltaT)
    // 3. Update movement/physics for entities in the scene
    currentScene.updateMovement(deltaT)
    // 4. Render all graphics in the scene using the provided GdxGraphics
    currentScene.updateGraphics(deltaT, g)

    currentScene.updateUi(deltaT)


  }

  /**
   * Add an Entity to the currently active scene.
   * Safe-guards against null `currentScene`.
   *
   * @param entity The Entity to add to the active scene.
   */
  def addToCurrentScene(entity: Entity): Unit = {
    if (currentScene != null)
      currentScene.add(entity)

  }

  def addToCurrentScene(uiElement: UiElement): Unit = {
    if (currentScene != null)
      currentScene.add(uiElement)
  }

  /**
   * Add an Entity to a specific scene by index, if the index is valid.
   *
   * @param sceneIdx Index of the target scene in `scenes`.
   * @param entity   The Entity to add to that scene.
   */
  def addToScene(sceneIdx: Int, entity: Entity): Unit = {
    if (0 <= sceneIdx && sceneIdx < scenes.length && scenes(sceneIdx) != null)
      scenes(sceneIdx).add(entity)
  }

  def addToScene(sceneIdx: Int, uiElement: UiElement): Unit = {
    if (0 <= sceneIdx && sceneIdx < scenes.length && scenes(sceneIdx) != null)
      scenes(sceneIdx).add(uiElement)
  }

  /**
   * Remove an Entity from the currently active scene.
   * Safe-guards against null `currentScene`.
   *
   * @param entity The Entity to remove from the active scene.
   */
  def removeFromCurrentScene(entity: Entity): Unit = {
    if (currentScene != null)
      currentScene.remove(entity)
  }

  def removeFromCurrentScene(uiElement: UiElement): Unit = {
    if (currentScene != null)
      currentScene.remove(uiElement)
  }

  /**
   * Remove an Entity from a specific scene by index, if the index is valid.
   *
   * @param sceneIdx Index of the target scene in `scenes`.
   * @param entity   The Entity to remove from that scene.
   */
  def removeFromScene(sceneIdx: Int, entity: Entity): Unit = {
    if (0 <= sceneIdx && sceneIdx < scenes.length && scenes(sceneIdx) != null) {
      scenes(sceneIdx).remove(entity)
    }
  }

  def removeFromScene(sceneIdx: Int, uiElement: UiElement): Unit = {
    if (0 <= sceneIdx && sceneIdx < scenes.length && scenes(sceneIdx) != null) {
      scenes(sceneIdx).remove(uiElement)
    }
  }

}