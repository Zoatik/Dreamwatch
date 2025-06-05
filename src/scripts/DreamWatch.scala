package scripts

import ch.hevs.gdx2d.desktop.{Game2D, PortableApplication}
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import scripts.Globals.{WINDOW_HEIGHT, WINDOW_WIDTH}
import scripts.Managers.{GameManager, InputManager}


class DreamWatch extends PortableApplication(WINDOW_WIDTH, WINDOW_HEIGHT){
  override def onInit(): Unit = {
    setTitle("Dreamwatch")
    Gdx.input.setInputProcessor(InputManager)
    GameManager.init(Game2D.g)
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    GameManager.update(Gdx.graphics.getDeltaTime)

    g.drawFPS()
  }
}

object DreamWatch extends App{
  new DreamWatch().run()
}
