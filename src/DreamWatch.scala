import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import Globals.{WINDOW_HEIGHT, WINDOW_WIDTH}
import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries
import ch.hevs.gdx2d.desktop.physics.DebugRenderer
import ch.hevs.gdx2d.lib.physics.PhysicsWorld
import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.math.Vector2

import java.util
import java.util.{Iterator, LinkedList}



class DreamWatch extends PortableApplication(WINDOW_WIDTH, WINDOW_HEIGHT) {

  override def onInit(): Unit = {
    setTitle("Dreamwatch")
    World2D.init()
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()

    World2D.update(GdxGraphics, Gdx.graphics.getDeltaTime)

    g.drawFPS()
  }


}

object DreamWatch extends App{
  new DreamWatch().run()
}
