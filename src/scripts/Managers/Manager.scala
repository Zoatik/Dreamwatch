package scripts.Managers

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.Layer
import scripts.World.Physics.{Collider2D, Movement2D}
import scripts.World.graphics.Graphics2D

import scala.collection.mutable.ArrayBuffer

/**
 * Trait representing a generic manager in the game engine.
 * The type parameter T defines the context passed to update each frame.
 * Subclasses must implement initialization and per-frame update logic.
 *
 * @tparam T The type of the context provided on each update call.
 */
trait Manager[T] {
  /** Flag indicating whether this manager is currently paused. */
  protected var _isPaused: Boolean = false

  /**
   * Query whether this manager is paused.
   * @return true if paused, false otherwise.
   */
  def isPaused: Boolean = _isPaused

  /**
   * Perform any necessary setup when the manager is first created.
   * Called once at application start.
   */
  def init(): Unit

  /**
   * Update method called each frame with the elapsed time and a context object.
   * Concrete managers implement this to perform their per-frame logic.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   * @param ctx    Context object providing additional data needed for update.
   */
  def update(deltaT: Float, ctx: T): Unit = {
    if(_isPaused) return
  }

  /**
   * Pause this manager, preventing its update logic from running until resumed.
   */
  def pause(): Unit = if (!_isPaused) _isPaused = true

  /**
   * Resume this manager after being paused, allowing update logic to run again.
   */
  def resume(): Unit = if (_isPaused) _isPaused = false
}

/**
 * Context passed to collision-related managers or systems.
 *
 * @param cLayer The collision layer containing Collider2D objects to process.
 */
case class CollisionContext(cLayer: Layer[Collider2D])

/**
 * Context passed to movement-related managers or systems.
 *
 * @param movableObjects Buffer of Movement2D instances to update each frame.
 * @param deltaScale     Scalar multiplier for delta time (e.g., for slow-motion effects).
 */
case class MovementContext(movableObjects: ArrayBuffer[Movement2D], deltaScale: Float)

/**
 * Context passed to rendering-related managers or systems.
 *
 * @param gLayer The render layer containing Graphics2D objects to draw.
 * @param g      The GdxGraphics instance used for drawing on screen.
 */
case class RenderingContext(gLayer: Layer[Graphics2D], g: GdxGraphics)

// The following contexts could be used for more complex scene or game-level updates:
// case class SceneContext(renderingCtx: RenderingContext)
// case class GameContext(sceneContext: SceneContext)

case class WaveContext(g: GdxGraphics)

case class GameContext(g: GdxGraphics)