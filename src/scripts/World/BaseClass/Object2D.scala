package scripts.World.BaseClass

import com.badlogic.gdx.math.Vector2
import scripts.Sprite

class Object2D(protected var _pos: Vector2, override val sprite: Sprite)
  extends Entity with Graphics2D {
  def pos: Vector2 = _pos
  def pos_=(newPos: Vector2): Unit = {
    _pos = newPos
    sprite.pos = newPos
  }

  override def destroy(): Unit = {}

}



