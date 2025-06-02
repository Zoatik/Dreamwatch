package scripts.World.Actors.Base

import scripts.Managers.SceneManager

abstract class Entity {
  def destroy(): Unit = {
    SceneManager.removeFromCurrentScene(this)
  }

  def spawn(): Entity = {
    SceneManager.addToCurrentScene(this)
    this
  }
}
