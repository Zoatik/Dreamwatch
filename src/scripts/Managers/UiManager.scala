package scripts.Managers

object UiManager extends Manager[Unit] {

  /**
   * Perform any necessary setup when the manager is first created.
   * Called once at application start.
   */
  override def init(): Unit = {}

  override def update(deltaT: Float, ctx: Unit): Unit = {
    super.update(deltaT, ctx)
  }

}
