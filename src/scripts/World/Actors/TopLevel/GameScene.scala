package scripts.World.Actors.TopLevel

import com.badlogic.gdx.math.Vector2
import scripts.Managers.GameManager
import scripts.Managers.GameManager.toyPos
import scripts.World.Actors.Base.{Entity, Scene}

import scala.util.Random

class GameScene extends Scene{
  private val rnd = new Random()
  private val spawnRate = 0.5f

  override def instantiate(): Entity = {
    super.instantiate()

  }

  override def update(deltaT: Float): Unit = {
    super.update(deltaT)

    if (!GameManager.isPaused && rnd.nextFloat() < deltaT * spawnRate) {
      val startX = rnd.nextFloat() * GameManager.g.getScreenWidth
      val startY = GameManager.g.getScreenHeight
      val targetX = rnd.nextFloat() * GameManager.g.getScreenWidth
      val targetY = 0f
      new Nightmare(new Vector2(startX, startY), new Vector2(targetX, targetY), Nightmare.Small).instantiate()
    }
  }

  override def handleMouseInput(pos: Vector2, button: Int): Unit = {
    if(!isMouseOnUi){
      button match {
        case 0 =>
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Small).instantiate()
        case 1 =>
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Big).instantiate()
        case 2 =>
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Laser).instantiate()
        case _ =>
      }
    }
  }

  override def handleKeyInput(button: Int): Unit = {

  }


}
