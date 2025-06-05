package scripts.Managers

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.Vector2
import scripts.Globals
import scripts.World.Actors.TopLevel.Boss.UneAraignee
import scripts.World.Actors.TopLevel.{Boss, Nightmare}

import scala.util.Random


object WavesManager extends Manager[Unit] {
  private var waveCounter: Int = 0
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

  // Overrides
  override def init(nothing: Unit): Unit = {
    // Will probably do a thing where it wait for you to press "enter" for ex.
    startNewWave()
  }
  override def update(deltaT: Float): Unit = {
    super.update(deltaT)
    currentTime += deltaT
    waveTimer += deltaT
    updateWave(deltaT)

    // Test
    InputManager.onMousePressed((_,_) => bossDefeated = true)
    InputManager.onMousePressed((_,_) => cardsSelectionDone = true)

    if(!isAlive){
      // Game is lost !
      println("Game is lost.")
    }
    waveStatus match {
      case "normal" =>
        if (waveTimer < Globals.WAVE_LENGTH) {
          // Do stuff here during the normal wave
        }
//        else if (waveCounter%Globals.NBR_WAVES_BEFORE_BOSS == 0){
//
//        }
        else {
          // End of the wave. Do this:
          // Stop spawning nightmares
          spawnRate = 0
          if(!ScenesManager.currentScene.getObjects.exists(e => e.isInstanceOf[Nightmare])) {
            waveStatus = "idle"
          }
        }


      case "boss" =>
        // Boss wave loop
        if(!bossDefeated){
        }
        // What happens when the boss is defeated
        else{
          if(bossCounter == Globals.NBR_OF_BOSSES){
            // Game is won !
            println("Game is won !")
            println(s"${bossCounter} bosses were defeated.")
          }
          else{
            // Next boss
            waveStatus = "idle"
            bossCounter += 1
            bossDefeated = false
          }
        }


      case "idle" =>
        println("Entered idle phase.. choose your cards.")
        // Show cards (will most likely handle the cards showing that will access this object and modify the cardsSelectionDone)
        // What happens once you've chosen you're upgrade card
        if (cardsSelectionDone) {
          if (waveCounter % Globals.NBR_WAVES_BEFORE_BOSS == 0) {
            cardsSelectionDone = false
            startNewBossWave()
          }
          else {
            cardsSelectionDone = false
            startNewWave()
          }
        }
        // While cards haven't been selected yet but still in idle
        else {

        }

      // Final default case
      case _ =>
    }
  }



  // Wave functions
  def startNewWave(): Unit = {
    waveCounter += 1
    waveTimer = 0
    //maxNightmares += 2
    waveStatus = "normal"
    spawnRate = (Globals.DEFAULT_SPAWN_RATE + waveCounter)*10
  }
  def startNewBossWave(): Unit = {
    waveStatus = "boss"
    waveCounter += 1
    bossCounter match {
      case 1 =>
        new Boss(new Vector2 (990, 510), Boss.UneAraignee).spawn()
      case 2 =>
        new Boss(new Vector2 (990, 510), Boss.Ghost).spawn()
      case 3 =>
        new Boss(new Vector2 (990, 510), Boss.TheGrimReaper).spawn()
      case _ =>
    }
  }

  // Basically handles all the spawning
  def updateWave(deltaT: Float): Unit = {
      if (rnd.nextFloat() < deltaT * spawnRate) {
      val startX = rnd.nextFloat() * GameManager.g.getScreenWidth
      val startY = GameManager.g.getScreenHeight
      val targetX = rnd.nextFloat() * GameManager.g.getScreenWidth
      val targetY = 0f
      new Nightmare(new Vector2(startX, startY), new Vector2(targetX, targetY), Nightmare.Small).spawn()
      }
  }
}
