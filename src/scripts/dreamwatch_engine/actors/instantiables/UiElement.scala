package scripts.dreamwatch_engine.actors.instantiables

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.physics.Area2D

import scala.collection.mutable.ArrayBuffer

class UiElement(pos: Vector2, angle: Float, images: ArrayBuffer[BitmapImage], gLayerZ: Int, area2DType: Area2D.Type)
  extends Sprite2D(pos, angle, images, gLayerZ, area2DType) {

  override def instantiate(): UiElement = {
    super.instantiate()
    this
  }

}
