package scripts.World.Actors.Base

import scripts.World.Physics.{Area2D, Collider2D}

class CollisionEntity(override val collisionArea2D: Area2D,
                      override val parent: Object2D,
                      override var collisionLayerZ: Int)
  extends Entity with Collider2D{

  override def destroy(): Unit = super.destroy()
}
