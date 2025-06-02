package scripts.Managers


object RenderingManager extends Manager[RenderingContext] {

  override def init(): Unit = {
    println("RenderingManager ready")
  }

  override def update(deltaT: Float, ctx: RenderingContext): Unit = {
    ctx.gLayer.elements.foreach(_.draw(ctx.g))
  }

}

