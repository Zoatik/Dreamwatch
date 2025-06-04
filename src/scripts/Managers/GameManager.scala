package scripts.Managers

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.Vector2
import scripts.GUI.UiElement
import scripts.World.Actors.TopLevel.{Bullet, Nightmare}
import scripts.{Globals, Sprite}
import scripts.World.Physics.Area2D

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
 * GameManager handles high-level game logic: input-driven bullet spawning and random enemy spawning.
 * It implements Manager[GdxGraphics] to receive update calls each frame with rendering context.
 */
object GameManager extends Manager[GameContext] {

  /**
   * Central toy position from which bullets originate (middle of screen horizontally, bottom).
   */
  val toyPos: Vector2 = new Vector2(Globals.WINDOW_WIDTH / 2f, 0)


  /**
   * Initialization logic for GameManager. Called once at startup.
   * Registers an input listener to spawn bullets based on mouse button clicks.
   */
  override def init(): Unit = {
    println("GameManager ready")
    // Initialize scene management (layers, etc.) before spawning anything.
    ScenesManager.init()
    WavesManager.init()

    // Register a mouse-pressed listener: depending on button, spawn different bullet types.
    InputManager.onMousePressed((pos, button) => {
      handleMouseInput(pos, button)
    })
    val s: Sprite = Sprite(ArrayBuffer(new BitmapImage("res/sprites/soccer.png")), new Vector2(100,100))
    new UiElement(Area2D.Circle, s, 0).instantiate()
  }

  /**
   * Update method called each frame. Contains game logic and random enemy spawning.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   * @param g      GdxGraphics used for rendering (passed to ScenesManager).
   */
  override def update(deltaT: Float, ctx: GameContext): Unit = {
    // First, update all scene-related managers (rendering, collisions, etc.)
    ScenesManager.update(deltaT, ctx.g)
    WavesManager.update(deltaT, ctx.g)

  }

  def handleMouseInput(pos: Vector2, button: Int): Unit = {
    if(!UiManager.isMouseOverUi){
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
    }
  }
}