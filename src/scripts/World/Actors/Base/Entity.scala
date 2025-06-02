package scripts.World.Actors.Base

import scripts.World.SceneManager

abstract class Entity {
  def destroy(): Unit

  def spawn(): Entity = {
    SceneManager.addToCurrentScene(this)
    this
  }
}
