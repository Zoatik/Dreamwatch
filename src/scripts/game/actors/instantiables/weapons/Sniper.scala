package scripts.game.actors.instantiables.weapons

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.game.actors.abstracts.Weapon
import scripts.game.actors.instantiables.Bullet

import scala.collection.mutable.ArrayBuffer

class Sniper(pos: Vector2)
  extends Weapon(
    pos,
    ArrayBuffer(
    new BitmapImage(""),
    new BitmapImage(""),
    new BitmapImage("")
  )) {


  /** Speed scalar in units per second. */
  override var speed: Float = ???
  /** Target position toward which the object moves. */
  override var target: Vector2 = ???
  override protected val bulletType: Bullet.Type = ???
}
