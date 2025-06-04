package scripts.GUI

import scripts.Managers.ScenesManager
import scripts.Sprite
import scripts.World.Actors.Base.Entity
import scripts.World.Physics.Area2D
import scripts.World.graphics.Graphics2D

class UiElement(areaType: Area2D.Type,
                override val sprite: Sprite,
                override val graphicLayerZ: Int) extends Graphics2D {

  val interactionArea: Area2D = Area2D.createArea2D(areaType, sprite.pos, sprite.width, sprite.height)



  def instantiate(): UiElement = {
    ScenesManager.addToCurrentScene(this)
    this
  }

  def destroy(): Unit = {
    ScenesManager.removeFromCurrentScene(this)
  }

}
