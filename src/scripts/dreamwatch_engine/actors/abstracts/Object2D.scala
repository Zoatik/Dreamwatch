package scripts.dreamwatch_engine.actors.abstracts

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.physics.Area2D
import scripts.game.GameManager

/**
 * Base class for any object in the 2D world.
 * Combines position handling, a sprite for rendering, and optional lifetime management.
 *
 * @param pos     Initial position of the object (protected; use pos getter/setter to access).
 * @param lifeTime Optional lifetime (in seconds). If defined, the object will be removed when expired.
 */
abstract class Object2D(
  var pos: Vector2,
  var angle: Float = 0.0f,
  lifeTime: Option[Float] = None
) extends Entity(lifeTime) {

  override def destroy(): Unit = {
    GameManager.currentScene.remove(this)
  }

  override def instantiate(): Object2D = {
    GameManager.currentScene.add(this)
    this
  }

}
