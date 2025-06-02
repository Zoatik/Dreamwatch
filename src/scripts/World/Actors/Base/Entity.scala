package scripts.World.Actors.Base

abstract class Entity {
  def destroy(): Unit

  def spawn(): Entity
}
