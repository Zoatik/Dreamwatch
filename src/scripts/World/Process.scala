package scripts.World

import ch.hevs.gdx2d.lib.GdxGraphics
import scripts.World.Process.processes

import scala.collection.mutable.ArrayBuffer


object Process {
  val processes: ArrayBuffer[Process] = ArrayBuffer()

  def updateAll(deltaT: Float): Unit = {
    processes.foreach(_.update(deltaT))
  }

  def updateAllGraphics(deltaT: Float, g: GdxGraphics): Unit = {
    for (process <- processes) process match {
      case graphicProcess: GraphicProcess =>
        graphicProcess.updateGraphics(deltaT, g)
    }
  }

}

trait Process {
  processes += this
  protected var _isPaused: Boolean = false
  def isPaused = _isPaused
  def init(): Unit
  def update(deltaT: Float): Unit
  def pause(): Unit = if (!isPaused) _isPaused = true
  def resume(): Unit = if (isPaused) _isPaused = false
}

trait GraphicProcess extends Process{
  def updateGraphics(deltaT: Float, g: GdxGraphics)
}
