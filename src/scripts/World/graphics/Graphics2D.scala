package scripts.World.graphics

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.Sprite
import scripts.World.Actors.Base.Object2D

/**
 * Trait that adds rendering capability to an Object2D.
 * Expects the implementing class to provide a Sprite and a render layer index.
 */
trait Graphics2D { self: Object2D =>
  /**
   * Sprite used for rendering this object (contains images, position, scale, and rotation).
   */
  val sprite: Sprite

  /**
   * Z-index indicating the rendering layer for this sprite. Higher values draw on top.
   */
  val graphicLayerZ: Int

  /**
   * Draws the sprite on screen using the provided GdxGraphics context.
   * Positions, rotates, and scales the image based on the sprite's properties.
   *
   * @param g GdxGraphics instance used to render textures.
   */
  def draw(g: GdxGraphics): Unit = {
    // Use drawTransformedPicture to render with rotation and scale:
    g.drawTransformedPicture(
      pos.x,
      pos.y,
      sprite.angle,
      sprite.scale,
      sprite.current()
    )
  }
}

