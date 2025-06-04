package scripts.Managers

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.World.graphics.Graphics2D

/**
 * Manager responsible for drawing all Graphics2D-enabled objects each frame.
 * Implements Manager[RenderingContext] to receive a RenderingContext containing
 * the render layer and the GdxGraphics instance for drawing.
 */
object RenderingManager extends Manager[RenderingContext] {

  /**
   * Initialization logic for the RenderingManager. Called once at application startup.
   */
  override def init(): Unit = {
    // Print to console to confirm that this manager is active
    println("RenderingManager ready")
  }

  /**
   * Update method called each frame. Iterates over the Graphics2D objects in the provided
   * render layer and calls their draw method with the GdxGraphics context.
   *
   * @param deltaT Time elapsed since last frame (in seconds). Unused here but part of signature.
   * @param ctx    RenderingContext containing the layer of Graphics2D instances and GdxGraphics.
   */
  override def update(deltaT: Float, ctx: RenderingContext): Unit = {
    // For each Graphics2D in the layer, invoke its draw() to render on-screen
    ctx.gLayer.elements.foreach { graphic =>
      graphic.draw(ctx.g)
    }
  }
}

