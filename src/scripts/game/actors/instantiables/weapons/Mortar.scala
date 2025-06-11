package scripts.game.actors.instantiables.weapons

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.instantiables.Sprite2D
import scripts.game.actors.abstracts.Weapon
import scripts.game.actors.instantiables.Bullet

import scala.collection.mutable.ArrayBuffer

class Mortar(pos: Vector2)
  extends Weapon(
    pos,
    ArrayBuffer("res/sprites/card.png")
  ){

  override def instantiate(): Mortar = {
    super.instantiate()
    this
  }


  override protected val bulletType: Bullet.Type = Bullet.Bomb

}
