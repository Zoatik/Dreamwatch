package scripts.game.actors.instantiables

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Entity
import scripts.game.actors.abstracts.Player

class MainMenuPlayer extends Player{

  override def handleMouseInput(pos: Vector2, button: Int): Unit = ???

  override def handleKeyInput(button: Int): Unit = ???

  /**
   * Remove this entity from the current scene via the ScenesManager.
   * Called when the entity should no longer exist (e.g., lifetime expired or explicit destroy).
   */
  override def destroy(): Unit = ???

  /**
   * Add (spawn) this entity into the current scene via the ScenesManager.
   * Returns `this` to allow method chaining if desired.
   */
  override def instantiate(): Entity = ???
}
