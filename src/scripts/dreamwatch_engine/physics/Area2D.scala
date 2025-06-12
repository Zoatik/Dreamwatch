package scripts.dreamwatch_engine.physics

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Object2D
import scripts.dreamwatch_engine.inputs.InputManager

import scala.collection.mutable.ArrayBuffer

object Area2D{
  sealed trait Type
  case object Circle extends Type
  case object Box extends Type
}

/**
 * Abstract base class representing a 2D area with mouse interaction capabilities.
 * Provides collision methods to be implemented by concrete shapes, and manages
 * mouse-enter, mouse-leave, mouse-press, and mouse-release listeners.
 *
 * @param pos The position (typically center or corner) of this Area2D in world coordinates.
 */
trait Area2D {

  def pos: Vector2
  def pos_=(newPos: Vector2): Unit
  var areaType: Area2D.Type

  def width: Float
  def width_=(newWidth: Float): Unit
  def height: Float
  def height_=(newHeight: Float): Unit
  /** List of functions to call when the mouse enters this area.
   * Each listener receives the mouse position as a Vector2(screenX, screenY).
   */
  private val mouseEnterListeners: ArrayBuffer[Vector2 => Unit] = ArrayBuffer()

  /** List of functions to call when the mouse leaves this area.
   * Each listener receives the last mouse position before leaving.
   */
  private val mouseLeaveListeners: ArrayBuffer[Vector2 => Unit] = ArrayBuffer()

  /** List of functions to call when a mouse button is pressed while over this area.
   * Each listener receives the click position and the button code.
   */
  private val mousePressedListeners: ArrayBuffer[(Vector2, Int) => Unit] = ArrayBuffer()

  /** List of functions to call when a mouse button is released while over this area.
   * Each listener receives the release position and the button code.
   */
  private val mouseReleasedListeners: ArrayBuffer[(Vector2, Int) => Unit] = ArrayBuffer()

  // Register with the global InputManager to receive raw mouse events,
  // then delegate to local handlers (mouseMoved, mousePressed, mouseReleased).
  private val _mouseMovedRef: Vector2 => Unit = mouseMoved
  private val _mousePressedRef: (Vector2, Int) => Unit = mousePressed
  private val _mouseReleasedRef: (Vector2, Int) => Unit = mouseReleased

  InputManager.bindMouseMoved(_mouseMovedRef)
  InputManager.bindMousePressed(_mousePressedRef)
  InputManager.bindMouseReleased(_mouseReleasedRef)

  /** Tracks whether the mouse is currently over this area. */
  private var _isMouseOver: Boolean = false

  /**
   * Checks if this area intersects another Area2D.
   * Concrete subclasses must implement shape-specific logic.
   *
   * @param other Another Area2D instance to test intersection against.
   * @return True if the two areas overlap.
   */
  def intersects(other: Area2D): Boolean = {
    areaType match {
      case Area2D.Circle =>
        other.areaType match {
          case Area2D.Circle =>
            // Compare squared distance between centers to squared sum of radii
            val dist2 = pos.dst2(other.pos)
            dist2 <= (width + other.width) * (width + other.width)

          case Area2D.Box =>
            // demi-largeur / demi-hauteur du rectangle
            val halfW = other.width  / 2f
            val halfH = other.height / 2f
            // clamp du centre du cercle aux bords du rectangle
            val closestX = math.max(other.pos.x - halfW, math.min(pos.x, other.pos.x + halfW))
            val closestY = math.max(other.pos.y - halfH, math.min(pos.y, other.pos.y + halfH))
            // test de distance cercle-rectangle
            val dx = pos.x - closestX
            val dy = pos.y - closestY
            dx*dx + dy*dy <= width * width

          case _ => false
        }
      case Area2D.Box =>
        other.areaType match {
          case Area2D.Circle =>
            other.intersects(this)

          case Area2D.Box =>
            val halfW1 = width  / 2f
            val halfH1 = height / 2f
            val halfW2 = other.width  / 2f
            val halfH2 = other.height / 2f

            math.abs(pos.x - other.pos.x) <= (halfW1 + halfW2) &&
              math.abs(pos.y - other.pos.y) <= (halfH1 + halfH2)

          case _ => false
        }
    }
  }

  /**
   * Checks if a world-coordinate point is contained within this area.
   * Concrete subclasses must implement shape-specific logic.
   *
   * @param p The point to test (world coordinates).
   * @return True if the point lies inside this area.
   */
  def containsPoint(p: Vector2): Boolean = {
    areaType match {
      case Area2D.Circle =>
        p.cpy().sub(pos).len() <= width


      case Area2D.Box =>
        val halfW = width  / 2f
        val halfH = height / 2f
        p.x >= pos.x - halfW && p.x <= pos.x + halfW &&
        p.y >= pos.y - halfH && p.y <= pos.y + halfH
    }
  }

