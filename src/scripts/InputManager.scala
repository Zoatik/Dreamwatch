package scripts

import com.badlogic.gdx.{Gdx, InputAdapter}
import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ArrayBuffer

/**
 * Singleton object that centralizes input event handling.
 * It maintains collections of listeners for various mouse and keyboard events
 * and dispatches incoming events to all registered listeners.
 */
object InputManager extends InputAdapter {

  /** List of functions to call when the mouse is moved.
   * Each listener receives the new mouse position as a Vector2(screenX, screenY).
   */
  private val mouseMotionListeners: ArrayBuffer[Vector2 => Unit] = ArrayBuffer()

  /** List of functions to call when a mouse button is pressed.
   * Each listener receives the click position as Vector2(screenX, screenY)
   * and the button code (e.g., Input.Buttons.LEFT).
   */
  private val mousePressedListeners: ArrayBuffer[(Vector2, Int) => Unit] = ArrayBuffer()

  /** List of functions to call when a mouse button is released.
   * Each listener receives the release position as Vector2(screenX, screenY)
   * and the button code.
   */
  private val mouseReleasedListeners: ArrayBuffer[(Vector2, Int) => Unit] = ArrayBuffer()

  /** List of functions to call when a keyboard key is pressed.
   * Each listener receives the key code (e.g., Input.Keys.SPACE).
   */
  private val keyPressedListeners: ArrayBuffer[Int => Unit] = ArrayBuffer()

  /** List of functions to call when a keyboard key is released.
   * Each listener receives the key code.
   */
  private val keyReleasedListeners: ArrayBuffer[Int => Unit] = ArrayBuffer()

  /**
   * Called by LibGDX whenever the mouse moves.
   * Delegates the event to all registered mouse motion listeners.
   *
   * @param screenX The x-coordinate of the mouse on the screen (pixels from left).
   * @param screenY The y-coordinate of the mouse on the screen (pixels from top).
   * @return Always returns true to mark the event as handled.
   */
  override def mouseMoved(screenX: Int, screenY: Int): Boolean = {
    // Notify every listener of the new mouse position
    val transformedY: Int = Gdx.graphics.getHeight - screenY
    mouseMotionListeners.toArray.foreach(_(new Vector2(screenX, transformedY)))
    true
  }

  /**
   * Called by LibGDX when a mouse button is pressed (touch down).
   * Delegates the event to all registered mouse pressed listeners.
   *
   * @param screenX The x-coordinate of the touch or click (pixels from left).
   * @param screenY The y-coordinate of the touch or click (pixels from top).
   * @param pointer Unused pointer index (always 0 for a single mouse).
   * @param button  The button code that was pressed (e.g., Input.Buttons.LEFT).
   * @return Always returns true to mark the event as handled.
   */
  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    // Notify every listener of the click position and button code
    val transformedY: Int = Gdx.graphics.getHeight - screenY
    mousePressedListeners.toArray.foreach(_(new Vector2(screenX, transformedY), button))
    true
  }

  /**
   * Called by LibGDX when a mouse button is released (touch up).
   * Delegates the event to all registered mouse released listeners.
   *
   * @param screenX The x-coordinate of the release (pixels from left).
   * @param screenY The y-coordinate of the release (pixels from top).
   * @param pointer Unused pointer index.
   * @param button  The button code that was released.
   * @return Always returns true to mark the event as handled.
   */
  override def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    // Notify every listener of the release position and button code
    val transformedY: Int = Gdx.graphics.getHeight - screenY
    mouseReleasedListeners.toArray.foreach(_(new Vector2(screenX, transformedY), button))
    true
  }

  /**
   * Called by LibGDX when a keyboard key is pressed.
   * Delegates the event to all registered key pressed listeners.
   *
   * @param keycode The code of the key that was pressed.
   * @return Always returns true to mark the event as handled.
   */
  override def keyDown(keycode: Int): Boolean = {
    // Notify every listener of the pressed key code
    keyPressedListeners.toArray.foreach(_(keycode))
    true
  }

  /**
   * Called by LibGDX when a keyboard key is released.
   * Delegates the event to all registered key released listeners.
   *
   * @param keycode The code of the key that was released.
   * @return Always returns true to mark the event as handled.
   */
  override def keyUp(keycode: Int): Boolean = {
    // Notify every listener of the released key code
    keyReleasedListeners.toArray.foreach(_(keycode))
    true
  }

  /**
   * Register a listener to be notified whenever the mouse moves.
   *
   * @param listener A function that takes a Vector2 (screenX, screenY) and returns Unit.
   */
  def onMouseMoved(listener: Vector2 => Unit): Unit = {
    mouseMotionListeners += listener
  }

  /**
   * Register a listener to be notified whenever a mouse button is pressed.
   *
   * @param listener A function that takes a Vector2 (screenX, screenY) and an Int (button code).
   */
  def onMousePressed(listener: (Vector2, Int) => Unit): Unit = {
    mousePressedListeners += listener
  }

  /**
   * Register a listener to be notified whenever a mouse button is released.
   *
   * @param listener A function that takes a Vector2 (screenX, screenY) and an Int (button code).
   */
  def onMouseReleased(listener: (Vector2, Int) => Unit): Unit = {
    mouseReleasedListeners += listener
  }

  /**
   * Register a listener to be notified whenever a keyboard key is pressed.
   *
   * @param listener A function that takes an Int (key code) and returns Unit.
   */
  def onKeyPressed(listener: Int => Unit): Unit = {
    keyPressedListeners += listener
  }

  /**
   * Register a listener to be notified whenever a keyboard key is released.
   *
   * @param listener A function that takes an Int (key code) and returns Unit.
   */
  def onKeyReleased(listener: Int => Unit): Unit = {
    keyReleasedListeners += listener
  }
}
