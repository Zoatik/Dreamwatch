package scripts.World.Actors.TopLevel.Abstract

import com.badlogic.gdx.math.Vector2

trait Controller {

  def handleMouseInput(pos: Vector2, button: Int): Unit

  def handleKeyInput(button: Int): Unit
}
