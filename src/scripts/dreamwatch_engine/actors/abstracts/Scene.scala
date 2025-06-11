package scripts.dreamwatch_engine.actors.abstracts

import scripts.dreamwatch_engine.actors.instantiables.{Particle2D, Sprite2D, UiElement}
import scripts.dreamwatch_engine.inputs.Controller
import scripts.dreamwatch_engine.physics.{Collider2D, Movement2D}
import scripts.dreamwatch_engine.utils.Layers
import scripts.game.GameManager
import scripts.game.actors.abstracts.Player
import scripts.utils.Globals

import scala.collection.immutable.ListSet
import scala.collection.mutable.ArrayBuffer

/**
 * Represents a top-level 2D scene. Manages all entities in the scene,
 * including their rendering, collision detection, and movement updates.
 */
abstract class Scene extends Entity with Controller {

  val player: Player
  /**
   * Layers for rendering: a collection of Graphics2D instances grouped by Z-index.
   */
  val gLayers: Layers[Sprite2D]

  val uiLayers: Layers[UiElement]

  /**
   * Layers for collision detection: a collection of Collider2D instances grouped by layer index.
   */
  val cLayers: Layers[Collider2D]

  /**
   * Buffer of all Movement2D-enabled objects; these will be updated each frame.
   */
  val movableObjects: ArrayBuffer[Movement2D]

  /**
   * Buffer of all objects present in the scene, regardless of their capabilities.
   */
  val objects: ArrayBuffer[Object2D]

  val particles: ArrayBuffer[Particle2D]

  /**
   * Scale factor applied to movement deltas (e.g., for slow-motion or speed-up effects).
   */
  var deltaScale: Float = 1.0f

  private var _isMouseOnUi: Boolean = false

  def isMouseOnUi: Boolean = _isMouseOnUi


  /**
   * Add a generic Entity to the scene. Depending on which traits the entity implements,
   * it will be registered for rendering, collisions, and/or movement.
   *
   * @param object2D The Object2D to add.
   */
  def add(object2D: Object2D): Unit = {
    // Track entity in the master list
    objects += object2D
    //println(s"element : $object2D added to scene")

    // If the entity supports rendering (Sprite2D), add it to the appropriate render layer
    object2D match {
      case u: UiElement =>
        val z: Int = u.gLayerZ
        if (z >= 0 && z < uiLayers.depth)
          uiLayers.add(z, u)
      case s: Sprite2D =>
        val z: Int = s.gLayerZ
        if (z >= 0 && z < gLayers.depth)
          gLayers.add(z, s)
      case _ =>
    }

    // If the entity supports collisions (Collider2D), add it to the appropriate collision layer
    object2D match {
      case c: Collider2D =>
        val z: Int = c.cLayerZ
        if (z >= 0 && z < cLayers.depth)
          cLayers.add(z, c)
      case _ =>
    }

    // If the entity supports movement (Movement2D), add it to the movableObjects buffer
    object2D match {
      case m: Movement2D =>
        movableObjects += m
      case _ =>
    }

    object2D match {
      case p: Particle2D =>
        particles += p
      case _ =>
    }

  }

  /**
   * Remove an Entity from the scene. Unregisters it from rendering, collisions, and movement
   * as applicable. Also disposes of any sprite images to free resources.
   *
   * @param object2D The Object2D to remove.
   */
  def remove(object2D: Object2D): Unit = {
    // Remove from master list
    objects -= object2D
    //println(s"element : $object2D removed from scene")
    // If the entity supports rendering, remove from all render layers and dispose its images
    object2D match {
      case u: UiElement =>
        uiLayers.remove(u)
      case g: Sprite2D =>
        gLayers.remove(g)
      case _ =>
    }

    // If the entity supports collisions, remove from all collision layers
    object2D match {
      case c: Collider2D =>
        cLayers.remove(c)
      case _ =>
    }

    // If the entity supports movement, remove it from the movableObjects buffer
    object2D match {
      case m: Movement2D =>
        movableObjects -= m
      case _ =>
    }

    object2D match {
      case p: Particle2D =>
        particles -= p
      case _ =>
    }
  }


  /**
   * Perform collision detection across every collision layer.
   * Called each frame by the ScenesManager.
   *
   * @param deltaT Time elapsed since last frame (in seconds). Provided to Managers for consistency,
   *               though collision logic here does not directly use deltaT.
   */
  private def updateCollisions(deltaT: Float): Unit = {
    // Iterate through each existing collision layer

    for (layer <- cLayers.get()) {
      //Collider2D.checkAndNotifyCollisions(layer)
      for (collider <- layer.elements.toArray){
        Collider2D.checkAndNotifyCollisions(collider, cLayers)
      }
    }
  }

  /**
   * Update movement for all Movement2D-enabled objects in this scene.
   * Called each frame by the ScenesManager.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   */
  private def updateMovement(deltaT: Float): Unit = {
    movableObjects.toArray.foreach(m => {
      m.move(deltaT)
      m.faceTarget()
    })
  }

  /**
   * Render all Graphics2D-enabled objects in this scene.
   * Called each frame by the ScenesManager, after movement and collision updates.
   *
   * @param deltaT Time elapsed since last frame (in seconds). Included for consistency,
   *               though drawing logic uses only the graphics context.
   * @param g      GdxGraphics instance used to draw on-screen.
   */
  private def updateGraphics(deltaT: Float): Unit = {

    GameManager.g.clear()



    // renders graphical elements (except UI)
    for (layer <- gLayers.get()) {
      layer.elements.foreach(gElement => gElement.draw(GameManager.g))
    }
    //println("size: " + gLayers.get(Globals.BULLET_G_LAYER).get.size)

    /*// renders particles
    for (particle <- particles){
      particle.render()
    }*/

    // renders UI
    for (layer <- uiLayers.get()){
      layer.elements.foreach(gElement => gElement.draw(GameManager.g))
    }
  }

  /**
   * Update generic Entity logic each frame, such as lifetime decrements or AI behaviors.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   */
  private def updateLogic(deltaT: Float): Unit = {
    // Convert to array to avoid concurrent modification if entities are added/removed during update
    objects.toArray.foreach(_.update(deltaT))
  }



  override def update(deltaT: Float): Unit = {
    super.update(deltaT)

    checkMouse()

    if (!GameManager.isPaused) {
      updateMovement(deltaT)
      updateCollisions(deltaT)
      updateLogic(deltaT)
    }

    updateGraphics(deltaT)
  }

  private def checkMouse(): Unit = {
    for (layer <- uiLayers.get()){
      for (el <- layer.elements){
        if (el.containsPoint(GameManager.mousePos)){
          _isMouseOnUi = true
          return
        }
      }
    }
    _isMouseOnUi = false
  }

  /**
   * Remove this entity from the current scene via the ScenesManager.
   * Called when the entity should no longer exist (e.g., lifetime expired or explicit destroy).
   */
  override def destroy(): Unit = {}

  /**
   * Add (spawn) this entity into the current scene via the ScenesManager.
   * Returns `this` to allow method chaining if desired.
   */
  override def instantiate(): Scene = this

}