  /**
   * Convenience method: returns true if the given mouse position is inside this area.
   *
   * @param mousePos The mouse position (screenX, screenY) as a Vector2.
   * @return True if the mouse is over this area.
   */
  def isMouseOver(mousePos: Vector2): Boolean = containsPoint(mousePos)

  /**
   * Called on every mouse-move event. Determines whether the mouse
   * has entered or left this area since the last update, and triggers
   * the corresponding callbacks.
   *
   * @param mousePos The current mouse position (screenX, screenY).
   */
  protected def mouseMoved(mousePos: Vector2): Unit = {
    val mouseState = isMouseOver(mousePos)
    //println("mouse state: " + mouseState + ", pos: " + pos)
    if (mouseState != _isMouseOver) {
      _isMouseOver = mouseState
      if (_isMouseOver)
        mouseEntered(mousePos)
      else
        mouseLeft(mousePos)
    }
    if (!_isMouseOver)
      return
  }

  /** Register a listener to be notified when the mouse enters this area.
   *
   * @param listener A function that takes mousePos: Vector2 and returns Unit.
   */
  def bindMouseEntered(listener: Vector2 => Unit): Unit = {
    mouseEnterListeners += listener
  }

  /** Register a listener to be notified when the mouse leaves this area.
   *
   * @param listener A function that takes mousePos: Vector2 and returns Unit.
   */
  def bindMouseLeft(listener: Vector2 => Unit): Unit = {
    mouseLeaveListeners += listener
  }

  /** Register a listener to be notified when a mouse button is pressed over this area.
   *
   * @param listener A function that takes (mousePos: Vector2, button: Int) and returns Unit.
   */
  def bindMousePressed(listener: (Vector2, Int) => Unit): Unit = {
    mousePressedListeners += listener
  }

  /** Register a listener to be notified when a mouse button is released over this area.
   *
   * @param listener A function that takes (mousePos: Vector2, button: Int) and returns Unit.
   */
  def bindMouseReleased(listener: (Vector2, Int) => Unit): Unit = {
    mouseReleasedListeners += listener
  }

  /** Invoke all registered mouse-enter callbacks with the given position.
   *
   * @param mousePos The position at which the mouse entered.
   */
  protected def onMouseEntered(mousePos: Vector2): Unit = {
    mouseEnterListeners.toArray.foreach(_(mousePos))
  }

  /** Invoke all registered mouse-leave callbacks with the given position.
   *
   * @param mousePos The position at which the mouse left.
   */
  protected def onMouseLeft(mousePos: Vector2): Unit = {
    mouseLeaveListeners.toArray.foreach(_(mousePos))
  }

  /** Invoke all registered mouse-press callbacks if the mouse is currently over this area.
   *
   * @param mousePos   The click position.
   * @param mouseButton The button code that was pressed.
   */
  protected def onMousePressed(mousePos: Vector2, mouseButton: Int): Unit = {
    mousePressedListeners.toArray.foreach(_(mousePos, mouseButton))
  }

  /** Invoke all registered mouse-release callbacks if the mouse is currently over this area.
   *
   * @param mousePos   The release position.
   * @param mouseButton The button code that was released.
   */
  protected def onMouseReleased(mousePos: Vector2, mouseButton: Int): Unit = {
    mouseReleasedListeners.toArray.foreach(_(mousePos, mouseButton))
  }

  /** Invoke all registered mouse-enter callbacks with the given position.
   *
   * @param mousePos The position at which the mouse entered.
   */
  private def mouseEntered(mousePos: Vector2): Unit = {
      onMouseEntered(mousePos)
  }

  /** Invoke all registered mouse-leave callbacks with the given position.
   *
   * @param mousePos The position at which the mouse left.
   */
  private def mouseLeft(mousePos: Vector2): Unit = {
      onMouseLeft(mousePos)
  }

  /** Invoke all registered mouse-press callbacks if the mouse is currently over this area.
   *
   * @param mousePos   The click position.
   * @param mouseButton The button code that was pressed.
   */
  private def mousePressed(mousePos: Vector2, mouseButton: Int): Unit = {
    if (isMouseOver(mousePos))
      onMousePressed(mousePos, mouseButton)
  }

  /** Invoke all registered mouse-release callbacks if the mouse is currently over this area.
   *
   * @param mousePos   The release position.
   * @param mouseButton The button code that was released.
   */
  private def mouseReleased(mousePos: Vector2, mouseButton: Int): Unit = {
    if (isMouseOver(mousePos))
      onMouseReleased(mousePos, mouseButton)
  }


  protected def unbindEvents(): Unit = {
    mouseEnterListeners.clear()
    mouseLeaveListeners.clear()
    mousePressedListeners.clear()
    mouseReleasedListeners.clear()
    InputManager.unbindMouseMoved(_mouseMovedRef)
    InputManager.unbindMousePressed(_mousePressedRef)
    InputManager.unbindMouseReleased(_mouseReleasedRef)
  }

}



