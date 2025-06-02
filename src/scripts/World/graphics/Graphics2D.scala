package scripts.World.graphics

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.Sprite
import scripts.World.Actors.Base.Object2D


trait Graphics2D { self: Object2D =>
  val sprite: Sprite
  val graphicLayerZ: Int

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

