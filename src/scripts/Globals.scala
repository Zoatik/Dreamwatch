package scripts

import scripts.World.Actors.TopLevel.Boss

object Globals {
  val WINDOW_WIDTH = 1920
  val WINDOW_HEIGHT = 1080

  val G_LAYERS_SIZE = 3
  val C_LAYERS_SIZE = 3
  val UI_LAYERS_SIZE = 1

  val NB_OF_SCENES = 3

  val BULLET_G_LAYER = 1
  val NIGHTMARE_G_LAYER = 0
  val BOSS_G_LAYER = 2

  val BULLET_C_LAYER = 0
  val NIGHTMARE_C_LAYER = 0
  val BOSS_C_LAYER = 0

  val WAVE_LENGTH: Float = 2          // Should be 60
  val DEFAULT_SPAWN_RATE: Float = 0.5f

  val NBR_WAVES_BEFORE_BOSS: Int = 2  // Should be 5
  val NBR_OF_BOSSES: Int = 3          // Should be 4

}