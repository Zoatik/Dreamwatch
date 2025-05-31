package scripts

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import scripts.Globals.{WINDOW_HEIGHT, WINDOW_WIDTH}
import scripts.World.World2D




class DreamWatch extends PortableApplication(WINDOW_WIDTH, WINDOW_HEIGHT){

  override def onInit(): Unit = {
    setTitle("Dreamwatch")
    Gdx.input.setInputProcessor(InputManager)
    World2D.init()
  }


  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    World2D.update(g, Gdx.graphics.getDeltaTime)


    g.drawFPS()
  }




}

object DreamWatch extends App{
  new DreamWatch().run()
}
