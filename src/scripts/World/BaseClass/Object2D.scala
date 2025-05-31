package scripts.World.BaseClass

import scripts.Sprite

class Object2D(var sprite: Sprite, var area2D: Area2D) {

}

trait drawable {
  var sprite: Sprite
}
