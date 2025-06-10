package scripts.game.actors.instantiables

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.instantiables.UiElement
import scripts.dreamwatch_engine.physics.Area2D
import scripts.game.GameManager
import scripts.game.actors.instantiables.Card.{CardLeft, CardMiddle, CardRight, destroy3Cards}
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

class Card(pos: Vector2 = Globals.CARDS_DEFAULT_POS(1),
           cardType: Card.Type,
           image: ArrayBuffer[BitmapImage] = ArrayBuffer(new BitmapImage("src/res/sprites/card.png")),
           gLayerZ: Int = Globals.CARD_GLAYERZ,
           area2D: Area2D.type = Globals.CARD_AREA2D)
  extends UiElement(pos,
    image,
    gLayerZ,
    area2D.Box){

  private var counter: Int = 0

  override def instantiate(): Card = {
    super.instantiate()
    this
  }

  override protected def mouseReleased(mousePos: Vector2, mouseButton: Int): Unit = {
    super.mouseReleased(mousePos, mouseButton)
    println(s"card clicked : ${isMouseOver(mousePos)}")
    this.cardType match {
      case Card.CardLeft => println("Clicked on left card")
      case Card.CardMiddle => println("Clicked on middle card")
      case Card.CardRight => println("Clicked on right card")
      case _ => println("All cards clicked")
    }
    destroy3Cards()
    GameManager.currentScene.asInstanceOf[GameScene].cardsSelectionDone = true
  }
  override protected def mouseEntered(mousePos: Vector2): Unit = {
    super.mouseEntered(mousePos)
    counter += 1
    println(s"YOuu have entered the primitive card's territory. Now it's the ${this.cardType}")
  }

}

object Card {
  var cards: Array[Card] = Array.ofDim(3)

  def create3Cards(): Unit = {
    cards(0) = new Card(Globals.CARDS_DEFAULT_POS(0), CardLeft).instantiate()
    cards(1) = new Card(Globals.CARDS_DEFAULT_POS(1), CardMiddle).instantiate()
    cards(2) = new Card(Globals.CARDS_DEFAULT_POS(2), CardRight).instantiate()
  }
  def destroy3Cards(): Unit = {
    cards(0).destroy()
    cards(1).destroy()
    cards(2).destroy()

    //cards = Array.empty
  }
  /*def cardClicked(cardType: Card.Type): Unit = {
    cardType match {
      case CardLeft => println("Clicked on left card")
      case CardMiddle => println("Clicked on middle card")
      case CardRight => println("Clicked on right card")
      case _ =>
    }
  }*/
  // Could directly do the action in the cardClicked function..
  /*def cardAction(cardTpye: Card.Type): Unit = {
    cardTpye match {
      case CardLeft =>
      case CardMiddle =>
      case CardRight =>
      case _ =>
    }
  }*/


  sealed trait Type

  case object CardLeft extends Type
  case object CardMiddle extends Type
  case object CardRight extends Type
}