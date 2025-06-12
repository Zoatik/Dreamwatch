package scripts.game.actors.instantiables

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.instantiables.UiElement
import scripts.dreamwatch_engine.physics.Area2D
import scripts.game.{GameManager, MusicManager}
import scripts.game.actors.abstracts.Weapon
import scripts.game.actors.abstracts.Weapon.{Holster, Phase3, RebuildToy, UltimatePhase, Upgrade}
import scripts.game.actors.instantiables.Card.destroy3Cards
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

class Card(pos: Vector2 = Globals.CARDS_POS(1),
           var cardHolster: Holster,
           images: ArrayBuffer[String] = ArrayBuffer("src/res/sprites/ui/card.png"),
           gLayerZ: Int = Globals.CARD_GLAYERZ,
           area2D: Area2D.type = Globals.CARD_AREA2D)
  extends UiElement(
    pos,
    0,
    images,
    gLayerZ,
    area2D.Box){

  width = 400.0f
  val token: UiElement = new UiElement(pos.cpy().add(0,55),0, cardHolster.images, gLayerZ + 1, area2D.Box)
  token.width = 170.0f

  override def instantiate(): Card = {
    super.instantiate()
    token.instantiate()
    this
  }

  override def destroy(): Unit = {
    super.destroy()
    token.destroy()
  }

  override protected def onMouseReleased(mousePos: Vector2, mouseButton: Int): Unit = {
    super.onMouseReleased(mousePos, mouseButton)
    val weapon = GameManager.player.weapon.getOrElse(return )

    if(cardHolster.isInstanceOf[Weapon.Upgrade])
      weapon.weaponUpgrades += this.cardHolster.asInstanceOf[Weapon.Upgrade]

    else
      weapon.weaponEvolution = this.cardHolster.asInstanceOf[Weapon.Evolution]

    MusicManager.playSound("click_sound_2")
    destroy3Cards()
  }
  override protected def onMouseEntered(mousePos: Vector2): Unit = {
    super.onMouseEntered(mousePos)
    val prevH: Float = height
    val prevTokenH: Float = token.height

    scale = scale * 1.1f
    token.scale = token.scale * 1.1f

    val gain: Float = (height - prevH)
    val tokenGain = (token.height-prevTokenH)

    token.pos.add(0.0f,gain/tokenGain)

    MusicManager.playSound("click_sound")
  }

  override protected def onMouseLeft(mousePos: Vector2): Unit = {
    super.onMouseLeft(mousePos)
    val prevH = height
    val prevTokenH: Float = token.height

    scale = scale / 1.1f
    token.scale = token.scale / 1.1f

    val reduc: Float = (height - prevH)
    val tokenReduc: Float = (token.height-prevTokenH)

    token.pos.add(0.0f, -reduc/tokenReduc)
  }
}

object Card {
  val cards: Array[Card] = Array.ofDim(3)

  def create3Cards(): Unit = {
    var u: Array[Upgrade] = generate3Upgrades()
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
    val upPos: ArrayBuffer[Upgrade] = GameManager.player.weapon.get.upgradePossibilities.clone().to(ArrayBuffer)
    /*if(GameManager.currentScene.asInstanceOf[GameScene].player.weapon.weaponEvolution == Phase3){
      upPos.append(UltimatePhase)
    }*/
    val cardUp: Array[Upgrade] = Array.ofDim(3)
    for(i <- cardUp.indices){
      cardUp(i) = upPos(random.nextInt(0, upPos.length))
      upPos -= cardUp(i)
      println(s"Upgrade $i is ${cardUp(i)}")
    }
    cardUp
  }
  /*def generate3WishingWellUpgrades(): Array[Holster] = {
    val wwUpPos: Array[Holster] = GameManager.currentScene.asInstanceOf[GameScene].player.weapon.upgradePossibilities
    val wwEvoPos: Array[Holster] = GameManager.currentScene.asInstanceOf[GameScene].player.weapon.weaponEvolution
    val wwUp: Array[Holster] = Array.ofDim(3)
    wwUp(0) = RebuildToy
    wwUp(1) = wwUpPos(random.nextInt(0, wwUpPos.length))
    wwUp(2) = wwEvoPos(random.nextInt(0, wwEvoPos.length))

    wwUp
  }*/
}