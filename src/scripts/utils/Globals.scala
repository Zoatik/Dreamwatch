package scripts.utils

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.physics.Area2D

import scala.collection.mutable.ArrayBuffer

object Globals {
  val WINDOW_WIDTH = 1920
  val WINDOW_HEIGHT = 1080
  val BOTTOM_UI_HEIGHT = 200

  val G_LAYERS_SIZE = 3
  val C_LAYERS_SIZE = 4
  val UI_LAYERS_SIZE = 3

  val NB_OF_SCENES = 3

  val BULLET_G_LAYER = 1
  val NIGHTMARE_G_LAYER = 0
  val BOSS_G_LAYER = 2
  val TOY_G_LAYER = 0
  val WEAPON_G_LAYER = 2

  val BULLET_C_LAYER = 0
  val NIGHTMARE_C_LAYER = 1
  val BOSS_C_LAYER = 2
  val TOY_C_LAYER = 3

  val BULLET_C_LAYERMASK: ArrayBuffer[Int] = ArrayBuffer(NIGHTMARE_C_LAYER, BOSS_C_LAYER)
  val NIGHTMARE_C_LAYERMASK: ArrayBuffer[Int] = ArrayBuffer(BULLET_C_LAYER)
  val BOSS_C_LAYERMASK: ArrayBuffer[Int] = ArrayBuffer(BULLET_C_LAYER)
  val TOY_C_LAYERMASK: ArrayBuffer[Int] = ArrayBuffer(NIGHTMARE_C_LAYER)


  val WAVE_LENGTH: Float = 0.1f         // Should be 60
  val DEFAULT_SPAWN_RATE: Float = 0.5f

  val NBR_WAVES_BEFORE_BOSS: Int = 2 // Should be 5
  val NBR_OF_BOSSES: Int = 3          // Should be 4
  val DEFAULT_BOSS_POS: Vector2 = new Vector2(WINDOW_WIDTH/2, WINDOW_HEIGHT-300)
  val DEFAULT_BOSS_HP: Float = 20

  val CARDS_POS: Array[Vector2] = Array(new Vector2(460, 500), new Vector2(960, 500), new Vector2(1460, 500))
  val CARD_GLAYERZ: Int = 0
  val CARD_AREA2D: Area2D.type = Area2D

}