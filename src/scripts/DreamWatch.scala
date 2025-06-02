package scripts

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import scripts.Globals.{WINDOW_HEIGHT, WINDOW_WIDTH}
import scripts.World.Actors.TopLevel.Bullet
import scripts.World.{Scene2D, World2D}




class DreamWatch extends PortableApplication(WINDOW_WIDTH, WINDOW_HEIGHT){

  override def onInit(): Unit = {
    setTitle("Dreamwatch")
    Gdx.input.setInputProcessor(InputManager)

    InputManager.onMousePressed((pos, button) => { // juste pour test
      val bullet: Bullet = new Bullet(pos, Bullet.Small, 0, null, 0)
      Scene2D.addToCurrentScene(bullet)
    })

    World.Process.processes.foreach(_.init())
  }



  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    World.Process.updateAll(Gdx.graphics.getDeltaTime)
    World.Process.updateAllGraphics(Gdx.graphics.getDeltaTime, g)

    g.drawFPS()
  }




}

object DreamWatch extends App{
  new DreamWatch().run()
}
