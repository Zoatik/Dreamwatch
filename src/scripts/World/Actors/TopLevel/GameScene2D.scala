package scripts.World.Actors.TopLevel

import com.badlogic.gdx.math.Vector2
import scripts.Managers.GameManager.toyPos
import scripts.World.Actors.Base.{Entity, Scene2D}

class GameScene2D extends Scene2D{
  override def instantiate(): Entity = {
    super.instantiate()

  }

  override def update(deltaT: Float): Unit = {
    super.update(deltaT)

  }

  override def handleMouseInput(pos: Vector2, button: Int): Unit = {
    if(!UiManager.isMouseOverUi){
      button match {
        case 0 =>
          // Left click: small bullet fired from toyPos toward mouse position.
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Small).spawn()
        case 1 =>
          // Right click: big bullet fired from toyPos toward mouse position.
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Big).spawn()
        case 2 =>
          // Middle click: laser bullet fired from toyPos toward mouse position.
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Laser).spawn()
        case _ =>
        // Other buttons: no action.
      }
    }
  }

  override def handleKeyInput(button: Int): Unit = ???


}
