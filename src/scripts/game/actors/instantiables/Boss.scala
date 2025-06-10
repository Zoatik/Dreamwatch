package scripts.game.actors.instantiables

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Component
import scripts.dreamwatch_engine.actors.instantiables.{CollisionObject2D, CollisionSprite2D}
import scripts.dreamwatch_engine.physics.{Area2D, Collider2D, Movement2D}
import scripts.game.GameManager
import scripts.game.actors.instantiables.Boss.bossHp
import scripts.utils.Globals

import scala.collection.mutable.ArrayBuffer


class Boss(pos: Vector2,
           bossType: Boss.Type,
          )
  extends CollisionSprite2D(
    pos,
    Boss.loadImagesFor(bossType),
    Globals.BOSS_G_LAYER,
    Globals.BOSS_C_LAYER,
    Globals.BOSS_C_LAYERMASK,
    Area2D.Circle,
    lifeTime = None
  ) with Movement2D {

  override var speed: Float = 0
  override var target: Vector2 = new Vector2(0,0)

  var hp: Float = bossHp(bossType)


  override protected def onCollision(other: Collider2D): Unit = other match{
    case b: Bullet =>
      takeDamage(b.damage)
    case e: CollisionObject2D with Component[Bullet] =>
      takeDamage(e.parent.explosionDamage)
    case _ =>
  }

  protected def takeDamage(amount: Float): Unit = {
    println("Boss has taken damage")
    hp -= amount
    if(hp <= 0){
      GameManager.currentScene.asInstanceOf[GameScene].bossDefeated = true
      this.destroy()
    }
  }

}

object Boss{
  private def loadImagesFor(bulletType: Type): ArrayBuffer[BitmapImage] = bulletType match {
    case UneAraignee => ArrayBuffer.fill(1)(new BitmapImage("res/sprites/UneAraignee.png"))
    case Ghost => ArrayBuffer.fill(1)(new BitmapImage("res/sprites/Ghost.png"))
    case TheGrimReaper => ArrayBuffer.fill(1)(new BitmapImage("res/sprites/TheGrimReaper.png"))
    case _ => ArrayBuffer.fill(1)(new BitmapImage("res/sprites/UneAraignee.png"))

  }
  private def baseBossRadius(bossType: Type): Float = bossType match {
    case UneAraignee => 50.0f
    case Ghost => 50.0f
    case TheGrimReaper => 50.0f
    case _ => 50.0f
  }

  private def bossHp(bossType: Type): Float = bossType match {
    case UneAraignee => Globals.DEFAULT_BOSS_HP
    case Ghost => Globals.DEFAULT_BOSS_HP
    case TheGrimReaper => Globals.DEFAULT_BOSS_HP
    case ZeMudry => Globals.DEFAULT_BOSS_HP
  }



  sealed trait Type

  case object UneAraignee extends Type
  case object Ghost extends Type
  case object TheGrimReaper extends Type
  case object ZeMudry extends Type
}