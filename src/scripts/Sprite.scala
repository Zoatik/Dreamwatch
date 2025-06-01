package scripts

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ArrayBuffer

case class Sprite(var pos: Vector2, var angle: Float, var scale: Float, images: ArrayBuffer[BitmapImage]) {
  require(images.nonEmpty)
  var currImageIdx = 0

  def next(): BitmapImage = {
    currImageIdx += 1
    current()
  }

  def current(): BitmapImage = images(currImageIdx)
}
