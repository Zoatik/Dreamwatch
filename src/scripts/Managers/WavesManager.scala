package scripts.Managers

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.Vector2
import scripts.Globals
import scripts.World.Actors.TopLevel.Nightmare

import scala.util.Random


object WavesManager extends Manager[GdxGraphics]{
  private var waveCounter: Int = 1
  private var waveTimer: Float = 0
  private var waveStartTime: Float = 0
  private var waveStatus: String = "normal"
  private var currentTime: Float = 0

  private var maxNightmares: Int = 20
  private var spawnRate = Globals.DEFAULT_SPAWN_RATE
  private var temporarySpawnRate = spawnRate
  private var bossCounter: Int = 0

  // Random number generator for spawn timing and positions.
  private val rnd = new Random()

  // Has to be called and modified somewhere else:
  var cardsSelectionDone: Boolean = true
  var bossDefeated: Boolean = false
  var isAlive: Boolean = true

  // Overrides
  override def init(): Unit = {
    // Will probably do a thing where it wait for you to press "enter" for ex.
    startNewWave()
  }
  override def update(deltaT: Float, g: GdxGraphics): Unit = {
    super.update(deltaT, g)
    currentTime += deltaT
    updateWave(deltaT, g)

    if(!isAlive){
      // Game is lost !
    }
    waveStatus match {
      case "normal" =>
        waveTimer = currentTime - waveStartTime
        if (waveTimer < Globals.WAVE_LENGTH) {
          // Do stuff here during the normal wave
        }
        else {
          // End of the wave. Do this:
          // Stop spawning nightmares
          temporarySpawnRate = spawnRate
          spawnRate = 0
          if(!ScenesManager.currentScene.getEntities.exists(e => e.isInstanceOf[Nightmare])) {
            waveStatus = "idle"
          }
        }



      case "boss" =>
        // What happens during the boss wave
        println("Boss reached")
        val simpleTestBool: Boolean = true
        if(!bossDefeated){
          println("Yes")
        }

        else if(simpleTestBool){
          startNewWave()
        }

        // What happens when the boss is defeated
        else{
          if(bossCounter == Globals.NBR_OF_BOSSES){
            //Game is won !
          }
          else{
            bossCounter += 1
          }
        }



      case "idle" =>
        println("Idle reached")
        // Show cards (will most likely handle the cards showing that will access this object and modify the cardsSelectionDone)
        // What happens once you've chosen you're upgrade card
        if (cardsSelectionDone) {
          if (waveCounter % Globals.NBR_WAVES_B4_BOSS == 0) {
            startNewBossWave()
          }
          else {
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
    println(s"Entered new wave: ${waveCounter}")
    waveStartTime = waveTimer
    //waveTimer = 0
    //maxNightmares += 2
    waveStatus = "normal"
    waveCounter += 1
    spawnRate = temporarySpawnRate*100
  }
  def startNewBossWave(): Unit = {
    println("Entered new boss wave")
    waveStatus = "boss"
    waveCounter += 1
  }

  def updateWave(deltaT: Float, g: GdxGraphics): Unit = {
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
