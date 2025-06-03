package scripts.Managers

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.Vector2
import scripts.World.Actors.TopLevel.{Bullet, Nightmare}
import scripts.Globals

import scala.util.Random


object GameManager extends Manager[GdxGraphics]{

  val toyPos: Vector2 = new Vector2(Globals.WINDOW_WIDTH/2, 0)

  // temporaire juste pour test
  private val spawnRate = 0.5f
  private val rnd = new Random()

  override def init(): Unit = {
    println("CollisionsManager ready")
    ScenesManager.init()

    InputManager.onMousePressed((pos, button) => {
      button match {
        case 0 => new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Small).spawn()
        case 1 => new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Big).spawn()
        case 2 => new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Laser).spawn()
        case _ =>
      }
    })






  }

  override def update(deltaT: Float, g: GdxGraphics): Unit = {
    /** GAME LOGIC HERE */
    ScenesManager.update(deltaT, g)

    if (rnd.nextFloat() < deltaT * spawnRate) {
      val startX = rnd.nextFloat() * g.getScreenWidth
      val startY = g.getScreenHeight
      val targetX = rnd.nextFloat() * g.getScreenWidth
      val targetY = 0
      new Nightmare(new Vector2(startX, startY), new Vector2(targetX, targetY), Nightmare.Small).spawn()
    }
  }

}

