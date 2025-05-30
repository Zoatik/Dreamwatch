import com.badlogic.gdx.math.Vector2


// TODO : trouver une manière élégante de séparer les objets avec et sans collision
class CollisionObject(var position: Vector2, var shape: String) {
}

trait collider {
  var position: Vector2
  var shape: String
}

trait drawable {
  var sprite: Sprite
}

class toto(var position: Vector2, var shape: String) extends collider with drawable{

  override var sprite: Sprite = ???
}