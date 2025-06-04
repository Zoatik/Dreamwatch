package scripts.Managers

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.Vector2
import scripts.World.Actors.TopLevel.{Bullet, Nightmare}
import scripts.Globals

import scala.util.Random

/**
 * GameManager handles high-level game logic: input-driven bullet spawning and random enemy spawning.
 * It implements Manager[GdxGraphics] to receive update calls each frame with rendering context.
 */
object GameManager extends Manager[GdxGraphics] {

  /**
   * Central toy position from which bullets originate (middle of screen horizontally, bottom).
   */
  val toyPos: Vector2 = new Vector2(Globals.WINDOW_WIDTH / 2f, 0)

  // Spawn rate (enemies per second) used for random spawning of Nightmare instances.
  private val spawnRate = 0.5f

  // Random number generator for spawn timing and positions.
  private val rnd = new Random()

  /**
   * Initialization logic for GameManager. Called once at startup.
   * Registers an input listener to spawn bullets based on mouse button clicks.
   */
  override def init(): Unit = {
    println("GameManager ready")
    // Initialize scene management (layers, etc.) before spawning anything.
    ScenesManager.init()

    // Register a mouse-pressed listener: depending on button, spawn different bullet types.
    InputManager.onMousePressed((pos, button) => {
      button match {
        case 0 =>
          // Left click: small bullet fired from toyPos toward mouse position.
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Small).spawn()
        case 1 =>
          // Right click: big bullet fired from toyPos toward mouse position.
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Big).spawn()
        case 2 =>
          // Middle click: laser bullet fired from toyPos toward mouse position.
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Laser).spawn()
        case _ =>
        // Other buttons: no action.
      }
    })
  }

  /**
   * Update method called each frame. Contains game logic and random enemy spawning.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   * @param g      GdxGraphics used for rendering (passed to ScenesManager).
   */
  override def update(deltaT: Float, g: GdxGraphics): Unit = {
    // First, update all scene-related managers (rendering, collisions, etc.)
    ScenesManager.update(deltaT, g)

    // Randomly spawn a new Nightmare enemy with probability = spawnRate * deltaT
    if (rnd.nextFloat() < deltaT * spawnRate) {
      // Determine random start position along the top edge of the screen
      val startX = rnd.nextFloat() * g.getScreenWidth
      val startY = g.getScreenHeight
      // Determine random target position along the bottom edge
      val targetX = rnd.nextFloat() * g.getScreenWidth
      val targetY = 0f
      // Create and spawn a small Nightmare enemy dropping from top toward bottom
      new Nightmare(new Vector2(startX, startY), new Vector2(targetX, targetY), Nightmare.Small).spawn()
    }
  }
}