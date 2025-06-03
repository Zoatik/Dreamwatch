package scripts.World.Actors.Base

import com.badlogic.gdx.math.Vector2
import scripts.Managers.{CollisionsManager, ScenesManager}
import scripts.Sprite
import scripts.World.Physics.{Area2D, BoxArea2D, CircleArea2D, Collider2D}

abstract class CollisionObject2D(pos: Vector2, sprite: Sprite,
                                 graphicLayerZ: Int,
                                 override val collisionArea2D: Area2D,
                                 override var collisionLayerZ: Int,
                                 lifeTime: Option[Float] = None,
                                 override val parent: Object2D = null)
  extends Object2D(pos, sprite, graphicLayerZ, lifeTime) with Collider2D{

  private val spriteWidth = collisionArea2D match {
    case c: CircleArea2D => 2 * c.radius
    case b: BoxArea2D => b.width
  }
  sprite.setWidth(spriteWidth)

  override def spawn(): Entity = {
    CollisionsManager.checkAndNotifyCollisions(this, ScenesManager.currentScene.getCollisionLayers.get(this.collisionLayerZ).get)
    super.spawn()
  }
}