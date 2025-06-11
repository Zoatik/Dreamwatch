package scripts.game.actors.instantiables.nightmares

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.game.actors.abstracts.Nightmare
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

class Ghost(pos: Vector2, targetPos: Vector2, animDuration: Float = 1.0f, scale: Float = 1.0f)
  extends Nightmare(
    pos,
    targetPos,
    ArrayBuffer(
    "src/res/sprites/ghost_1.png",
    "src/res/sprites/ghost_2.png",
    "src/res/sprites/ghost_3.png",
    "src/res/sprites/ghost_4.png",
    "src/res/sprites/ghost_5.png",
    "src/res/sprites/ghost_6.png"),
    animDuration,
    scale) {

  override var speed: Float = 100.0f
}
