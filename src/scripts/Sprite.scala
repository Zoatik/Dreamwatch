package scripts

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scala.collection.mutable.ArrayBuffer

/**
 * Represents a sprite composed of one or more BitmapImage frames, with position, rotation, and scale.
 * Supports cycling through frames, resizing by width, and disposing resources when no longer needed.
 *
 * @param images Collection of BitmapImage frames (must be nonEmpty).
 * @param pos    Current position of the sprite in world coordinates.
 * @param angle  Rotation angle in degrees (default 0.0f).
 * @param scale  Uniform scale factor applied to all images (default 1.0f).
 */
case class Sprite(
  images: ArrayBuffer[BitmapImage],
  var pos: Vector2,
  var angle: Float = 0.0f,
  var scale: Float = 1.0f,
) {

  // Ensure at least one image frame is provided
  require(images.nonEmpty, "Sprite must contain at least one BitmapImage")

  private var _width: Float = images(0).getImage.getWidth
  private var _height: Float = images(0).getImage.getHeight

  /**
   * Index of the current image frame in the `images` buffer.
   * Starts at 0.
   */
  private var currImageIdx: Int = 0

  /**
   * Advance to the next frame and return it.
   * Increments the internal index and wraps around automatically.
   * @return The BitmapImage at the new index.
   */
  def next(): BitmapImage = {
    currImageIdx = (currImageIdx + 1) % images.length
    current()
  }

  /**
   * Adjust the sprite's scale so that each frame has the given new width.
   * Computes the scale factor for each frame based on its original width.
   * @param newWidth Desired width in pixels.
   */
  def setWidth(newWidth: Float): Unit = {
    images.foreach { _ =>
      // Multiply the current scale by ratio of desired to original width
      scale *= (newWidth / _width)
      _width = newWidth
      _height *= scale
    }
  }

  def width: Float = _width

  def height: Float = _height

  /**
   * Dispose of all BitmapImage resources held by this sprite.
   * Should be called when the sprite is no longer needed to free memory.
   */
  def destroy(): Unit = {
    images.foreach(_.dispose())
  }

  /**
   * Retrieve the current BitmapImage frame without advancing the index.
   * @return The BitmapImage at `currImageIdx`.
   */
  def current(): BitmapImage = images(currImageIdx)
}
