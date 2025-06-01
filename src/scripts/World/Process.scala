package scripts.World

trait Process[T] {
  protected var _isPaused: Boolean = false
  def isPaused = _isPaused
  def init(): Unit
  def update(deltaT: Float, arg: T): Unit
  def pause(): Unit = if (!isPaused) _isPaused = true
  def resume(): Unit = if (isPaused) _isPaused = false
}
