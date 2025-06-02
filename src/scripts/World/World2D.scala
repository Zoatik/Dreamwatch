package scripts.World

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.World.Actors.TopLevel.Bullet
import scripts.World.Physics.{Collider2D, Movement2D}
import scripts.{InputManager, Layers, Sprite}

import scala.collection.mutable.ArrayBuffer

object World2D{

  InputManager.onMousePressed((pos, button) => {
    val bullet: Bullet = new Bullet(pos, Bullet.Small, 0, null, 0)
    Scene2D.addToCurrentScene(bullet)
  })

}

