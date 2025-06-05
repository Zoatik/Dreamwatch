package scripts.World.Actors.Base

import com.badlogic.gdx.math.Vector2
import scripts.GUI.UiElement
import scripts.Managers._
import scripts.World.Physics.{Collider2D, Movement2D}
import scripts.{Globals, Layers}

import scala.collection.mutable.ArrayBuffer

/**
 * Represents a top-level 2D scene. Manages all entities in the scene,
 * including their rendering, collision detection, and movement updates.
 */
abstract class Scene extends Entity {

  /**
   * Layers for rendering: a collection of Graphics2D instances grouped by Z-index.
   */
  val gLayers: Layers[Sprite2D] = new Layers(Globals.G_LAYERS_SIZE)

  val uiLayers: Layers[UiElement] = new Layers(Globals.UI_LAYERS_SIZE)

  /**
   * Layers for collision detection: a collection of Collider2D instances grouped by layer index.
   */
  val cLayers: Layers[Collider2D] = new Layers(Globals.C_LAYERS_SIZE)

  /**
   * Buffer of all Movement2D-enabled objects; these will be updated each frame.
   */
  val movableObjects: ArrayBuffer[Movement2D] = ArrayBuffer()

  /**
   * Buffer of all objects present in the scene, regardless of their capabilities.
   */
  val objects: ArrayBuffer[Object2D] = ArrayBuffer()

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
        if (z >= 0 && z < Globals.G_LAYERS_SIZE)
          uiLayers.add(z, u)
      case s: Sprite2D =>
        val z: Int = s.gLayerZ
        if (z >= 0 && z < Globals.G_LAYERS_SIZE)
          gLayers.add(z, s)
      case _ =>
    }

    // If the entity supports collisions (Collider2D), add it to the appropriate collision layer
    object2D match {
      case c: Collider2D =>
        val z: Int = c.cLayerZ
        if (z >= 0 && z < Globals.C_LAYERS_SIZE)
          cLayers.add(z, c)
      case _ =>
    }

    // If the entity supports movement (Movement2D), add it to the movableObjects buffer
    object2D match {
      case m: Movement2D =>
        movableObjects += m
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
      case g: Sprite2D =>
        gLayers.remove(g)
        // Manually dispose bitmap resources to prevent memory leaks or crashes
        g.images.foreach(_.dispose())
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
      Collider2D.checkAndNotifyCollisions(layer)
    }
  }

  /**
   * Update movement for all Movement2D-enabled objects in this scene.
   * Called each frame by the ScenesManager.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   */
  private def updateMovement(deltaT: Float): Unit = {
    movableObjects.toArray.foreach(_.move(deltaT))
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
    // Iterate through each existing render layer
    for (layer <- gLayers.get()) {
      // Delegate drawing to the RenderingManager, passing the layer and graphics context
      layer.elements.foreach(gElement => gElement.draw(GameManager.g))
      //RenderingManager.update(deltaT, RenderingContext(layer, g))
    }

    // Placeholder for future UI rendering logic
    for (layer <- uiLayers.get()){
      //RenderingManager.update(deltaT, RenderingContext(layer.asInstanceOf[Layer[Graphics2D]], g))
      layer.elements.foreach(gElement => gElement.draw(GameManager.g))
    }
  }

  /**
   * Update generic Entity logic each frame, such as lifetime decrements or AI behaviors.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   */
  private def updateObjectsLogic(deltaT: Float): Unit = {
    // Convert to array to avoid concurrent modification if entities are added/removed during update
    objects.toArray.foreach(_.update(deltaT))
  }

  private def updateUiLogic(deltaT: Float): Unit = {
    for (layer <- uiLayers.get()){
      layer.elements.toArray.foreach(_.update(deltaT))
    }
  }

  override def update(deltaT: Float): Unit = {
    super.update(deltaT)
    updateGraphics(deltaT)
    checkMouse()
    if (!GameManager.isPaused) {
      updateMovement(deltaT)
      updateCollisions(deltaT)
      updateObjectsLogic(deltaT)
      updateUiLogic(deltaT)
    }
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

  def handleMouseInput(pos: Vector2, button: Int): Unit

  def handleKeyInput(button: Int): Unit

  /**
   * Remove this entity from the current scene via the ScenesManager.
   * Called when the entity should no longer exist (e.g., lifetime expired or explicit destroy).
   */
  override def destroy(): Unit = ???

  /**
   * Add (spawn) this entity into the current scene via the ScenesManager.
   * Returns `this` to allow method chaining if desired.
   */
  override def instantiate(): Entity = ???
}
