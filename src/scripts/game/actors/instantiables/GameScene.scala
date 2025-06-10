package scripts.game.actors.instantiables

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.{Object2D, Scene}
import scripts.dreamwatch_engine.actors.instantiables.{Particle2D, Sprite2D, UiElement}
import scripts.dreamwatch_engine.inputs.InputManager
import scripts.dreamwatch_engine.physics.{Collider2D, Movement2D}
import scripts.dreamwatch_engine.utils.Layers
import scripts.game.GameManager
import scripts.game.GameManager.toyPos
import scripts.game.actors.abstracts.{Nightmare, Player}
import scripts.game.actors.instantiables.nightmares.Ghost
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class GameScene extends Scene{

  override val gLayers: Layers[Sprite2D] = new Layers[Sprite2D](Globals.G_LAYERS_SIZE)
  override val uiLayers: Layers[UiElement] = new Layers[UiElement](Globals.UI_LAYERS_SIZE)
  override val cLayers: Layers[Collider2D] = new Layers[Collider2D](Globals.C_LAYERS_SIZE)
  override val movableObjects: ArrayBuffer[Movement2D] = ArrayBuffer()
  override val objects: ArrayBuffer[Object2D] = ArrayBuffer()
  override val particles: ArrayBuffer[Particle2D] = ArrayBuffer()
  override val player: Player = new GamePlayer()

  private var waveCounter: Int = 1
  private var waveTimer: Float = 0
  private var waveStatus: String = "normal"
  private var currentTime: Float = 0

  private var maxNightmares: Int = 20
  private var spawnRate: Float = 0

  private var bossCounter: Int = 1

  // Random number generator for spawn timing and positions.
  private val rnd = new Random()

  // Has to be called and modified somewhere else:
  var cardsSelectionDone: Boolean = false
  var bossDefeated: Boolean = false
  var isAlive: Boolean = true

  override def instantiate(): GameScene = {
    super.instantiate()
    InputManager.onKeyPressed(_ => { // TODO: SHOULD BE HERE
      println("key pressed from GameScene")
      waveStatus match {
        case "boss" => bossDefeated = true
        case "idle" => cardsSelectionDone = true
        case _ =>
      }
    })

    startNewWave()
    this
  }

  override def update(deltaT: Float): Unit = {
    super.update(deltaT)

    if (GameManager.isPaused)
      return

    currentTime += deltaT
    waveTimer += deltaT
    updateWave(deltaT)


    if(!isAlive){
      // Game is lost !
      println("Game is lost.")
    }
    waveStatus match {
      case "normal" =>
        if (waveTimer < Globals.WAVE_LENGTH) {
          // Do stuff here during the normal wave
        }
        else {
          // End of the wave. Do this:
          // Stop spawning nightmares
          spawnRate = 0
          if(!GameManager.currentScene.objects.exists(e => e.isInstanceOf[Nightmare])) {
            waveCounter += 1
            waveStatus = "idle"
          }
          else{
            println("Wait..")
          }
        }


      case "boss" =>
        // Boss wave loop
        if(!bossDefeated){
          //InputManager.onKeyPressed(_ => bossDefeated = true) // TODO: SHOULD NOT BE HERE
        }
        // What happens when the boss is defeated
        else{
          spawnRate = 0
          if(!GameManager.currentScene.objects.exists(e => e.isInstanceOf[Nightmare])) {
            bossDefeated = false
            if (bossCounter == Globals.NBR_OF_BOSSES) {
              // Game is won !
              println("Game is won !")
              println(s"${bossCounter} bosses were defeated.")
              waveStatus = "finished"
            }
            else {
              // Next boss
              waveCounter += 1
              waveStatus = "idle"
              cardsSelectionDone = false // I HAVE NO CLUE WHY BUT cardsSelectionDone IS TRUE HERE
              bossCounter += 1
            }
          }
        }


      case "idle" =>
        //InputManager.onKeyPressed(_ => cardsSelectionDone = true) // TODO: SHOULD NOT BE HERE
        // Show cards (will most likely handle the cards showing that will access this object and modify the cardsSelectionDone)
        // What happens once you've chosen you're upgrade card
        if (cardsSelectionDone) {
          println("Cards selection DONE")
          cardsSelectionDone = false
          if (waveCounter % (Globals.NBR_WAVES_BEFORE_BOSS+1) == 0) {
            startNewBossWave()
          }
          else {
            startNewWave()
          }
        }
        // While cards haven't been selected yet but still in idle
        else {
          println("Entered idle phase.. choose your cards.")
        }

      // Final default case
      case _ => // Probably sends info that the game is done
    }
  }

  // Wave functions
  def startNewWave(): Unit = {
    println(s"Wave: $waveCounter")
    waveTimer = 0
    //maxNightmares += 2
    waveStatus = "normal"
    spawnRate = (Globals.DEFAULT_SPAWN_RATE + waveCounter)*10
  }
  def startNewBossWave(): Unit = {
    waveStatus = "boss"
    spawnRate = (Globals.DEFAULT_SPAWN_RATE + waveCounter)*40
    bossCounter match {
      case 1 =>
        new Boss(Globals.DEFAULT_BOSS_POS, Boss.UneAraignee).instantiate()
      case 2 =>
        new Boss(Globals.DEFAULT_BOSS_POS, Boss.Ghost).instantiate()
      case 3 =>
        new Boss(Globals.DEFAULT_BOSS_POS, Boss.TheGrimReaper).instantiate()
      case _ =>
        new Boss(Globals.DEFAULT_BOSS_POS, Boss.ZeMudry).instantiate()
    }
  }

  // Basically handles all the spawning
  def updateWave(deltaT: Float): Unit = {
    waveStatus match {
      case "normal" =>
        if(rnd.nextFloat() < deltaT * spawnRate) {
          val startX = rnd.nextFloat() * GameManager.g.getScreenWidth
          val startY = GameManager.g.getScreenHeight
          val targetX = rnd.nextFloat() * GameManager.g.getScreenWidth
          val targetY = 0f
          new Ghost(new Vector2(startX, startY), new Vector2(targetX, targetY)).instantiate()
        }
      case "idle" =>
      //println("Do nothing")

      case "boss" =>
      // Boss spawns
      case "yo mama" =>
        if(rnd.nextFloat() < deltaT * spawnRate) {
          val bossPos: Vector2 = new Vector2(GameManager.g.getScreenWidth/2, GameManager.g.getScreenHeight)
          val bossTarget: Vector2 = new Vector2(rnd.nextFloat()*GameManager.g.getScreenWidth, 0)
          new Ghost(bossPos, bossTarget).instantiate()
        }
      case _ =>
    }
  }

  override def handleMouseInput(pos: Vector2, button: Int): Unit = {
    if(!isMouseOnUi && !GameManager.isPaused){
      button match {
        case 0 =>
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Piercing).instantiate()
        case 1 =>
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Explosive).instantiate()
        case 2 =>
          new Bullet(new Vector2(toyPos), new Vector2(pos), Bullet.Bomb).instantiate()
        case _ =>
      }
    }
  }

  override def handleKeyInput(button: Int): Unit = {

  }



}
