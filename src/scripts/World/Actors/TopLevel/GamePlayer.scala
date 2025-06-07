
package scripts.World.Actors.TopLevel

import com.badlogic.gdx.math.Vector2
import scripts.World.Actors.BaseClass.Abstract.Entity
import scripts.World.Actors.TopLevel.Abstract.Player

class GamePlayer extends Player {

  override def handleMouseInput(pos: Vector2, button: Int): Unit = {

  }

  override def handleKeyInput(button: Int): Unit = ???

  override def destroy(): Unit = ???

  override def instantiate(): GamePlayer = ???
}
