package scripts.game.actors.instantiables

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.{Object2D, Scene}
import scripts.dreamwatch_engine.actors.instantiables.{Particle2D, Sprite2D, UiElement}
import scripts.dreamwatch_engine.physics.{Collider2D, Movement2D}
import scripts.dreamwatch_engine.utils.Layers
import scripts.game.actors.abstracts.Player
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

class MainMenuScene extends Scene {

  override val gLayers: Layers[Sprite2D] = new Layers[Sprite2D](Globals.G_LAYERS_SIZE)
  override val uiLayers: Layers[UiElement] = new Layers[UiElement](Globals.UI_LAYERS_SIZE)
  override val cLayers: Layers[Collider2D] = new Layers[Collider2D](Globals.C_LAYERS_SIZE)
  override val movableObjects: ArrayBuffer[Movement2D] = ArrayBuffer()
  override val objects: ArrayBuffer[Object2D] = ArrayBuffer()
  override val particles: ArrayBuffer[Particle2D] = ArrayBuffer()
  override val player: Player = new GamePlayer()

  override def handleMouseInput(pos: Vector2, button: Int): Unit = ???

  override def handleKeyInput(button: Int): Unit = ???

}


