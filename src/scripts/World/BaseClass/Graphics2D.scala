package scripts.World.BaseClass

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.Sprite


trait Graphics2D { self: Object2D =>
  Scene2D.addToCurrentScene(this)
  val sprite: Sprite

  def draw(g: GdxGraphics): Unit = {
    g.drawTransformedPicture(
      pos.x,
      pos.y,
      sprite.angle,
      sprite.scale,
      sprite.current()
    )
  }
}

