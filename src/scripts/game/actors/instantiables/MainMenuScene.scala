package scripts.game.actors.instantiables

import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.{Object2D, Scene}
import scripts.dreamwatch_engine.actors.instantiables.{Particle2D, Sprite2D, UiElement}
import scripts.dreamwatch_engine.physics.{Area2D, Collider2D, Movement2D}
import scripts.dreamwatch_engine.utils.Layers
import scripts.game.actors.instantiables.weapons.Sniper
import scripts.game.{GameManager, MusicManager}
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

class MainMenuScene() extends Scene {

  override val gLayers: Layers[Sprite2D] = new Layers[Sprite2D](Globals.G_LAYERS_SIZE)
  override val uiLayers: Layers[UiElement] = new Layers[UiElement](Globals.UI_LAYERS_SIZE)
  override val cLayers: Layers[Collider2D] = new Layers[Collider2D](Globals.C_LAYERS_SIZE)
  override val movableObjects: ArrayBuffer[Movement2D] = ArrayBuffer()
  override val objects: ArrayBuffer[Object2D] = ArrayBuffer()
  override val particles: ArrayBuffer[Particle2D] = ArrayBuffer()

  override def handleMouseInput(pos: Vector2, button: Int): Boolean = {false}

  override def handleKeyInput(button: Int): Boolean = {false}


  override def instantiate(): MainMenuScene = {
    super.instantiate()

    createPlayButton().instantiate()
    createSettingsButton().instantiate()
    createSettingsPanel().instantiate()

    this
  }


  override def update(deltaT: Float): Unit = {
    super.update(deltaT)
  }


  private def createPlayButton(): UiElement = {
    val playButton: UiElement = new UiElement(
      new Vector2(Globals.WINDOW_WIDTH / 2, Globals.WINDOW_HEIGHT / 2),
      0,
      ArrayBuffer("res/sprites/ui/buttons/play_button.png"),
      0,
      Area2D.Box
    )
    playButton.width = 400.0f

    playButton.bindMouseEntered(_ => {
      playButton.scale = playButton.scale * 1.1f
      MusicManager.playSound("click_sound")
    })
    playButton.bindMouseLeft(_ => {
      playButton.scale = playButton.scale / 1.1f
      MusicManager.playSound("click_sound")
    })

    playButton.bindMousePressed((_, _) => {
      MusicManager.playSound("click_sound_2")

      val gameScene = new GameScene()
      GameManager.loadScene(gameScene)
      gameScene.add(createSettingsButton())
      gameScene.add(createSettingsPanel())
      GameManager.player.weapon = Some(new Sniper(new Vector2(Globals.WINDOW_WIDTH / 2, 50)).instantiate())
    })

    playButton
  }

  private def createSettingsButton(): UiElement = {
    val settingsButton: UiElement = new UiElement(
      new Vector2(50, Globals.WINDOW_HEIGHT - 50),
      0,
      ArrayBuffer("res/sprites/ui/buttons/settings_small_button.png"),
      0,
      Area2D.Box
    )

    settingsButton.width = 50.0f

    settingsButton.bindMouseEntered(_ => {
      settingsButton.scale = settingsButton.scale * 1.1f
      MusicManager.playSound("click_sound")
    })

    settingsButton.bindMouseLeft(_ => {
      settingsButton.scale = settingsButton.scale / 1.1f
      MusicManager.playSound("click_sound")
    })

    settingsButton.bindMousePressed((_, _) => {
      MusicManager.playSound("click_sound_2")
      GameManager.togglePause()
      GameManager.currentScene.objects.filter(obj =>
        obj != settingsButton && obj.isInstanceOf[UiElement]).foreach(el =>
        el.asInstanceOf[UiElement].toggleVisible())
    })

    settingsButton

  }

  private def createSettingsPanel(): UiElement = {
    val settingsPanel: UiElement = new UiElement(
      new Vector2(Globals.WINDOW_WIDTH/2, Globals.WINDOW_HEIGHT/2),
      0,
      ArrayBuffer("res/sprites/ui/panel.png"),
      0,
      Area2D.Box
    )
    settingsPanel.isVisible = false

    settingsPanel

  }


}


