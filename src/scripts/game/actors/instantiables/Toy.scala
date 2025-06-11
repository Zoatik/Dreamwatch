package scripts.game.actors.instantiables

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.instantiables.CollisionSprite2D
import scripts.dreamwatch_engine.physics.{Area2D, Movement2D}
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

class Toy(pos: Vector2, images: ArrayBuffer[BitmapImage])
  extends CollisionSprite2D(pos, 0, images, Globals.TOY_G_LAYER, Globals.TOY_C_LAYER, Globals.NIGHTMARE_C_LAYERMASK, Area2D.Box) with Movement2D {

  override var speed: Float = 0
  override var target: Vector2 = null

  /**
   * Remove this entity from the current scene via the ScenesManager.
   * Called when the entity should no longer exist (e.g., lifetime expired or explicit destroy).
   */
  override def destroy(): Unit = ???
}
