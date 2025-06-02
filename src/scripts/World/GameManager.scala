package scripts.World

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.Vector2
import scripts.World.Actors.TopLevel.Bullet
import scripts.{Globals, InputManager}


object GameManager extends Manager[GdxGraphics]{

  val toyPos: Vector2 = new Vector2(Globals.WINDOW_WIDTH/2, 0)

  override def init(): Unit = {
    println("CollisionsManager ready")
    SceneManager.init()

    InputManager.onMousePressed((pos, button) => {
      if (button == 0) {
        val newBullet = Bullet.spawn(new Vector2(toyPos), pos, Bullet.Small)
        newBullet.onCollision(other => {
          println(s"bullet : $newBullet collided with bullet : $other")
        })
      }
      else if (button == 1) {
        val newBullet = Bullet.spawn(new Vector2(toyPos), pos, Bullet.Big)
        newBullet.onCollision(other => {
          println(s"bullet : $newBullet collided with bullet : $other")
        })
      }
    })
  }

  override def update(deltaT: Float, g: GdxGraphics): Unit = {
    /** GAME LOGIC HERE */
    SceneManager.update(deltaT, g)
  }

}

