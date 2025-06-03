package scripts.World.Actors.TopLevel

import com.badlogic.gdx.math.Vector2
import scripts.Sprite
import scripts.World.Actors.Base.{Entity, Object2D}
import scripts.World.Physics.Movement2D

class Toy(pos: Vector2, sprite: Sprite, graphicLayerZ: Int)
  extends Object2D(pos, sprite, graphicLayerZ) with Movement2D {

  override var speed: Float = 0
  override var target: Vector2 = null
}
