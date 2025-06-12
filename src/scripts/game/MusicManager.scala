package scripts.game

import ch.hevs.gdx2d.components.audio.{MusicPlayer, SoundSample}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object MusicManager {

  private var _soundsMuted = false
  private var _musicsMuted = false

  private val musics: ArrayBuffer[String] = ArrayBuffer(
    "res/sounds/music_1.mp3",
    "res/sounds/music_2.mp3",
    "res/sounds/music_3.mp3"
  )

  private var musicPlayers: ArrayBuffer[MusicPlayer] = _
  private var soundSamples: mutable.HashMap[String, SoundSample] = _
  private var currentMusicId: Int = -1

  def init(): Unit = {
    soundSamples = mutable.HashMap(
      ("click_sound", new SoundSample("res/sounds/click_sound.mp3")),
      ("click_sound_2", new SoundSample("res/sounds/click_sound_2.mp3")),
      ("explosion_sound", new SoundSample("res/sounds/explosion_sound.mp3")),
      ("bubble_sound", new SoundSample("res/sounds/bubble_pop.mp3")),
      ("reload_sound", new SoundSample("res/sounds/reload.mp3"))
    )

    musicPlayers = ArrayBuffer(
      new MusicPlayer("res/sounds/music_1.mp3"),
      new MusicPlayer("res/sounds/music_2.mp3"),
      new MusicPlayer("res/sounds/music_3.mp3")
    )

  }


  def playSound(name: String): Unit = {
    if (!_soundsMuted)
      soundSamples(name).play()
  }

  def muteSounds(): Unit = _soundsMuted = true
  def unmuteSounds(): Unit = _soundsMuted = false
  def toggleSounds(): Unit = _soundsMuted = ! _soundsMuted

  def playMusic(id: Int): Unit = {
    if(!_musicsMuted) {
      musicPlayers(id).loop()
      currentMusicId = id
    }
  }

  def stopMusic(): Unit = {
    if (currentMusicId >= 0)
      musicPlayers(currentMusicId).stop()
  }

  def nextMusic(): Unit = {
    if(!_musicsMuted) {
      currentMusicId = (currentMusicId + 1) % musicPlayers.length
      musicPlayers(currentMusicId).loop()
    }
  }

  def muteMusic(): Unit = {
    _musicsMuted = true
    stopMusic()
  }
  def unmuteMusic(): Unit = {
    _musicsMuted = false
    playMusic(currentMusicId)
  }
  def toggleMusic(): Unit = {
    if (_musicsMuted)
      unmuteMusic()
    else
      muteMusic()
  }


  def dispose(): Unit = {
    soundSamples.foreach(_._2.dispose())
    musicPlayers.foreach(_.dispose())
  }






}
