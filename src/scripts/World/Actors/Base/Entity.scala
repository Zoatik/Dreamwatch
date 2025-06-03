package scripts.World.Actors.Base

import scripts.Managers.ScenesManager

abstract class Entity(var lifeTime: Option[Float] = None) {
  def destroy(): Unit = {
    ScenesManager.removeFromCurrentScene(this)
  }

  def spawn(): Entity = {
    ScenesManager.addToCurrentScene(this)
    this
  }

  def update(deltaT: Float): Unit = {
    if (lifeTime.isDefined) {
      lifeTime = Some(lifeTime.get - deltaT)
      println(lifeTime.get)
      if (lifeTime.get <= 0) {
        destroy()
        return
      }
    }
  }

}
