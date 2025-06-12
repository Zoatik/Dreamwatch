package scripts.game

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.{Gdx, Input}
import scripts.dreamwatch_engine.actors.abstracts.Scene
import scripts.dreamwatch_engine.inputs.InputManager
import scripts.game.actors.instantiables.{MainMenuScene, Player}
import scripts.utils.Globals

/**
 * GameManager handles high-level game logic: input-driven bullet spawning and random enemy spawning.
 * It implements Manager[GdxGraphics] to receive update calls each frame with rendering context.
 */
object GameManager{

  private var _isPaused: Boolean = false
  def isPaused: Boolean = _isPaused

  /**
   * Central toy position from which bullets originate (middle of screen horizontally, bottom).
   */
  val toyPos: Vector2 = new Vector2(Globals.WINDOW_WIDTH / 2f, 0)
  val player: Player = new Player()


  var g: GdxGraphics = _

  //val scenes: ArrayBuffer[Scene] = ArrayBuffer()
  var currentScene: Scene = _

  def mousePos: Vector2 = new Vector2(Gdx.input.getX, Gdx.graphics.getHeight - Gdx.input.getY)




  /**
   * Initialization logic for GameManager. Called once at startup.
   * Registers an input listener to spawn bullets based on mouse button clicks.
   */
  def init(gdxGraphics: GdxGraphics): Unit = {
    g = gdxGraphics

    // Register a mouse-pressed listener: depending on button, spawn different bullet types.
    InputManager.bindMousePressed((pos, button) => {
      handleMouseInput(pos, button)
    })

    // Pause button
    InputManager.bindKeyPressed(button => {
      if(button == Input.Keys.ESCAPE) _isPaused = !_isPaused
    })

    //main menu
    loadScene(new MainMenuScene())
    MusicManager.playMusic(0)









  }

  /**
   * Update method called each frame. Contains game logic and random enemy spawning.
   *
   * @param deltaT Time elapsed since last frame (in seconds).
   * @param g      GdxGraphics used for rendering (passed to ScenesManager).
   */
  def update(deltaT: Float): Unit = {
    // First, update all scene-related managers (rendering, collisions, etc.)
    require(g != null, "GameManager must be initialized with gdxGraphics !")

    currentScene.update(deltaT)

  }

  def pause(): Unit = _isPaused = true

  def resume(): Unit = _isPaused = false

  def togglePause(): Unit = _isPaused = !_isPaused

  def handleMouseInput(pos: Vector2, button: Int): Unit = {
    currentScene.handleMouseInput(pos, button)
  }

  def loadScene(newScene: Scene): Unit = {
    if (currentScene != null)
      currentScene.destroy()
    currentScene = newScene
    currentScene.instantiate()
  }

}