package scripts.World.Actors.TopLevel.Abstract

import com.badlogic.gdx.math.Vector2
import scripts.World.Actors.BaseClass.Abstract.Entity
import scripts.World.Actors.TopLevel.Toy

abstract class Player extends Entity with Controller{
  private var dreamShards: Int = 0
  private var weapon: Toy = new Toy(new Vector2(0,0))

}
