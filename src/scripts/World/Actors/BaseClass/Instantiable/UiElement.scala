package scripts.World.Actors.BaseClass.Instantiable

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.World.Physics.Area2D

import scala.collection.mutable.ArrayBuffer

class UiElement(pos: Vector2, images: ArrayBuffer[BitmapImage], gLayerZ: Int, area2DType: Area2D.Type)
  extends Sprite2D(pos, images, gLayerZ, area2DType) {

}
