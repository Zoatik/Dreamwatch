package scripts.World.Physics

import com.badlogic.gdx.math.Vector2
import scripts.World.Actors.Base.Object2D


trait Movement2D { self: Object2D =>

  var speed: Float
  var target: Vector2
  var stopAtTarget: Boolean = true
  var trajectory: Movement2D.Trajectory = Movement2D.Linear
  private var _targetReached = false

  // For sinusoidal and spiral motions, we keep track of elapsed time
  private var elapsedTime: Float = 0f

  // Parameters for sinusoidal motion
  private val sineAmplitude: Float = 1f      // maximum offset in pixels
  private val sineFrequency: Float = 1f       // oscillations per second

  // Parameter for spiral motion
  private var spiralAngularSpeed: Float = 0f// degrees per second

  def initMovement(newTrajectory: Movement2D.Trajectory): Unit = {
    trajectory = newTrajectory
    spiralAngularSpeed = target.cpy.sub(pos).len() * 1f
  }

  def move(deltaT: Float): Unit = {
    elapsedTime += deltaT

    if (target == null) return
    if (stopAtTarget && targetReached()) return

    // Base direction toward target
    val toTarget = target.cpy().sub(pos)
    if (toTarget.isZero) return
    val unitToTarget = toTarget.nor()

    // Compute “direction” based on chosen trajectory
    val direction: Vector2 = trajectory match {
      case Movement2D.Linear =>
        // Straight line
        unitToTarget

      case Movement2D.Sinus =>
        // 1. Primary direction toward target
        val baseDir = unitToTarget.cpy()
        // 2. Perpendicular (to oscillate sideways)
        val perp = new Vector2(-baseDir.y, baseDir.x)
        // 3. Phase of sine wave
        val phase = elapsedTime * sineFrequency * (2 * Math.PI).toFloat
        val offset = perp.scl((Math.sin(phase) * sineAmplitude).toFloat)
        // 4. Combine and normalize
        baseDir.add(offset).nor()

      case Movement2D.Spiral =>
        // 1. Distance to target
        val radiusVec = pos.cpy().sub(target)
        val radius = radiusVec.len()
        if (radius == 0f) {
          // Already at target center; just stay put
          new Vector2(0, 0)
        } else {
          // 2. Radial unit (pointing outward from target)
          val radialDir = radiusVec.cpy().nor()
          // 3. Tangential (perpendicular to radial, for circular component)
          val tangential = new Vector2(-radialDir.y, radialDir.x)
          // 4. Spiral combines radial (inward) and tangential
          //    We want to move “inward” toward the target while also circling:
          //    - Inward component: (target - pos).nor() which is -radialDir
          //    - Angular rotation amount based on elapsedTime & speed
          val angleDeg = spiralAngularSpeed * elapsedTime
          // Rotate the inward vector by angleDeg around its own origin
          val inwardDir = target.cpy().sub(pos).nor()
          val spiraled = inwardDir.rotateDeg(angleDeg)
          spiraled.cpy.add(radialDir).scl(-1).nor()
        }

      case _ =>
        // Fallback to straight line
        unitToTarget
    }

    // Update position
    val deltaXY = direction.scl(speed * deltaT)
    pos.x += deltaXY.x
    pos.y += deltaXY.y

    // Handle “reached” callbacks
    if (!_targetReached && targetReached()) {
      onTargetReached()
      _targetReached = true
    }
    if (_targetReached && !targetReached()) {
      onTargetLeft()
      _targetReached = false
    }
  }

  private def targetReached(eps: Float = 10.0f): Boolean = {
    target.cpy().sub(pos).len() < eps
  }

  protected def onTargetReached(): Unit = {}
  protected def onTargetLeft(): Unit = {}
}


object Movement2D {
  sealed trait Trajectory
  case object Linear extends Trajectory
  case object Sinus extends Trajectory
  case object Spiral extends Trajectory
}