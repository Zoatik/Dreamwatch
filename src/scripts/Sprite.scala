package scripts

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ArrayBuffer

case class Sprite(images: ArrayBuffer[BitmapImage], var pos: Vector2, var angle: Float = 0.0f, var scale: Float = 1.0f) {
  require(images.nonEmpty)
  private var currImageIdx = 0

  def next(): BitmapImage = {
    currImageIdx += 1
    current()
  }

  def current(): BitmapImage = images(currImageIdx)
}
