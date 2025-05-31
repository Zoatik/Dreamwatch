package scripts.World.BaseClass

import com.badlogic.gdx.math.Vector2
import scripts.InputManager

import scala.collection.mutable.ArrayBuffer

abstract class Area2D(var pos: Vector2) {
  private val mouseEnterListeners: ArrayBuffer[Vector2 => Unit] = ArrayBuffer()
  private val mouseLeaveListeners: ArrayBuffer[Vector2 => Unit] = ArrayBuffer()
  private val mousePressedListeners: ArrayBuffer[(Vector2, Int) => Unit] = ArrayBuffer()
  private val mouseReleasedListeners: ArrayBuffer[(Vector2, Int) => Unit] = ArrayBuffer()

  InputManager.onMouseMoved(mousePos => mouseMoved(mousePos))
  InputManager.onMousePressed((mousePos, mouseButton) => mousePressed(mousePos, mouseButton))
  InputManager.onMouseReleased((mousePos, mouseButton) => mouseReleased(mousePos, mouseButton))

  private var _isMouseOver: Boolean = false


  def intersects(other: Area2D): Boolean
  def containsPoint(p: Vector2): Boolean

  def isMouseOver(mousePos: Vector2): Boolean = containsPoint(mousePos)

  def mouseMoved(mousePos: Vector2): Unit = {
    val mouseState = isMouseOver(mousePos)
    if (mouseState != _isMouseOver){
      _isMouseOver = mouseState
      if (_isMouseOver)
        mouseEntered(mousePos)
      else
        mouseLeft(mousePos)
    }
  }

  def onMouseEntered(listener: Vector2 => Unit): Unit = {
    mouseEnterListeners += listener
  }

  def onMouseLeft(listener: Vector2 => Unit): Unit = {
    mouseLeaveListeners += listener
  }

  def onMousePressed(listener: (Vector2, Int) => Unit): Unit = {
    mousePressedListeners += listener
  }

  def onMouseReleased(listener: (Vector2, Int) => Unit): Unit = {
    mouseReleasedListeners += listener
  }

  protected def mouseEntered(mousePos: Vector2): Unit = {
    mouseEnterListeners.foreach(_(mousePos))
  }

  protected def mouseLeft(mousePos: Vector2): Unit = {
    mouseLeaveListeners.foreach(_(mousePos))
  }

  protected def mousePressed(mousePos: Vector2, mouseButton: Int): Unit = {
    if (isMouseOver(mousePos))
      mousePressedListeners.foreach(_(mousePos, mouseButton))
  }

  protected def mouseReleased(mousePos: Vector2, mouseButton: Int): Unit = {
    if (isMouseOver(mousePos))
      mouseReleasedListeners.foreach(_(mousePos, mouseButton))
  }


}

class CircleArea2D(pos: Vector2, var radius: Float) extends Area2D(pos) {

  override def intersects(other: Area2D): Boolean = other match {
    case c: CircleArea2D =>
      val dist2 = pos.dst2(c.pos)
      dist2 <= (radius + c.radius)*(radius + c.radius)

    case r: BoxArea2D =>
      val closestX = math.max(r.pos.x, math.min(pos.x, r.pos.x + r.width))
      val closestY = math.max(r.pos.y, math.min(pos.y, r.pos.y + r.height))
      val dx = pos.x - closestX
      val dy = pos.y - closestY
      dx*dx + dy*dy <= radius * radius

    case _ => false
  }

  override def containsPoint(p: Vector2): Boolean = {
    p.sub(pos).len() <= radius
  }
}

class BoxArea2D(pos: Vector2, var width: Float, var height: Float) extends Area2D(pos) {

  override def intersects(other: Area2D): Boolean = other match {
    case c: CircleArea2D =>
      c.intersects(this)

    case r: BoxArea2D =>
      pos.x < r.pos.x + r.width  &&
      pos.x + width > r.pos.x   &&
      pos.y < r.pos.y + r.height &&
      height + pos.y > r.pos.y

    case _ => false
  }

  override def containsPoint(p: Vector2): Boolean = {
    p.x >= pos.x  && p.x < pos.x + width && p.y >= pos.y && p.y < pos.y + height
  }
}



