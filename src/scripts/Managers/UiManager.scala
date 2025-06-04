package scripts.Managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import scripts.Layer
import scripts.World.graphics.Graphics2D

object UiManager extends Manager[UiContext] {

  var isMouseOverUi: Boolean = false
  /**
   * Perform any necessary setup when the manager is first created.
   * Called once at application start.
   */
  override def init(): Unit = {}

  override def update(deltaT: Float, ctx: UiContext): Unit = {
    super.update(deltaT, ctx)

    println(isMouseOverUi)
    ctx.uiLayer.elements.foreach(uiElement => {
      val mousePos: Vector2 = new Vector2(Gdx.input.getX, Gdx.input.getY)
      println(mousePos)
      println("box pos : " + uiElement.interactionArea.pos)
      println(uiElement.interactionArea.isMouseOver(mousePos))
      //println(s"box width: ${uiElement.interactionArea.}")
      if(uiElement.interactionArea.isMouseOver(mousePos)){

        isMouseOverUi = true
        return
      }
      isMouseOverUi = false
    })
  }




}
