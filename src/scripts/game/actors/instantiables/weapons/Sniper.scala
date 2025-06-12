package scripts.game.actors.instantiables.weapons

import com.badlogic.gdx.math.Vector2
import scripts.game.actors.abstracts.Weapon
import scripts.game.actors.instantiables.Bullet

import scala.collection.mutable.ArrayBuffer

class Sniper(pos: Vector2)
  extends Weapon(
    pos,
    ArrayBuffer("res/sprites/placeholders/soccer.png")
  ){

  override def instantiate(): Sniper = {
    super.instantiate()
    this
  }


  override protected var bulletTypePrimary: Bullet.Type = Bullet.Piercing
  override protected var bulletTypeSecondary: Bullet.Type = Bullet.Explosive

}
