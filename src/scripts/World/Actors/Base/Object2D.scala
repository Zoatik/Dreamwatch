package scripts.World.Actors.Base

import com.badlogic.gdx.math.Vector2
import scripts.Sprite
import scripts.World.graphics.Graphics2D

class Object2D(protected var _pos: Vector2,
               override val sprite: Sprite,
               override val graphicLayerZ: Int)
  extends Entity with Graphics2D {
  def pos: Vector2 = _pos
  def pos_=(newPos: Vector2): Unit = {
    _pos = newPos
    sprite.pos = newPos
  }

  override def destroy(): Unit = {}


}



