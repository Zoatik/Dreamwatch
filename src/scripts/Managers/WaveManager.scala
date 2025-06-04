package scripts.Managers


object WaveManager extends Manager[WaveContext]{
  private var waveCounter: Int = 0
  private var waveTimer: Long = 0
  private var waveStartTime: Long = 0
  private var currentTime: Long = System.currentTimeMillis()

  private var maxNightmares: Int = 20
  private var waveStatus: String = "normal"

  private val WAVE_LENGTH: Long = 10_000
  private val NBR_WAVES_B4_BOSS: Long = 4

  // Overrides
  override def init(): Unit = {
    // Will probably do a thing where it wait for you to press enter for ex.
    startNewWave()
  }
  override def update(deltaT: Float, ctx: WaveContext): Unit = {
    waveStatus match {
      case "normal" =>
        waveTimer = currentTime - waveStartTime
        if (waveTimer < WAVE_LENGTH) {
          // Do stuff during the wave
        }
        else {
          // end of the wave. Do this:
          // show cards
          if(waveCounter % NBR_WAVES_B4_BOSS == 0){
            startNewBossWave()
          }
          else{
            startNewWave()
          }
        }
      case "boss" =>
        println("OUi")

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
