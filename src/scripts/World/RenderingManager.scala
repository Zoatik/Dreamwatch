package scripts.World

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.World.graphics.Graphics2D


object RenderingManager extends Manager[RenderingContext] {

  override def init(): Unit = {
    println("RenderingManager ready")
  }

  override def update(deltaT: Float, ctx: RenderingContext): Unit = {
    ctx.gLayer.elements.foreach(_.draw(ctx.g))
  }

}

