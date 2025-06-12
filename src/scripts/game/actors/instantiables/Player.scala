package scripts.game.actors.instantiables

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Entity
import scripts.dreamwatch_engine.inputs.Controller
import scripts.game.GameManager
import scripts.game.actors.abstracts.Weapon

class Player extends Entity with Controller{
  var weapon: Option[Weapon] = None
  private var dreamGems: Int = 0

  override def handleMouseInput(pos: Vector2, button: Int): Boolean = {
    GameManager.currentScene match {
      case scene: GameScene =>
        button match {
          case 0 => weapon.get.shoot(pos, Weapon.Primary); true
          case 1 => weapon.get.shoot(pos, Weapon.Secondary); true
          case _ => false
        }
      case scene: MainMenuScene => false
      case _ => return false
    }
  }

  override def handleKeyInput(button: Int): Boolean = {
    false
  }

  override def destroy(): Unit = {}

  override def instantiate(): Player = {
    this
  }
}
