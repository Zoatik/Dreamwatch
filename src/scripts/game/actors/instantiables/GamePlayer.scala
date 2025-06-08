
package scripts.game.actors.instantiables

import com.badlogic.gdx.math.Vector2
import scripts.game.actors.abstracts.Player

class GamePlayer extends Player {

  override def handleMouseInput(pos: Vector2, button: Int): Unit = {

  }

  override def handleKeyInput(button: Int): Unit = ???

  override def destroy(): Unit = ???

  override def instantiate(): GamePlayer = ???
}
