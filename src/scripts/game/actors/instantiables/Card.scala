package scripts.game.actors.instantiables

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.instantiables.UiElement
import scripts.dreamwatch_engine.physics.Area2D
import scripts.game.GameManager
import scripts.game.actors.abstracts.Weapon
import scripts.game.actors.abstracts.Weapon.{Holster, Upgrade}
import scripts.game.actors.instantiables.Card.destroy3Cards
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

class Card(pos: Vector2 = Globals.CARDS_POS(1),
           var cardHolster: Holster,
           image: ArrayBuffer[String] = ArrayBuffer("src/res/sprites/card.png"),
           gLayerZ: Int = Globals.CARD_GLAYERZ,
           area2D: Area2D.type = Globals.CARD_AREA2D)
  extends UiElement(
    pos,
    0,
    image,
    gLayerZ,
    area2D.Box){

  width = 400.0f
  val cardHighlight: UiElement = new UiElement(pos, 0, ArrayBuffer("src/res/sprites/cardHighlight.png"), 2, Area2D.Box)

  override def instantiate(): Card = {
    super.instantiate()
    this
  }
  override protected def onMouseReleased(mousePos: Vector2, mouseButton: Int): Unit = {
    super.onMouseReleased(mousePos, mouseButton)
    val weapon = GameManager.currentScene.asInstanceOf[GameScene].player.weapon

    if(cardHolster.isInstanceOf[Weapon.Upgrade])
      weapon.weaponUpgrades += this.cardHolster.asInstanceOf[Weapon.Upgrade]

    else
      weapon.weaponEvolution = this.cardHolster.asInstanceOf[Weapon.Evolution]

    destroy3Cards()
  }
  override protected def onMouseEntered(mousePos: Vector2): Unit = {
    super.onMouseEntered(mousePos)
    //cardHighlight.instantiate()
  }

  override protected def onMouseLeft(mousePos: Vector2): Unit = {
    super.onMouseLeft(mousePos)
    //cardHighlight.destroy()
  }
}

object Card {
  val cards: Array[Card] = Array.ofDim(3)

  def create3Cards(): Unit = {
    val u: Array[Upgrade] = generate3Upgrades()
    cards(0) = new Card(Globals.CARDS_POS(0), u(0)).instantiate()
    cards(1) = new Card(Globals.CARDS_POS(1), u(1)).instantiate()
    cards(2) = new Card(Globals.CARDS_POS(2), u(2)).instantiate()
  }
  def destroy3Cards(): Unit = {
    cards(0).destroy()
    cards(1).destroy()
    cards(2).destroy()
    GameManager.currentScene.asInstanceOf[GameScene].cardsSelectionDone = true
  }
  def generate3Upgrades(): Array[Upgrade] = {
    val upPos: ArrayBuffer[Upgrade] = GameManager.currentScene.asInstanceOf[GameScene].player.weapon.upgradePossibilities.clone().to(ArrayBuffer)
    val cardUp: Array[Upgrade] = Array.ofDim(3)
    for(i <- cardUp.indices){
      cardUp(i) = upPos(random.nextInt(0, upPos.length))
      upPos -= cardUp(i)
      println(s"Upgrade $i is ${cardUp(i)}")
    }
    cardUp
  }
}