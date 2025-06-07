package scripts.World.Actors.TopLevel

import com.badlogic.gdx.math.Vector2
import scripts.World.Actors.TopLevel.Abstract.{Player, Scene}

class MainMenuScene extends Scene{

  override val player: Player = new MainMenuPlayer

  override def handleMouseInput(pos: Vector2, button: Int): Unit = ???

  override def handleKeyInput(button: Int): Unit = ???
}
