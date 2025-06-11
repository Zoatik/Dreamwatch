
package scripts.game.actors.instantiables

import com.badlogic.gdx.math.Vector2
import scripts.game.actors.abstracts.{Player, Weapon}

class GamePlayer(var weapon: Weapon) extends Player {

  private var dreamShards: Int = 0

  override def handleMouseInput(pos: Vector2, button: Int): Boolean = {
    println("handled player")
    button match {
      case 0 => weapon.shoot(pos, Weapon.Primary); true
      case 1 => weapon.shoot(pos, Weapon.Secondary);  true
      case _ => false
    }
  }

  override def handleKeyInput(button: Int): Boolean = ???

  override def destroy(): Unit = ???

  override def instantiate(): GamePlayer = {
    weapon.instantiate()
    this
  }
}
