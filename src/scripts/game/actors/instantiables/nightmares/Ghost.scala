package scripts.game.actors.instantiables.nightmares

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.physics.Collider2D
import scripts.game.actors.abstracts.Nightmare
import scripts.game.actors.instantiables.Toy

import scala.collection.mutable.ArrayBuffer

class Ghost(pos: Vector2, targetPos: Vector2, animDuration: Float = 1.0f, scale: Float = 1.0f)
  extends Nightmare(
    pos,
    targetPos,
    ArrayBuffer(
    "res/sprites/nightmares/ghost_1.png",
    "res/sprites/nightmares/ghost_2.png",
    "res/sprites/nightmares/ghost_3.png",
    "res/sprites/nightmares/ghost_4.png",
    "res/sprites/nightmares/ghost_5.png",
    "res/sprites/nightmares/ghost_6.png"),
    animDuration,
    scale) {

    override var speed: Float = 100.0f

    /*override protected def onTargetReached(): Unit = {
        super.onTargetReached()

    }*/

}
