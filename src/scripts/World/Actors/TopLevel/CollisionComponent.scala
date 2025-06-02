package scripts.World.Actors.TopLevel

import scripts.World.Actors.Base.{Entity, Object2D}
import scripts.World.Physics.{Area2D, Collider2D}

class CollisionComponent(override val collisionArea2D: Area2D,
                      override val parent: Object2D,
                      override var collisionLayerZ: Int)
  extends Entity with Collider2D{

}
