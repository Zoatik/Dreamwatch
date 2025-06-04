package scripts.Managers

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.Layer
import scripts.World.Physics.{Collider2D, Movement2D}
import scripts.World.graphics.Graphics2D

import scala.collection.mutable.ArrayBuffer


trait Manager[T] {
  protected var _isPaused: Boolean = false
  def isPaused = _isPaused
  def init(): Unit
  def update(deltaT: Float, ctx: T): Unit
  def pause(): Unit = if (!isPaused) _isPaused = true
  def resume(): Unit = if (isPaused) _isPaused = false
}

case class CollisionContext(cLayer: Layer[Collider2D])

case class MovementContext(movableObjects: ArrayBuffer[Movement2D], deltaScale: Float)

case class RenderingContext(gLayer: Layer[Graphics2D], g: GdxGraphics)

//case class SceneContext(renderingCtx: RenderingContext)

//case class GameContext(sceneContext: SceneContext)

case class WaveContext()