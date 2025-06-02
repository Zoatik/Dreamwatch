package scripts.World.Physics

import com.badlogic.gdx.math.Vector2
import scripts.InputManager

import scala.collection.mutable.ArrayBuffer

/**
 * Abstract base class representing a 2D area with mouse interaction capabilities.
 * Provides collision methods to be implemented by concrete shapes, and manages
 * mouse-enter, mouse-leave, mouse-press, and mouse-release listeners.
 *
 * @param pos The position (typically center or corner) of this Area2D in world coordinates.
 */
abstract class Area2D(var pos: Vector2) {

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
  InputManager.onMouseMoved(mousePos => mouseMoved(mousePos))
  InputManager.onMousePressed((mousePos, mouseButton) => mousePressed(mousePos, mouseButton))
  InputManager.onMouseReleased((mousePos, mouseButton) => mouseReleased(mousePos, mouseButton))

  /** Tracks whether the mouse is currently over this area. */
  private var _isMouseOver: Boolean = false

  /**
   * Checks if this area intersects another Area2D.
   * Concrete subclasses must implement shape-specific logic.
   *
   * @param other Another Area2D instance to test intersection against.
   * @return True if the two areas overlap.
   */
  def intersects(other: Area2D): Boolean

  /**
   * Checks if a world-coordinate point is contained within this area.
   * Concrete subclasses must implement shape-specific logic.
   *
   * @param p The point to test (world coordinates).
   * @return True if the point lies inside this area.
   */
  def containsPoint(p: Vector2): Boolean

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
  def mouseMoved(mousePos: Vector2): Unit = {
    val mouseState = isMouseOver(mousePos)
    if (mouseState != _isMouseOver) {
      _isMouseOver = mouseState
      if (_isMouseOver)
        mouseEntered(mousePos)
      else
        mouseLeft(mousePos)
    }
  }

  /** Register a listener to be notified when the mouse enters this area.
   *
   * @param listener A function that takes mousePos: Vector2 and returns Unit.
   */
  def onMouseEntered(listener: Vector2 => Unit): Unit = {
    mouseEnterListeners += listener
  }

  /** Register a listener to be notified when the mouse leaves this area.
   *
   * @param listener A function that takes mousePos: Vector2 and returns Unit.
   */
  def onMouseLeft(listener: Vector2 => Unit): Unit = {
    mouseLeaveListeners += listener
  }

  /** Register a listener to be notified when a mouse button is pressed over this area.
   *
   * @param listener A function that takes (mousePos: Vector2, button: Int) and returns Unit.
   */
  def onMousePressed(listener: (Vector2, Int) => Unit): Unit = {
    mousePressedListeners += listener
  }

  /** Register a listener to be notified when a mouse button is released over this area.
   *
   * @param listener A function that takes (mousePos: Vector2, button: Int) and returns Unit.
   */
  def onMouseReleased(listener: (Vector2, Int) => Unit): Unit = {
    mouseReleasedListeners += listener
  }

  /** Invoke all registered mouse-enter callbacks with the given position.
   *
   * @param mousePos The position at which the mouse entered.
   */
  protected def mouseEntered(mousePos: Vector2): Unit = {
    mouseEnterListeners.toArray.foreach(_(mousePos))
  }

  /** Invoke all registered mouse-leave callbacks with the given position.
   *
   * @param mousePos The position at which the mouse left.
   */
  protected def mouseLeft(mousePos: Vector2): Unit = {
    mouseLeaveListeners.toArray.foreach(_(mousePos))
  }

  /** Invoke all registered mouse-press callbacks if the mouse is currently over this area.
   *
   * @param mousePos   The click position.
   * @param mouseButton The button code that was pressed.
   */
  protected def mousePressed(mousePos: Vector2, mouseButton: Int): Unit = {
    if (isMouseOver(mousePos))
      mousePressedListeners.toArray.foreach(_(mousePos, mouseButton))
  }

  /** Invoke all registered mouse-release callbacks if the mouse is currently over this area.
   *
   * @param mousePos   The release position.
   * @param mouseButton The button code that was released.
   */
  protected def mouseReleased(mousePos: Vector2, mouseButton: Int): Unit = {
    if (isMouseOver(mousePos))
      mouseReleasedListeners.toArray.foreach(_(mousePos, mouseButton))
  }
}

/**
 * Concrete implementation of Area2D representing a circle.
 *
 * @param pos    Center position of the circle.
 * @param radius Radius of the circle.
 */
class CircleArea2D(pos: Vector2, var radius: Float) extends Area2D(pos) {

  /**
   * Checks intersection between this circle and another Area2D.
   * Supports circle–circle and circle–axis-aligned box collisions.
   */
  override def intersects(other: Area2D): Boolean = other match {
    case c: CircleArea2D =>
      // Compare squared distance between centers to squared sum of radii
      val dist2 = pos.dst2(c.pos)
      dist2 <= (radius + c.radius) * (radius + c.radius)

    case r: BoxArea2D =>
      // Clamp circle center to rectangle bounds, then test distance
      val closestX = math.max(r.pos.x, math.min(pos.x, r.pos.x + r.width))
      val closestY = math.max(r.pos.y, math.min(pos.y, r.pos.y + r.height))
      val dx = pos.x - closestX
      val dy = pos.y - closestY
      dx * dx + dy * dy <= radius * radius

    case _ => false
  }

  /**
   * Checks if a point lies inside this circle.
   *
   * @param p The point to test (world coordinates).
   * @return True if the distance from p to center <= radius.
   */
  override def containsPoint(p: Vector2): Boolean = {
    p.sub(pos).len() <= radius
  }
}

/**
 * Concrete implementation of Area2D representing an axis-aligned rectangle.
 *
 * @param pos    Bottom-left corner of the rectangle.
 * @param width  Width of the rectangle.
 * @param height Height of the rectangle.
 */
class BoxArea2D(pos: Vector2, var width: Float, var height: Float) extends Area2D(pos) {

  /**
   * Checks intersection between this rectangle and another Area2D.
   * Supports rectangle–circle (delegates to circle logic) and rectangle–rectangle.
   */
  override def intersects(other: Area2D): Boolean = other match {
    case c: CircleArea2D =>
      c.intersects(this)

    case r: BoxArea2D =>
      val halfW1 = width  / 2f
      val halfH1 = height / 2f
      val halfW2 = r.width  / 2f
      val halfH2 = r.height / 2f

      math.abs(pos.x - r.pos.x) <= (halfW1 + halfW2) &&
      math.abs(pos.y - r.pos.y) <= (halfH1 + halfH2)

    case _ => false
  }

  /**
   * Checks if a point lies inside this rectangle.
   *
   * @param p The point to test (world coordinates).
   * @return True if p.x and p.y fall within the rectangle bounds.
   */
  override def containsPoint(p: Vector2): Boolean = {
    val halfW = width  / 2f
    val halfH = height / 2f
    p.x >= pos.x - halfW && p.x <= pos.x + halfW &&
    p.y >= pos.y - halfH && p.y <= pos.y + halfH
  }
}



