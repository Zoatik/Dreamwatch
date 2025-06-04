package scripts.Managers

import scripts.Globals


object WaveManager extends Manager[WaveContext]{
  private var waveCounter: Int = 0
  private var waveTimer: Long = 0
  private var waveStartTime: Long = 0
  private var currentTime: Long = System.currentTimeMillis()

  private var maxNightmares: Int = 20
  // Has to be called and modified somewhere else:
  var noMoreNightmaresInScene: Boolean = false

  private var waveStatus: String = "normal"
  var cardsSelectionDone: Boolean = false

  private var bossCounter: Int = 0
  private var bossDefeated: Boolean = false




  // Overrides
  override def init(): Unit = {
    // Will probably do a thing where it wait for you to press "enter" for ex.
    startNewWave()
  }
  override def update(deltaT: Float, ctx: WaveContext): Unit = {
    waveStatus match {
      case "normal" =>
        waveTimer = currentTime - waveStartTime
        if (waveTimer < Globals.WAVE_LENGTH) {
          // Do stuff here during the normal wave
        }
        else {
          // End of the wave. Do this:
          if(noMoreNightmaresInScene) {
            waveStatus = "idle"
          }
        }



      case "boss" =>
        // What happens during the boss wave
        if(!bossDefeated){

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
        // Show cards (will most likely handle the cards showing that will access this object and modify the cardsSelectionDone
        if (!cardsSelectionDone) {
          if (waveCounter % Globals.NBR_WAVES_B4_BOSS == 0) {
            startNewBossWave()
          }
          else {
            startNewWave()
          }
        }
        // What happens once you've chosen you're upgrade card
        else {
          // Will become true when entering so it's done => have to set it back
          cardsSelectionDone = false
        }

      // Final default case
      case _ =>
    }
  }



  // Wave functions
  def startNewWave(): Unit = {
    waveStartTime = currentTime
    waveTimer = 0
    maxNightmares += 2
    waveStatus = "normal"
    waveCounter += 1
  }
  def startNewBossWave(): Unit = {
    waveStatus = "boss"
    waveCounter += 1
  }
}
