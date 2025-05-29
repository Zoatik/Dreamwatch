import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

class DreamWatch extends PortableApplication {
  println("1. DemoDebug constructor starts")

  var a = {
    println("2. Field 'a' initialization starting")
    val result = 1
    println(s"3. Field 'a' initialized to $result")
    result
  }

  println("4. DemoDebug constructor about to call parent constructor")

  override def onInit(): Unit = {
    println(s"5. onInit() called, a = $a")
    setTitle("demo" + a)
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    a += 1
    g.clear()
    g.drawRectangle(10, 10, 20, 20, 0)
    g.drawAntiAliasedCircle(200, 200, a, Color.RED)
  }

  println("6. DemoDebug constructor completed")


}

object DreamWatch extends App{
  new DreamWatch().run()
}