package scripts.game.actors.instantiables

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
    0,
    Boss.loadImagesFor(bossType),
    Globals.BOSS_G_LAYER,
    Globals.BOSS_C_LAYER,
    Globals.BOSS_C_LAYERMASK,
    Area2D.Box,
    lifeTime = None
  ) with Movement2D {

  width = 300.0f

  override var speed: Float = 80.0f
  override var target: Vector2 = new Vector2(Globals.WINDOW_WIDTH,Globals.WINDOW_HEIGHT/1.4f)

  var hp: Float = bossHp(bossType)

  override def instantiate(): Boss = {
    super.instantiate()
    this
  }

  override protected def onCollision(other: Collider2D): Unit = other match{
    case b: Bullet =>
      takeDamage(b.bulletDamage)
    case e: CollisionObject2D with Component[Bullet] =>
      takeDamage(e.parent.explosionDamage)
    case _ =>
  }

  override def destroy(): Unit = {
    super.destroy()
  }

  override def onTargetReached(): Unit = {
    super.onTargetReached()
    if(target.x == Globals.WINDOW_WIDTH)
      target = new Vector2(0,Globals.WINDOW_HEIGHT/1.4f)
    else
      target = new Vector2(Globals.WINDOW_WIDTH,Globals.WINDOW_HEIGHT/1.4f)
  }

  protected def takeDamage(amount: Float): Unit = {
    println("Boss has taken damage")
    hp -= amount
    scale = hp / Globals.DEFAULT_BOSS_HP
    if (width <= 20.0f) width = 20.0f
    if(hp <= 0){
      GameManager.currentScene.asInstanceOf[GameScene].bossDefeated = true
      this.destroy()
    }
  }

}

object Boss{
  private def loadImagesFor(bulletType: Type): ArrayBuffer[String] = bulletType match {
    case UneAraignee => ArrayBuffer.fill(1)("res/sprites/bosses/UneAraignee.png")
    case Ghost => ArrayBuffer.fill(1)("res/sprites/bosses/Ghost.png")
    case TheGrimReaper => ArrayBuffer.fill(1)("res/sprites/bosses/TheGrimReaper.png")
    case _ => ArrayBuffer.fill(1)("res/sprites/bosses/UneAraignee.png")

  }
  private def baseBossRadius(bossType: Type): Float = bossType match {
    case UneAraignee => 500.0f
    case Ghost => 500.0f
    case TheGrimReaper => 500.0f
    case _ => 500.0f
  }

  private def bossHp(bossType: Type): Float = bossType match {
    case UneAraignee => Globals.DEFAULT_BOSS_HP
    case Ghost => Globals.DEFAULT_BOSS_HP
    case TheGrimReaper => Globals.DEFAULT_BOSS_HP
    case ZeMudry => Globals.DEFAULT_BOSS_HP
  }


  def spawnBoss(bossCounter: Int): Boss = {
    bossCounter match {
      case 1 =>
        new Boss(Globals.DEFAULT_BOSS_POS, Boss.UneAraignee).instantiate()
      case 2 =>
        new Boss(Globals.DEFAULT_BOSS_POS, Boss.Ghost).instantiate()
      case 3 =>
        new Boss(Globals.DEFAULT_BOSS_POS, Boss.TheGrimReaper).instantiate()
      case _ =>
        new Boss(Globals.DEFAULT_BOSS_POS, Boss.ZeMudry).instantiate()
    }

  }


  sealed trait Type

  case object UneAraignee extends Type
  case object Ghost extends Type
  case object TheGrimReaper extends Type
  case object ZeMudry extends Type
}