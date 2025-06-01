package scripts.World.Actors

import com.badlogic.gdx.math.Vector2
import scripts.Sprite
import scripts.World.BaseClass.{Area2D, CircleArea2D, Collider2D, Movement2D, Object2D}


class Bullet(pos: Vector2, bulletType: Bullet.Type)
  extends Object2D(pos, Bullet.loadSpriteFor(bulletType)) with Movement2D{

  var damage: Float = 1.0f
  var explosionRadius: Float =

  private val bulletArea = new CircleArea2D(pos, )
  val bulletCollider = Collider2D.create(bulletArea, this, 0)

  override def destroy(): Unit = ???
}

/** Sealed trait pour les types de projectiles */
object Bullet {

  sealed trait Type
  case object Small extends Type
  case object Big   extends Type
  case object Laser extends Type

  /** Charge le sprite correspondant à chaque type de bullet */
  def loadSpriteFor(bulletType: Type): Sprite = bulletType match {
    case Small => new Sprite(new Texture("small_bullet.png"))
    case Big   => new Sprite(new Texture("big_bullet.png"))
    case Laser => new Sprite(new Texture("laser_beam.png"))
  }

  def baseExplosionRadius(bulletType: Type): Float = bulletType match {
    case Small => 1.0f
    case Big => 2.0f
    case Laser => 1.0f
  }
}




