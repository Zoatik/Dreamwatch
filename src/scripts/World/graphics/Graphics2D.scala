package scripts.World.graphics

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.Vector2

/**
 * Trait that adds rendering capability to an Object2D.
 * Expects the implementing class to provide a Sprite and a render layer index.
 */
trait Graphics2D {

  var pos: Vector2
  var angle: Float
  protected var _scale: Float
  protected var _width: Float
  protected var _height: Float
  var image: BitmapImage




  /**
   * Draws the sprite on screen using the provided GdxGraphics context.
   * Positions, rotates, and scales the image based on the sprite's properties.
   *
   * @param g GdxGraphics instance used to render textures.
   */
  def draw(g: GdxGraphics): Unit = {
    // Use drawTransformedPicture to render with rotation and scale:
    if (image != null) {
      g.drawTransformedPicture(
        pos.x,
        pos.y,
        angle,
        _scale,
        image
      )
    }
  }
}

