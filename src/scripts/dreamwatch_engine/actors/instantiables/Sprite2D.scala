package scripts.dreamwatch_engine.actors.instantiables

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Object2D
import scripts.dreamwatch_engine.graphics.Graphics2D
import scripts.dreamwatch_engine.physics.Area2D

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
               angle: Float,
               val imagesPaths: ArrayBuffer[String],
               var gLayerZ: Int,
               area2DType: Area2D.Type,
               var animDuration: Float = 0.0f,
               spriteScale: Float = 1f,
               lifeTime: Option[Float] = None) extends Object2D(pos, angle, lifeTime) with Graphics2D with Area2D{

  // Ensure at least one image frame is provided
  require(imagesPaths.nonEmpty, "Sprite must contain at least one BitmapImage")

  val images: ArrayBuffer[BitmapImage] = imagesPaths.map(path => new BitmapImage(path))

  private val baseWidth: Float = {
    if(area2DType == Area2D.Circle) images(0).getImage.getWidth / 2
    else images(0).getImage.getWidth
  }
  private val baseHeight: Float = {
    if(area2DType == Area2D.Circle) images(0).getImage.getHeight / 2
    else images(0).getImage.getHeight
  }

  private var currImageIdx: Int = 0
  private var dt: Float = 0

  var isVisible: Boolean = true

  def toggleVisible(): Unit = isVisible = !isVisible


  override var image: BitmapImage = images(0)

  override protected var _scale: Float = spriteScale
  override protected var _width: Float = baseWidth * scale
  override protected var _height: Float = baseHeight * scale

  override var areaType: Area2D.Type = area2DType

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
   * Advance to the next frame and return it.
   * Increments the internal index and wraps around automatically.
   * @return The BitmapImage at the new index.
   */
  def next(): BitmapImage = {
    currImageIdx = (currImageIdx + 1) % images.length
    //image.dispose()
    image = images(currImageIdx)
    image
  }

  override protected def onMouseEntered(mousePos: Vector2): Unit = {
    if(isVisible)
      super.onMouseEntered(mousePos)
  }

  override protected def onMouseLeft(mousePos: Vector2): Unit = {
    if(isVisible)
      super.onMouseLeft(mousePos)
  }

  override protected def onMousePressed(mousePos: Vector2, mouseButton: Int): Unit = {
    if(isVisible)
      super.onMousePressed(mousePos, mouseButton)
  }

  override protected def onMouseReleased(mousePos: Vector2, mouseButton: Int): Unit = {
    if(isVisible)
      super.onMouseReleased(mousePos, mouseButton)
  }


  /**
   * Retrieve the current BitmapImage frame without advancing the index.
   * @return The BitmapImage at `currImageIdx`.
   */
  def current(): BitmapImage = image


  override def update(deltaT: Float): Unit = {
    super.update(deltaT)
    if (animDuration > 0.0 && images.length > 1) {
      dt += deltaT
      if (dt > animDuration/images.length) {
        dt = 0
        next()
      }
    }
  }
  /**
   * Dispose of all BitmapImage resources held by this sprite.
   * Should be called when the sprite is no longer needed to free memory.
   */
  override def destroy(): Unit = {
    super.destroy()
    unbindEvents()
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
