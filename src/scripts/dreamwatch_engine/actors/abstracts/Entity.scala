package scripts.dreamwatch_engine.actors.abstracts

/**
 * Base class for all entities in the game world.
 *
 * @param lifeTime Optional remaining lifetime in seconds. If defined, the entity will be destroyed
 *                 once this value reaches zero or below during update.
 */
abstract class Entity(var lifeTime: Option[Float] = None) {

  protected var timeFromCreation: Float = 0
  /**
   * Remove this entity from the current scene via the ScenesManager.
   * Called when the entity should no longer exist (e.g., lifetime expired or explicit destroy).
   */
  def destroy(): Unit

  /**
   * Add (spawn) this entity into the current scene via the ScenesManager.
   * Returns `this` to allow method chaining if desired.
   */
  def instantiate(): Entity

  /**
   * Update the entity each frame. Decrements `lifeTime` if defined.
   * If `lifeTime` reaches zero or below, the entity is destroyed immediately.
   * @param deltaT Time elapsed since last frame (in seconds).
   */
  def update(deltaT: Float): Unit = {
    timeFromCreation += deltaT
    // Only proceed if a lifetime is specified
    if (lifeTime.isDefined) {
      // Decrement the remaining lifetime
      lifeTime = Some(lifeTime.get - deltaT)
      // Print the new lifetime for debugging purposes


      // If lifetime has expired, destroy this entity and stop further execution
      if (lifeTime.get <= 0) {
        destroy()
        return
      }
    }
  }

}
