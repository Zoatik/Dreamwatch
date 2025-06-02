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
    SceneManager.init()

    InputManager.onMousePressed((pos, button) => {
      if (button == 0) {
        val newBullet = Bullet.spawn(new Vector2(toyPos), pos, Bullet.Small)
        newBullet.onCollision {
          case nightmare: Nightmare =>
            nightmare.destroy()
            newBullet.destroy()
          case _ =>
        }
      }
      else if (button == 1) {
        val newBullet = Bullet.spawn(new Vector2(toyPos), pos, Bullet.Big)
        newBullet.onCollision {
          case nightmare: Nightmare =>
            nightmare.destroy()
            newBullet.destroy()
          case _ =>
        }
      }
    })




  }

  override def update(deltaT: Float, g: GdxGraphics): Unit = {
    /** GAME LOGIC HERE */
    SceneManager.update(deltaT, g)

    if (rnd.nextFloat() < deltaT * spawnRate) {
      val startX = rnd.nextFloat() * g.getScreenWidth
      val startY = g.getScreenHeight
      val targetX = rnd.nextFloat() * g.getScreenWidth
      val targetY = 0
      Nightmare.spawn(new Vector2(startX, startY), new Vector2(targetX, targetY), Nightmare.Small)
    }
  }

}

