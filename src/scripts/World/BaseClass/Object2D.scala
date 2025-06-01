package scripts.World.BaseClass

import com.badlogic.gdx.math.Vector2
import scripts.Sprite

abstract class Object2D(protected var _pos: Vector2, val sprite: Sprite) {
  def pos: Vector2 = _pos
  def pos_=(newPos: Vector2): Unit = {
    _pos = newPos
    sprite.pos = newPos
  }
}

trait drawable {
  var sprite: Sprite
}
