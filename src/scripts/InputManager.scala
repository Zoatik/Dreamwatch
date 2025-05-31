package scripts

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ArrayBuffer

object InputManager extends InputAdapter {
  private val mouseMotionListener: ArrayBuffer[Vector2 => Unit] = ArrayBuffer()
  private val mousePressedListeners: ArrayBuffer[(Vector2, Int) => Unit] = ArrayBuffer()
  private val mouseReleasedListeners: ArrayBuffer[(Vector2, Int) => Unit] = ArrayBuffer()

  override def mouseMoved(screenX: Int, screenY: Int): Boolean = {
    mouseMotionListener.foreach(_(new Vector2(screenX, screenY)))
    true
  }

  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    mousePressedListeners.foreach(_(new Vector2(screenX, screenY), button))
    true
  }

  override def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    mouseReleasedListeners.foreach(_(new Vector2(screenX, screenY), button))
    true
  }


  def onMouseMoved(listener: Vector2 => Unit): Unit = {
    mouseMotionListener += listener
  }

  def onMousePressed(listener: (Vector2, Int) => Unit): Unit = {
    mousePressedListeners += listener
  }

  def onMouseReleased(listener: (Vector2, Int) => Unit): Unit = {
    mouseReleasedListeners += listener
  }



}
