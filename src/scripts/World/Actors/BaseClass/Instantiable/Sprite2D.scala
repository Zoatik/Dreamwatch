package scripts.World.Actors.BaseClass.Instantiable

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.Vector2
import scripts.World.Actors.BaseClass.Abstract.Object2D
import scripts.World.Physics.Area2D
import scripts.World.graphics.Graphics2D

import scala.collection.mutable.ArrayBuffer

/**
 * Represents a sprite composed of one or more BitmapImage frames, with position, rotation, and scale.
 * Supports cycling through frames, resizing by width, and disposing resources when no longer needed.
 *
 * @param images      Collection of BitmapImage frames (must be nonEmpty).
 * @param angle       Rotation angle in degrees (default 0.0f).
 * @param spriteScale Uniform scale factor applied to all images (default 1.0f).
 * @param lifeTime    Time before destruction (default None)
 */
class Sprite2D(pos: Vector2,
               val images: ArrayBuffer[BitmapImage],
               var gLayerZ: Int,
               area2DType: Area2D.Type,
               var angle: Float = 0,
               spriteScale: Float = 1f,
               lifeTime: Option[Float] = None) extends Object2D(pos, lifeTime) with Graphics2D with Area2D{

  // Ensure at least one image frame is provided
  require(images.nonEmpty, "Sprite must contain at least one BitmapImage")

  private val baseWidth: Float = images(0).getImage.getWidth
  private val baseHeight: Float = images(0).getImage.getHeight

  var isVisible: Boolean = true


  override var image: BitmapImage = images(0)

  override protected var _scale: Float = spriteScale
  override protected var _width: Float = baseWidth * scale
  override protected var _height: Float = baseHeight * scale

  override var areaType: Area2D.Type = area2DType
  override var areaWidth: Float = if (areaType == Area2D.Box) baseWidth else baseWidth / 2
  override var areaHeight: Float = baseHeight

  def scale: Float = _scale
  def scale_=(newScale: Float): Unit = {
    _scale = newScale
    _width = baseWidth * newScale
    _height = baseHeight * newScale
  }

  def height: Float = _height

  def height_=(newHeight: Float): Unit = {
    scale = scale * (newHeight / _height)
  }

  def width: Float = _width

  def width_=(newWidth: Float): Unit = {
    scale = scale * (newWidth / _width)
  }

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
    image.dispose()
    image = images(currImageIdx)
    current()
  }

  /**
   * Retrieve the current BitmapImage frame without advancing the index.
   * @return The BitmapImage at `currImageIdx`.
   */
  def current(): BitmapImage = image


  /**
   * Dispose of all BitmapImage resources held by this sprite.
   * Should be called when the sprite is no longer needed to free memory.
   */
  override def destroy(): Unit = {
    super.destroy()
    images.foreach(_.dispose())
  }


  /**
   * Add (spawn) this entity into the current scene via the ScenesManager.
   * Returns `this` to allow method chaining if desired.
   */
  override def instantiate(): Sprite2D = {
    super.instantiate()
    this
  }


  override def draw(g: GdxGraphics): Unit = {
    if (isVisible)
      super.draw(g)
  }



}
