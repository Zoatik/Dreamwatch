package scripts.game.actors.instantiables

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.instantiables.UiElement
import scripts.dreamwatch_engine.physics.Area2D
import scripts.game.GameManager
import scripts.game.actors.abstracts.Weapon
import scripts.game.actors.instantiables.Card.{CardLeft, CardMiddle, CardRight, cardAction, destroy3Cards}
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

class Card(pos: Vector2 = Globals.CARDS_POS(1),
           cardType: Card.Type,
           cardUpgarde: String,
           image: ArrayBuffer[String] = ArrayBuffer("src/res/sprites/basicCard.png"),
           gLayerZ: Int = Globals.CARD_GLAYERZ,
           area2D: Area2D.type = Globals.CARD_AREA2D)
  extends UiElement(
    pos,
    0,
    image,
    gLayerZ,
    area2D.Box){

  private var counter: Int = 0

  override def instantiate(): Card = {
    super.instantiate()
    this
  }

  override protected def onMouseReleased(mousePos: Vector2, mouseButton: Int): Unit = {
    super.onMouseReleased(mousePos, mouseButton)
    //println(s"card clicked : ${isMouseOver(mousePos)}")
    cardAction(this.cardType)
    destroy3Cards()
    GameManager.currentScene.asInstanceOf[GameScene].cardsSelectionDone = true
  }
  override protected def onMouseEntered(mousePos: Vector2): Unit = {
    super.onMouseEntered(mousePos)
    counter += 1
    println(s"YOuu have entered the primitive card's territory. Now it's the ${this.cardType}")
  }

}

object Card {
  val cards: Array[Card] = Array.ofDim(3)

  def create3Cards(): Unit = {
    val u: Array[String] = generate3Upgrades()
    println(u(0))
    println(u(1))
    println(u(2))
    cards(0) = new Card(Globals.CARDS_POS(0), CardLeft,u(0)).instantiate()
    cards(1) = new Card(Globals.CARDS_POS(1), CardMiddle, u(1)).instantiate()
    cards(2) = new Card(Globals.CARDS_POS(2), CardRight, u(2)).instantiate()
  }
  def destroy3Cards(): Unit = {
    cards(0).destroy()
    cards(1).destroy()
    cards(2).destroy()

    //cards = Array.empty
  }
  def generate3Upgrades(): Array[String] = {
    // Gonna need like a holster with the info of all the weapons
    val holster: Array[String] = Array("up1", "up2", "up3", "up4", "up5", "up6")
    val upgrades: Array[String] = Array.ofDim(3)
    for(i <- upgrades.indices){
      upgrades(i) = holster(random.nextInt(0, holster.length))
    }
    upgrades
  }
  def cardAction(cardType: Card.Type): Unit = {
    cardType match {
      case CardLeft => println("Clicked on left card")
      case CardMiddle => println("Clicked on middle card")
      case CardRight => println("Clicked on right card")
      case _ =>
    }
  }


  sealed trait Type

  case object CardLeft extends Type
  case object CardMiddle extends Type
  case object CardRight extends Type
}