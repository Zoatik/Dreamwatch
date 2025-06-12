package scripts.game.actors.instantiables

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.{Object2D, Scene}
import scripts.dreamwatch_engine.actors.instantiables.{Particle2D, Sprite2D, UiElement}
import scripts.dreamwatch_engine.physics.{Area2D, Collider2D, Movement2D}
import scripts.dreamwatch_engine.utils.Layers
import scripts.game.actors.abstracts.Nightmare
import scripts.game.actors.instantiables.nightmares.Ghost
import scripts.game.{GameManager, MusicManager}
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class GameScene() extends Scene{

  override val gLayers: Layers[Sprite2D] = new Layers[Sprite2D](Globals.G_LAYERS_SIZE)
  override val uiLayers: Layers[UiElement] = new Layers[UiElement](Globals.UI_LAYERS_SIZE)
  override val cLayers: Layers[Collider2D] = new Layers[Collider2D](Globals.C_LAYERS_SIZE)
  override val movableObjects: ArrayBuffer[Movement2D] = ArrayBuffer()
  override val objects: ArrayBuffer[Object2D] = ArrayBuffer()
  override val particles: ArrayBuffer[Particle2D] = ArrayBuffer()


  private var waveCounter: Int = 1
  private var waveTimer: Float = 0
  private var waveStatus: String = "normal"
  private var currentTime: Float = 0

  private var maxNightmares: Int = 20
  private var spawnRate: Float = 0

  private var bossCounter: Int = 1
  private var currentBoss: Option[Boss] = None

  val player: Player = GameManager.player
  var dreamShards: Int = 0

  // Random number generator for spawn timing and positions.
  private val rnd = new Random()

  // Has to be called and modified somewhere else:
  var cardsSelectionDone: Boolean = false
  var bossDefeated: Boolean = false
  var isAlive: Boolean = true

  override def instantiate(): GameScene = {
    super.instantiate()

    createSettingsPanel()
    initToys()
    startNewWave()

    this
  }

  override def update(deltaT: Float): Unit = {
    super.update(deltaT)

    GameManager.g.setColor(Color.WHITE)
    GameManager.g.drawString(Globals.WINDOW_WIDTH - 200, Globals.WINDOW_HEIGHT - 40, s"DreamShards: ${dreamShards}")
    if (GameManager.isPaused)
      return

    if(!objects.exists(t => t.isInstanceOf[Toy])) {
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
          if(!objects.exists(e => e.isInstanceOf[Nightmare]) && !objects.exists(f =>
            f.isInstanceOf[Bullet]) && !objects.exists(s => s.isInstanceOf[Particle2D])) {
            waveCounter += 1
            waveStatus = "cards"
            initCards()
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
              waveStatus = "game won"
            }
            else {
              // Next boss
              if(currentBoss.isDefined){
                currentBoss.get.destroy()
                currentBoss = None
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
          player.weapon.get.canShoot = true
          cardsSelectionDone = false
          if (waveCounter % (Globals.NBR_WAVES_BEFORE_BOSS+1) == 0) {
            startNewBossWave()
          }
          else {
            startNewWave()
          }
        }
        // While cards haven't been selected yet but still in cards


      case "wishing well" =>

      case "game won" => gameWon()
      case "game lost" => gameLost()

      // Final default case
      case _ => // Probably sends info that the game is done
    }
  }
  // TODO: give ghost their target
  def initToys(): Unit = {
    new Toy(Globals.TOY_POS(0), ArrayBuffer("res/sprites/game/toy.png")).instantiate()
    new Toy(Globals.TOY_POS(1), ArrayBuffer("res/sprites/game/toy.png")).instantiate()
    new Toy(Globals.TOY_POS(2), ArrayBuffer("res/sprites/game/toy.png")).instantiate()
    new Toy(Globals.TOY_POS(3), ArrayBuffer("res/sprites/game/toy.png")).instantiate()
  }

  def initCards(): Unit = {
    player.weapon.get.canShoot = false
    Card.create3Cards()
  }

  // Wave functions
  def startNewWave(): Unit = {
    waveTimer = 0
    //maxNightmares += 2
    waveStatus = "normal"
    spawnRate = (Globals.DEFAULT_SPAWN_RATE + waveCounter)*10
  }
  def startNewBossWave(): Unit = {
    waveStatus = "boss"
    spawnRate = (Globals.DEFAULT_SPAWN_RATE + waveCounter)*10
    currentBoss = Some(Boss.spawnBoss(bossCounter))
    nextBackground()
    MusicManager.nextMusic()
  }

  // Basically handles all the spawning
  def updateWave(deltaT: Float): Unit = {
    waveStatus match {
      case "normal" =>
        if(rnd.nextFloat() < deltaT * spawnRate) {
          val toys = objects.filter(obj => obj.isInstanceOf[Toy])
          if (toys.length <= 0)
            return
          val startX = rnd.nextFloat() * GameManager.g.getScreenWidth
          val startY = GameManager.g.getScreenHeight
          val randIdx: Int = rnd.nextInt(toys.length)
          val targetX = toys(randIdx).pos.x + (rnd.nextFloat()-0.5f) * 50.0f
          val targetY = 0f
          new Ghost(new Vector2(startX, startY), new Vector2(targetX, targetY)).instantiate()
        }
      case "cards" => // nothing

      case "boss" => // nothing

      case "bossMinionsAttack" =>
      // Boss spawns ghosts
        if(rnd.nextFloat() < deltaT * spawnRate) {
          val spawnPosX = currentBoss.getOrElse(return).pos.x + 500*(rnd.nextFloat() - 0.5f)
          val bossPos: Vector2 = new Vector2(spawnPosX, currentBoss.getOrElse(return).pos.y)
          val bossTarget: Vector2 = new Vector2(GameManager.g.getScreenWidth/2, 0)
          new Ghost(bossPos, bossTarget).instantiate()
        }
      case _ =>
    }
  }

  override def handleMouseInput(pos: Vector2, button: Int): Boolean = {
    var handled: Boolean = false
    if(!GameManager.isPaused){
      handled = player.handleMouseInput(pos, button)
    }
    handled
  }

  override def handleKeyInput(button: Int): Boolean = {
    true
  }

  private def gameLost(): Unit = {
    GameManager.pause()
    createEndPanel()
  }

  private def gameWon(): Unit = {
    GameManager.pause()
    createEndPanel()
  }

  private def createEndPanel(): Unit = {
    val panel: UiElement = new UiElement(
      new Vector2(Globals.WINDOW_WIDTH/2, Globals.WINDOW_HEIGHT/2),
      0,
      ArrayBuffer("res/sprites/ui/panel.png"),
      0,
      Area2D.Box
    )

    val mainMenuButton: UiElement = new UiElement(
      new Vector2(Globals.WINDOW_WIDTH/2, Globals.WINDOW_HEIGHT/2),
      0,
      ArrayBuffer("res/sprites/ui/buttons/home_small_button.png"),
      1,
      Area2D.Box
    )

    panel.width = 200.0f
    mainMenuButton.width = 50.0f

    mainMenuButton.bindMouseEntered(_ => {
      mainMenuButton.scale = mainMenuButton.scale * 1.1f
      MusicManager.playSound("click_sound")
    })

    mainMenuButton.bindMouseLeft(_ => {
      mainMenuButton.scale = mainMenuButton.scale / 1.1f
      MusicManager.playSound("click_sound")
    })

    mainMenuButton.bindMousePressed((_, _) => {
      MusicManager.playSound("click_sound_2")
      GameManager.loadScene(new MainMenuScene())
      GameManager.resume()
    })


    panel.instantiate()
    mainMenuButton.instantiate()
  }

  private def createSettingsPanel(): Unit = {
    val panel: UiElement = new UiElement(
      new Vector2(Globals.WINDOW_WIDTH/2, Globals.WINDOW_HEIGHT/2),
      0,
      ArrayBuffer("res/sprites/ui/panel.png"),
      0,
      Area2D.Box
    )

    val mainMenuButton: UiElement = new UiElement(
      new Vector2(Globals.WINDOW_WIDTH/2, Globals.WINDOW_HEIGHT/2),
      0,
      ArrayBuffer("res/sprites/ui/buttons/home_small_button.png"),
      1,
      Area2D.Box
    )

    panel.width = 200.0f
    panel.isVisible = false
    mainMenuButton.width = 50.0f
    mainMenuButton.isVisible = false

    mainMenuButton.bindMouseEntered(_ => {
      mainMenuButton.scale = mainMenuButton.scale * 1.1f
      MusicManager.playSound("click_sound")
    })

    mainMenuButton.bindMouseLeft(_ => {
      mainMenuButton.scale = mainMenuButton.scale / 1.1f
      MusicManager.playSound("click_sound")
    })

    mainMenuButton.bindMousePressed((_, _) => {
      MusicManager.playSound("click_sound_2")
      GameManager.loadScene(new MainMenuScene())
      GameManager.resume()
    })


    panel.instantiate()
    mainMenuButton.instantiate()
  }

}
