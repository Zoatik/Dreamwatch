package scripts

import ch.hevs.gdx2d.desktop.{Game2D, PortableApplication}
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import scripts.dreamwatch_engine.inputs.InputManager
import scripts.game.{GameManager, MusicManager}
import scripts.utils.Globals.{WINDOW_HEIGHT, WINDOW_WIDTH}


class DreamWatch extends PortableApplication(WINDOW_WIDTH, WINDOW_HEIGHT){
  override def onInit(): Unit = {
    setTitle("Dreamwatch")
    Gdx.input.setInputProcessor(InputManager)
    MusicManager.init()
    GameManager.init(Game2D.g)
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    GameManager.update(Gdx.graphics.getDeltaTime)
  }

  override def onDispose(): Unit = {
    super.onDispose()
    MusicManager.dispose()
  }

}

object DreamWatch extends App{
  new DreamWatch().run()
}
