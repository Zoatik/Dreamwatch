package scripts.World.Actors.Base

import com.badlogic.gdx.math.Vector2
import scripts.Sprite
import scripts.World.graphics.Graphics2D

/**
 * Base class for any object in the 2D world.
 * Combines position handling, a sprite for rendering, and optional lifetime management.
 *
 * @param _pos           Initial position of the object (protected; use pos getter/setter to access).
 * @param sprite         The Sprite instance used for rendering this object.
 * @param graphicLayerZ  Z-index indicating which rendering layer this object belongs to.
 * @param lifeTime       Optional lifetime (in seconds). If defined, the object will be removed when expired.
 */
abstract class Object2D(
  protected var _pos: Vector2,
  override val sprite: Sprite,
  override val graphicLayerZ: Int,
  lifeTime: Option[Float] = None
) extends Entity(lifeTime) with Graphics2D {

  /**
   * Retrieve the current position of this Object2D.
   * @return A Vector2 representing the object's position.
   */
  def pos: Vector2 = _pos

  /**
   * Update the position of this Object2D.
   * Also synchronizes the internal sprite's position to match.
   *
   * @param newPos The new position to assign to this object.
   */
  def pos_=(newPos: Vector2): Unit = {
    // Update internal position field
    _pos = newPos
    // Ensure the sprite is rendered at the same coordinates
    sprite.pos = newPos
  }

}
