package scripts.GUI

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.Sprite
import scripts.World.Actors.Base.{Entity, Sprite2D}
import scripts.World.Physics.Area2D
import scripts.World.graphics.Graphics2D

import scala.collection.mutable.ArrayBuffer

class UiElement(pos: Vector2, images: ArrayBuffer[BitmapImage], gLayerZ: Int, area2DType: Area2D.Type)
  extends Sprite2D(pos, images, gLayerZ, area2DType) {


}
