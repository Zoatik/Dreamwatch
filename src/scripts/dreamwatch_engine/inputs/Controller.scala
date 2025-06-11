package scripts.dreamwatch_engine.inputs

import com.badlogic.gdx.math.Vector2

trait Controller {

  def handleMouseInput(pos: Vector2, button: Int): Boolean

  def handleKeyInput(button: Int): Boolean
}
