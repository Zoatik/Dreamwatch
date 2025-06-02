package scripts.World.Physics

import com.badlogic.gdx.math.Vector2
import scripts.Managers.SceneManager
import scripts.World.Actors.Base.Object2D


trait Movement2D { self: Object2D =>

  protected var _speed: Vector2


  def speed: Vector2 = _speed

  def speed_=(newSpeed: Vector2): Unit = _speed = newSpeed

  def speed_=(newSpeed: Float): Unit = {
    val absSpeed: Float = _speed.len()
    val multFactor: Float = newSpeed / absSpeed
    _speed.x = _speed.x * multFactor
    _speed.y = _speed.y * multFactor
  }

  def move(deltaT: Float): Unit = {
    pos.x += speed.x * deltaT
    pos.y += speed.y * deltaT
  }

}