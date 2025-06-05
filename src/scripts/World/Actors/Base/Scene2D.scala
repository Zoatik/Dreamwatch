package scripts.World.Actors.Base

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.GUI.UiElement
import scripts.Managers._
import scripts.World.Physics.{Collider2D, Movement2D}
import scripts.World.graphics.Graphics2D
import scripts.{Globals, Layer, Layers}

import scala.collection.mutable.ArrayBuffer

/**
 * Represents a top-level 2D scene. Manages all entities in the scene,
 * including their rendering, collision detection, and movement updates.
 */
abstract class Scene2D extends Entity {

  /**
   * Layers for rendering: a collection of Graphics2D instances grouped by Z-index.
   */
  private val gLayers: Layers[Graphics2D] = new Layers(Globals.G_LAYERS_SIZE)

  private val uiLayers: Layers[UiElement] = new Layers(Globals.UI_LAYERS_SIZE)

  /**
   * Layers for collision detection: a collection of Collider2D instances grouped by layer index.
   */
  private val cLayers: Layers[Collider2D] = new Layers(Globals.C_LAYERS_SIZE)

  /**
   * Buffer of all Movement2D-enabled objects; these will be updated each frame.
   */
  private val movableObjects: ArrayBuffer[Movement2D] = ArrayBuffer()

  /**
   * Buffer of all objects present in the scene, regardless of their capabilities.
   */
  private val objects: ArrayBuffer[Entity] = ArrayBuffer()

  /**
   * Scale factor applied to movement deltas (e.g., for slow-motion or speed-up effects).
   */
  var deltaScale: Float = 1.0f

  /**
   * Add a generic Entity to the scene. Depending on which traits the entity implements,
   * it will be registered for rendering, collisions, and/or movement.
   *
   * @param entity The Entity to add.
   */
  def add(object2D: Object2D): Unit = {
    // Track entity in the master list
    objects += object2D

    // If the entity supports rendering (Graphics2D), add it to the appropriate render layer
    object2D match {
      case g: Graphics2D =>
        val z: Int = g.graphicLayerZ
        if (z >= 0 && z < Globals.G_LAYERS_SIZE)
          gLayers.add(z, g)
      case _ =>
    }

    // If the entity supports collisions (Collider2D), add it to the appropriate collision layer
    object2D match {
      case c: Collider2D =>
        val z: Int = c.collisionLayerZ
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

  def add(uiElement: UiElement): Unit = {
    val z: Int = uiElement.graphicLayerZ
    if (z >= 0 && z < Globals.UI_LAYERS_SIZE)
      uiLayers.add(z, uiElement)
  }

  /**
   * Remove an Entity from the scene. Unregisters it from rendering, collisions, and movement
   * as applicable. Also disposes of any sprite images to free resources.
   *
   * @param entity The Entity to remove.
   */
  def remove(object2D: Object2D): Unit = {
    // Remove from master list
    objects -= object2D

    // If the entity supports rendering, remove from all render layers and dispose its images
    object2D match {
      case g: Graphics2D =>
        gLayers.remove(g)
        // Manually dispose bitmap resources to prevent memory leaks or crashes
        g.sprite.images.foreach(_.dispose())
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

  def remove(uiElement: UiElement): Unit = {
    uiLayers.remove(uiElement)
  }

  /**
   * Accessor for collision layers. External code (e.g., Managers) may need to retrieve this.
   *
   * @return The Layers[Collider2D] container used by this scene.
   */
  def getCollisionLayers: Layers[Collider2D] = cLayers

  /**
   * Accessor for render layers. External code (e.g., Managers) may need to retrieve this.
   *
   * @return The Layers[Graphics2D] container used by this scene.
   */
  def getGraphicLayers: Layers[Graphics2D] = gLayers

  /**
   * Accessor for all movable objects to pass to the movement manager each frame.
   *
   * @return Buffer of Movement2D instances.
   */
  def getMovableObjects: ArrayBuffer[Movement2D] = movableObjects

  /**
   * Accessor for all entities in this scene, for tasks like lifetime updates, AI, etc.
   *
   * @return Buffer of Entity instances.
   */
  def getObjects: ArrayBuffer[Entity] = objects

  /**
   * Perform collision detection across every collision layer.
   * Called each frame by the ScenesManager.
   *
   * @param deltaT Time elapsed since last frame (in seconds). Provided to Managers for consistency,
   *               though collision logic here does not directly use deltaT.
   */
  def updateCollisions(deltaT: Float): Unit = {
    // Iterate through each existing collision layer
    for (layer <- cLayers.get())
      // Delegate collision checks to CollisionsManager, passing the layer as context
      CollisionsManager.update(deltaT, CollisionContext(layer))
  }

  /**
   * Update movement for all Movement2D-enabled objects in this scene.
   * Called each frame by the ScenesManager.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   */
  def updateMovement(deltaT: Float): Unit = {
    // Delegate movement updates to the MovementsManager, providing the movableObjects buffer
    MovementsManager.update(deltaT, MovementContext(movableObjects, deltaScale))
  }

  /**
   * Render all Graphics2D-enabled objects in this scene.
   * Called each frame by the ScenesManager, after movement and collision updates.
   *
   * @param deltaT Time elapsed since last frame (in seconds). Included for consistency,
   *               though drawing logic uses only the graphics context.
   * @param g      GdxGraphics instance used to draw on-screen.
   */
  def updateGraphics(deltaT: Float, g: GdxGraphics): Unit = {
    // Iterate through each existing render layer
    for (layer <- gLayers.get()) {
      // Delegate drawing to the RenderingManager, passing the layer and graphics context
      RenderingManager.update(deltaT, RenderingContext(layer, g))
    }

    // Placeholder for future UI rendering logic
    for (layer <- uiLayers.get()){
      RenderingManager.update(deltaT, RenderingContext(layer.asInstanceOf[Layer[Graphics2D]], g))
    }
  }

  def updateUi(deltaT: Float): Unit = {
    for (layer <- uiLayers.get()){
      UiManager.update(deltaT, UiContext(layer))
    }
  }

  /**
   * Update generic Entity logic each frame, such as lifetime decrements or AI behaviors.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   */
  def updateObjects(deltaT: Float): Unit = {
    // Convert to array to avoid concurrent modification if entities are added/removed during update
    objects.toArray.foreach(_.update(deltaT))
  }

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
