package scripts.World.Physics

import com.badlogic.gdx.math.Vector2
import scripts.World.Actors.Base.Object2D

/**
 * Trait that adds movement behavior to any Object2D in the world.
 * Provides speed, target tracking, and different trajectory styles.
 */
trait Movement2D {

  def pos: Vector2
  def pos_=(newPos: Vector2): Unit
  /** Speed scalar in units per second. */
  var speed: Float

  /** Target position toward which the object moves. */
  var target: Vector2

  /** If true, stops movement once the target is reached. */
  var stopAtTarget: Boolean = true

  /** Chosen trajectory style for movement. */
  var trajectory: Movement2D.Trajectory = Movement2D.Linear

  private var _targetReached = false

  private var elapsedTime: Float = 0f

  private val sineAmplitude: Float = 1f
  private val sineFrequency: Float = 1f

  private var spiralAngularSpeed: Float = 0f

  /**
   * Initialize the movement settings, setting a new trajectory and
   * computing an angular speed for spiral motion based on the target distance.
   *
   * @param newTrajectory The trajectory style to use (Linear, Sinus, Spiral).
   */
  def initMovement(newTrajectory: Movement2D.Trajectory): Unit = {
    trajectory = newTrajectory
    spiralAngularSpeed = target.cpy().sub(pos).len() * 1f
  }

  /**
   * Move the object according to its speed, target, and trajectory style.
   * Should be called each frame with the elapsed time.
   *
   * @param deltaT Time elapsed since the last update (in seconds).
   */
  def move(deltaT: Float): Unit = {
    elapsedTime += deltaT

    if (target == null) return
    if (stopAtTarget && targetReached()) return

    val toTarget = target.cpy().sub(pos)
    if (toTarget.isZero) return
    val unitToTarget = toTarget.nor()

    val direction: Vector2 = trajectory match {
      case Movement2D.Linear =>
        unitToTarget
      case Movement2D.Sinus =>
        val baseDir = unitToTarget.cpy()
        val perp = new Vector2(-baseDir.y, baseDir.x)
        val phase = elapsedTime * sineFrequency * (2 * Math.PI).toFloat
        val offset = perp.scl((Math.sin(phase) * sineAmplitude).toFloat)
        baseDir.add(offset).nor()
      case Movement2D.Spiral =>
        val radiusVec = pos.cpy().sub(target)
        val radius = radiusVec.len()
        if (radius == 0f) new Vector2(0, 0)
        else {
          val radialDir = radiusVec.cpy().nor()
          val tangential = new Vector2(-radialDir.y, radialDir.x)
          val angleDeg = spiralAngularSpeed * elapsedTime
          val inwardDir = target.cpy().sub(pos).nor()
          val spiraled = inwardDir.rotateDeg(angleDeg)
          spiraled.cpy().add(radialDir).scl(-1).nor()
        }
      case _ => unitToTarget
    }

    val deltaXY = direction.scl(speed * deltaT)
    pos.x += deltaXY.x
    pos.y += deltaXY.y

    if (!_targetReached && targetReached()) {
      onTargetReached()
      _targetReached = true
    }
    if (_targetReached && !targetReached()) {
      onTargetLeft()
      _targetReached = false
    }
  }

  /**
   * Check whether the object has reached its target within a tolerance.
   *
   * @param eps Distance tolerance in world units (default 10.0f).
   * @return True if the target is within eps of the current position.
   */
  private def targetReached(eps: Float = 10.0f): Boolean = {
    target.cpy().sub(pos).len() < eps
  }

  /**
   * Callback invoked once when the target is reached for the first time.
   * Can be overridden to perform custom logic.
   */
  protected def onTargetReached(): Unit = {}

  /**
   * Callback invoked once when the object moves away after having reached the target.
   * Can be overridden to perform custom logic.
   */
  protected def onTargetLeft(): Unit = {}
}

/**
 * Companion object defining possible trajectory styles for Movement2D.
 */
object Movement2D {
  /** Base trait for trajectory types. */
  sealed trait Trajectory

  /** Straight-line motion toward the target. */
  case object Linear extends Trajectory

  /** Sinusoidal oscillation around the straight path. */
  case object Sinus extends Trajectory

  /** Spiral path converging on the target over time. */
  case object Spiral extends Trajectory
}