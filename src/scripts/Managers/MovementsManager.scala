package scripts.Managers

/**
 * Manager responsible for updating all Movement2D-enabled objects each frame.
 * Implements Manager[MovementContext] to receive a MovementContext containing
 * all movable objects that need updating.
 */
object MovementsManager extends Manager[MovementContext] {

  /**
   * Initialization logic for the MovementsManager. Called once at application start.
   */
  override def init(): Unit = {
    // Print to console to confirm that this manager is active.
    println("MovementManager ready")
  }

  /**
   * Update method called each frame. Iterates through the buffer of Movement2D objects
   * and invokes their move(deltaT) method.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   * @param ctx    MovementContext containing all Movement2D instances to update.
   */
  override def update(deltaT: Float, ctx: MovementContext): Unit = {
    // Convert the mutable buffer to an array to avoid concurrent modification issues
    // while iterating, then call move(deltaT) on each Movement2D.
    ctx.movableObjects.toArray.foreach { mover =>
      mover.move(deltaT)
    }
  }
}
