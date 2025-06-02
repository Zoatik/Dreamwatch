package scripts.World.Physics

import com.badlogic.gdx.math.Vector2
import scripts.World.Actors.Base.Object2D
import scripts.World.Scene2D


trait Movement2D { self: Object2D =>
  //Scene2D.addToCurrentScene(this)

  protected var _speed: Vector2 = new Vector2(0,0)


  def speed: Vector2 = _speed

  def speed_=(newSpeed: Vector2): Unit = _speed = newSpeed

  def speed_=(newSpeed: Float): Unit = {
    val absSpeed: Float = _speed.len()
    val multFactor: Float = newSpeed / absSpeed
    _speed.x = _speed.x * multFactor
    _speed.y = _speed.y * multFactor
  }

  override def destroy(): Unit = {
    self.destroy()
    Scene2D.removeFromCurrentScene(this)
  }

  def move(deltaT: Float): Unit = {
    pos.x += speed.x * deltaT
    pos.y += speed.y * deltaT
  }

}