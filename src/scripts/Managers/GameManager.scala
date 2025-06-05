package scripts.Managers

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.Vector2
import scripts.GUI.UiElement
import scripts.Globals
import scripts.World.Actors.Base.Scene2D
import scripts.World.Actors.TopLevel.GameScene2D
import scripts.World.Physics.Area2D

import scala.collection.mutable.ArrayBuffer

/**
 * GameManager handles high-level game logic: input-driven bullet spawning and random enemy spawning.
 * It implements Manager[GdxGraphics] to receive update calls each frame with rendering context.
 */
object GameManager extends Manager[GdxGraphics] {

  /**
   * Central toy position from which bullets originate (middle of screen horizontally, bottom).
   */
  val toyPos: Vector2 = new Vector2(Globals.WINDOW_WIDTH / 2f, 0)

  var g: GdxGraphics = _

  val scenes: ArrayBuffer[Scene2D] = ArrayBuffer()
  var currentScene: Scene2D = _



  /**
   * Initialization logic for GameManager. Called once at startup.
   * Registers an input listener to spawn bullets based on mouse button clicks.
   */
  override def init(gdxGraphics: GdxGraphics): Unit = {
    g = gdxGraphics
    println("GameManager ready")
    // Initialize scene management (layers, etc.) before spawning anything.
    WavesManager.init()

    // Register a mouse-pressed listener: depending on button, spawn different bullet types.
    InputManager.onMousePressed((pos, button) => {
      handleMouseInput(pos, button)
    })

    currentScene = new GameScene2D
    scenes += currentScene

    val im = ArrayBuffer(new BitmapImage("res/sprites/soccer.png"))
    new UiElement(new Vector2(100,100), im, 0, Area2D.Circle).instantiate()
  }

  /**
   * Update method called each frame. Contains game logic and random enemy spawning.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   * @param g      GdxGraphics used for rendering (passed to ScenesManager).
   */
  override def update(deltaT: Float): Unit = {
    // First, update all scene-related managers (rendering, collisions, etc.)
    require(g != null, "GameManager must be initialized with gdxGraphics !")

    WavesManager.update(deltaT)

  }

  def handleMouseInput(pos: Vector2, button: Int): Unit = {
    currentScene.handleMouseInput(pos, button)
  }
}