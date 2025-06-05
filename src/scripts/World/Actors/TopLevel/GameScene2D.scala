package scripts.World.Actors.TopLevel

import com.badlogic.gdx.math.Vector2
import scripts.Managers.GameManager
import scripts.Managers.GameManager.toyPos
import scripts.World.Actors.Base.{Entity, Scene2D}

import scala.util.Random

class GameScene2D extends Scene2D{
  private val rnd = new Random()
  private val spawnRate = 0.5f

  override def instantiate(): Entity = {
    super.instantiate()

  }

  override def update(deltaT: Float): Unit = {
    super.update(deltaT)

    if (rnd.nextFloat() < deltaT * spawnRate) {
      val startX = rnd.nextFloat() * GameManager.g.getScreenWidth
      val startY = GameManager.g.getScreenHeight
      val targetX = rnd.nextFloat() * GameManager.g.getScreenWidth
      val targetY = 0f
      new Nightmare(new Vector2(startX, startY), new Vector2(targetX, targetY), Nightmare.Small).instantiate()
    }
  }

  override def handleMouseInput(pos: Vector2, button: Int): Unit = {
    if(true){
      button match {
        case 0 =>
          // Left click: small bullet fired from toyPos toward mouse position.
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Small).instantiate()
        case 1 =>
          // Right click: big bullet fired from toyPos toward mouse position.
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Big).instantiate()
        case 2 =>
          // Middle click: laser bullet fired from toyPos toward mouse position.
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Laser).instantiate()
        case _ =>
        // Other buttons: no action.
      }
    }
  }

  override def handleKeyInput(button: Int): Unit = ???


}
