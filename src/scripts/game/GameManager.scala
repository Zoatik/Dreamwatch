package scripts.game

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.{Vector2, Vector3}
import com.badlogic.gdx.{Gdx, Input}
import scripts.dreamwatch_engine.actors.abstracts.Scene
import scripts.dreamwatch_engine.actors.instantiables.{Particle2D, Sprite2D, UiElement}
import scripts.dreamwatch_engine.inputs.InputManager
import scripts.dreamwatch_engine.physics.Area2D
import scripts.game.actors.abstracts.Weapon
import scripts.game.actors.instantiables.weapons.Sniper
import scripts.game.actors.instantiables.{GamePlayer, GameScene, MainMenuScene}
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

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

    val sniper: Sniper = new Sniper(new Vector2(Globals.WINDOW_WIDTH/2, 50))
    val gamePlayer: GamePlayer = new GamePlayer(sniper)
    currentScene = new GameScene(gamePlayer).instantiate()
    gamePlayer.instantiate()
    //scenes += currentScene

    // TESTS
    //val im = ArrayBuffer(new BitmapImage("res/sprites/soccer.png"))
    //val im2 = ArrayBuffer(new BitmapImage("res/sprites/cloud.png"))
    //val test: Sprite2D = new Sprite2D(new Vector2(100,100), 0, im, 0, Area2D.Circle).instantiate()
    //val test2: Sprite2D = new Sprite2D(new Vector2(1000,1000), 0, im2, 2, Area2D.Circle).instantiate()


    /*val part1: Particle2D = new Particle2D("res/shaders/fire_ball_2.fp", new Vector2(0,0), 0)
    part1.shaderRenderer.setUniform("pos", new Vector3(100,100, 100))
    part1.instantiate()*/



    println("GameManager ready")
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

  def handleMouseInput(pos: Vector2, button: Int): Unit = {
    println("handled game manager")
    currentScene.handleMouseInput(pos, button)
  }

  def loadScene(newScene: Scene): Unit = {
    currentScene.destroy()
    currentScene = newScene.instantiate()
  }

}