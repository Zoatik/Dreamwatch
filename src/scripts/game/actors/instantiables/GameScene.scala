package scripts.game.actors.instantiables

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.{Object2D, Scene}
import scripts.dreamwatch_engine.actors.instantiables.{Particle2D, Sprite2D, UiElement}
import scripts.dreamwatch_engine.inputs.InputManager
import scripts.dreamwatch_engine.physics.{Collider2D, Movement2D}
import scripts.dreamwatch_engine.utils.Layers
import scripts.game.GameManager
import scripts.game.GameManager.toyPos
import scripts.game.actors.abstracts.{Nightmare, Player, Weapon}
import scripts.game.actors.instantiables.nightmares.Ghost
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class GameScene(gamePlayer: GamePlayer) extends Scene{

  override val gLayers: Layers[Sprite2D] = new Layers[Sprite2D](Globals.G_LAYERS_SIZE)
  override val uiLayers: Layers[UiElement] = new Layers[UiElement](Globals.UI_LAYERS_SIZE)
  override val cLayers: Layers[Collider2D] = new Layers[Collider2D](Globals.C_LAYERS_SIZE)
  override val movableObjects: ArrayBuffer[Movement2D] = ArrayBuffer()
  override val objects: ArrayBuffer[Object2D] = ArrayBuffer()
  override val particles: ArrayBuffer[Particle2D] = ArrayBuffer()
  override val player: GamePlayer = gamePlayer

  private var waveCounter: Int = 1
  private var waveTimer: Float = 0
  private var waveStatus: String = "normal"
  private var currentTime: Float = 0

  private var maxNightmares: Int = 20
  private var spawnRate: Float = 0

  private var bossCounter: Int = 1
  private var currentBoss: Option[Boss] = None

  // Random number generator for spawn timing and positions.
  private val rnd = new Random()

  // Has to be called and modified somewhere else:
  var cardsSelectionDone: Boolean = false
  var bossDefeated: Boolean = false
  var isAlive: Boolean = true

  override def instantiate(): GameScene = {
    super.instantiate()

    /*InputManager.onKeyPressed(_ => { // TODO: SHOULD BE HERE
      println("key pressed from GameScene")
      waveStatus match {
        case "boss" => bossDefeated = true
        //case "cards" => cardsSelectionDone = true
        case _ =>
      }
    })*/
    startNewWave()
    initToys()
    this
  }

  override def update(deltaT: Float): Unit = {
    super.update(deltaT)

    GameManager.g.setColor(Color.WHITE)
    GameManager.g.drawString(Globals.WINDOW_WIDTH - 200, Globals.WINDOW_HEIGHT - 40, s"DreamShards: ${player.dreamShards}")
    if (GameManager.isPaused)
      return

    if(!objects.exists(t => t.isInstanceOf[Toy])) {
      println("you lost boi")
      isAlive = false
    }

    currentTime += deltaT
    waveTimer += deltaT
    updateWave(deltaT)


    if(!isAlive){
      // Game is lost !
      waveStatus = "game lost"
    }
    waveStatus match {
      case "normal" =>
        if (waveTimer < Globals.WAVE_LENGTH) {
          // Do stuff here during the normal wave
          // if clicked on wishing well: 1) waveStatus = "wishing well" 2) initCards(waveStatus)
        }
        else {
          // End of the wave. Do this:
          // Stop spawning nightmares
          spawnRate = 0                                         // Ajouté pour éviter d'avoir des balles encore vollantes lors du choix de cartes
          if(!objects.exists(e => e.isInstanceOf[Nightmare]) && !objects.exists(f => f.isInstanceOf[Bullet]) && !objects.exists(s => s.isInstanceOf[Particle2D])) {
            waveCounter += 1
            waveStatus = "cards"
            initCards()
          }
          else{
            //println("Wait..")
          }
        }


      case "boss" | "bossMinionsAttack" =>
        // Boss wave loop
        if(!bossDefeated){
          if(waveTimer%20 < 5){
            waveStatus = "bossMinionsAttack"
          }
          else{
            waveStatus = "boss"
          }
        }
        // What happens when the boss is defeated
        else{
          spawnRate = 0 // Keep cause the boss died so would be bad if kept spawning out of nowhere
          if(!objects.exists(e => e.isInstanceOf[Nightmare])) {
            bossDefeated = false

            if (bossCounter == Globals.NBR_OF_BOSSES) {
              // Game is won !
              println("Game is won !")
              println(s"${bossCounter} bosses were defeated.")
              waveStatus = "game won"
            }
            else {
              // Next boss
              if(currentBoss.isDefined){
                currentBoss.get.destroy()
                currentBoss = None
                nextBackground()
              }
              waveCounter += 1
              waveStatus = "cards"
              initCards()
              cardsSelectionDone = false // I HAVE NO CLUE WHY BUT cardsSelectionDone IS TRUE HERE
              bossCounter += 1
            }
          }
        }


      case "cards" =>
        // Show cards (will most likely handle the cards showing that will access this object and modify the cardsSelectionDone)
        // What happens once you've chosen you're upgrade card
        if (cardsSelectionDone) {
          println("Cards selection DONE")
          player.weapon.canShoot = true
          cardsSelectionDone = false
          if (waveCounter % (Globals.NBR_WAVES_BEFORE_BOSS+1) == 0) {
            startNewBossWave()
          }
          else {
            startNewWave()
          }
        }
        // While cards haven't been selected yet but still in cards
        else {
          //println("Entered cards phase.. choose your cards.")

        }

      case "wishing well" =>

      case "game won" => println("Game won !")
      case "game lost" => println("Gam lost !")

      // Final default case
      case _ => // Probably sends info that the game is done
    }
  }

  def initToys(): Unit = {
    new Toy(Globals.TOY_POS(0), ArrayBuffer("src/res/sprites/toy.png")).instantiate()
    new Toy(Globals.TOY_POS(1), ArrayBuffer("src/res/sprites/toy.png")).instantiate()
    new Toy(Globals.TOY_POS(2), ArrayBuffer("src/res/sprites/toy.png")).instantiate()
    new Toy(Globals.TOY_POS(3), ArrayBuffer("src/res/sprites/toy.png")).instantiate()
  }

  def initCards(): Unit = {
    player.weapon.canShoot = false
    Card.create3Cards()
  }

  // Wave functions
  def startNewWave(): Unit = {
    println(s"Wave: $waveCounter")
    waveTimer = 0
    //maxNightmares += 2
    waveStatus = "normal"
    spawnRate = (Globals.DEFAULT_SPAWN_RATE + waveCounter)*10
  }
  def startNewBossWave(): Unit = {
    waveStatus = "boss"
    spawnRate = (Globals.DEFAULT_SPAWN_RATE + waveCounter)*10
    currentBoss = Some(Boss.spawnBoss(bossCounter))
  }

  // Basically handles all the spawning
  def updateWave(deltaT: Float): Unit = {
    waveStatus match {
      case "normal" =>
        if(rnd.nextFloat() < deltaT * spawnRate) {
          val startX = rnd.nextFloat() * GameManager.g.getScreenWidth
          val startY = GameManager.g.getScreenHeight
          val targetX = rnd.nextFloat() * GameManager.g.getScreenWidth
          val targetY = 0f
          new Ghost(new Vector2(startX, startY), new Vector2(targetX, targetY)).instantiate()
        }
      case "cards" =>
        //println("Do nothing")

      case "boss" =>
        //nothing

      case "bossMinionsAttack" =>
      // Boss spawns ghosts
        if(rnd.nextFloat() < deltaT * spawnRate) {
          val bossPos: Vector2 = new Vector2(Globals.DEFAULT_BOSS_POS.x*rnd.nextFloat(), Globals.DEFAULT_BOSS_POS.y)
          val bossTarget: Vector2 = new Vector2(GameManager.g.getScreenWidth/2, 0)
          new Ghost(bossPos, bossTarget).instantiate()
        }
      case _ =>
    }
  }

  override def handleMouseInput(pos: Vector2, button: Int): Boolean = {
    var handled: Boolean = false
    if(!GameManager.isPaused){
      println("handled scene")
      handled = player.handleMouseInput(pos, button)
    }
    handled
  }

  override def handleKeyInput(button: Int): Boolean = {
    true
  }

}
