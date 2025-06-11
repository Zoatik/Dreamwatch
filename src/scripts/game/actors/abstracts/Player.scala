package scripts.game.actors.abstracts

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Entity
import scripts.dreamwatch_engine.inputs.Controller
import scripts.game.actors.instantiables.Toy

abstract class Player extends Entity with Controller{
  private var dreamGems: Int = 0
  //private var weapon: Toy = new Toy(new Vector2(0,0))

}
