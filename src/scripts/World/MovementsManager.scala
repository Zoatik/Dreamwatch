package scripts.World


object MovementsManager extends Manager[MovementContext] {

  override def init(): Unit = {
    println("MovementManager ready")
  }

  override def update(deltaT: Float, ctx: MovementContext): Unit = {
    /** Movements Here */
    ctx.movableObjects.foreach(_.move(deltaT))

  }
}
