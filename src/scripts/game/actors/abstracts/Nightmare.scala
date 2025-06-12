package scripts.game.actors.abstracts

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Component
import scripts.dreamwatch_engine.actors.instantiables.{CollisionObject2D, CollisionSprite2D, Particle2D}
import scripts.dreamwatch_engine.physics.{Area2D, Collider2D, Movement2D}
import scripts.game.GameManager
import scripts.game.actors.instantiables.{Bullet, GamePlayer}
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer

abstract class Nightmare (pos: Vector2,
                          targetPos: Vector2,
                          images: ArrayBuffer[String],
                          animDuration: Float,
                          scale: Float,
                          lifeTime: Option[Float] = None,
                          )
  extends CollisionSprite2D(
    pos,
    0,
    images,
    Globals.NIGHTMARE_G_LAYER,
    Globals.NIGHTMARE_C_LAYER,
    Globals.NIGHTMARE_C_LAYERMASK,
    Area2D.Box,
    animDuration,
    spriteScale = scale,
    lifeTime = lifeTime
  ) with Movement2D {

  override var speed: Float
  override var target: Vector2 = targetPos.cpy
  // Just testing to set the target to a custom value.. will somehow set target as one of the random toys
  //override var target: Vector2 = new Vector2(GameManager.g.getScreenWidth/3, 0)


  override protected def onCollision(other: Collider2D): Unit = other match{
    case _: Bullet => this.destroy()
    case _: CollisionObject2D with Component[Bullet] => this.destroy()
    case _ =>
  }

  override def destroy(): Unit = {
    super.destroy()
    val particles = Particle2D.spawnParticles(pos, 40.0f, 10.0f, 50.0f, 5, "res/sprites/texture.png", gLayerZ)
    particles.foreach(particle => particle.bindMouseEntered(_ => {
      particle.speed = 500.0f
      particle.direction = null
      particle.target = new Vector2(Globals.WINDOW_WIDTH/2,0)
      particle.stopAtTarget = true
      particle.bindTargetReached(_ =>{
        GameManager.currentScene.player.asInstanceOf[GamePlayer].dreamShards += 1
        particle.unbindListeners()
        particle.destroy()
      })
    }))
  }

}

