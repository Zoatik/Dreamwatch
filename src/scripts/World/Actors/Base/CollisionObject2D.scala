package scripts.World.Actors.Base

import com.badlogic.gdx.math.Vector2
import scripts.Sprite
import scripts.World.Physics.{Area2D, Collider2D}

class CollisionObject2D(pos: Vector2, sprite: Sprite,
                        graphicLayerZ: Int,
                        override val collisionArea2D: Area2D,
                        override val parent: Object2D,
                        override var collisionLayerZ: Int)
  extends Object2D(pos, sprite, graphicLayerZ) with Collider2D{

}