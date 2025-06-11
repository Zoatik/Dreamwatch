package scripts.dreamwatch_engine.actors.instantiables

import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.physics.{Area2D, Movement2D}
import scripts.utils.Globals

import java.util.Random
import scala.collection.mutable.ArrayBuffer

class Particle2D(pos: Vector2,
                 angle: Float,
                 override var speed: Float,
                 override var target: Vector2,
                 images: ArrayBuffer[String],
                 gLayerZ: Int,
                 area2DType: Area2D.Type,
                 animDuration: Float = 0f,
                 spriteScale: Float = 1f,
                 lifeTime: Option[Float] = None)
  extends Sprite2D(pos, angle, images, gLayerZ, area2DType, animDuration, spriteScale, lifeTime) with Movement2D{


  override def instantiate(): Particle2D = {
    super.instantiate()
    this
  }

  override def onTargetReached(): Unit = {
    super.onTargetReached()
    if(stopAtTarget){}
      //destroy()
  }



  override def destroy(): Unit = {
    super.destroy()
  }


}

object Particle2D {
  private val rand = new Random
  def spawnParticles(pos: Vector2,
                     particleSize: Float,
                     lifeTime: Float,
                     speed: Float,
                     nbOfParticles: Int,
                     imagePath: String,
                     gLayerz: Int,
                     maxDist: Float = 0.0f): Array[Particle2D] = {
    val particles = new Array[Particle2D](nbOfParticles)
    for (i <- 0 until nbOfParticles){
      val target: Vector2 = new Vector2(rand.nextInt(0, Globals.WINDOW_WIDTH), rand.nextInt(0, Globals.WINDOW_HEIGHT))
      target.sub(pos)
      target.nor()
      if(maxDist > 0) {
        target.scl(maxDist * rand.nextFloat(0.1f,1.0f))
        target.add(pos)
      }

      val part = new Particle2D(
        pos.cpy(),
        rand.nextFloat(),
        rand.nextFloat(0.0f, speed),
        target.cpy(),
        ArrayBuffer(imagePath),
        gLayerz,
        Area2D.Circle,
        lifeTime = Some(lifeTime))

      if (maxDist <= 0f)
        part.stopAtTarget = false

      if(maxDist <= 0f)
        part.direction = target.cpy().nor()

      part.width = particleSize
      part.instantiate()
      particles(i) = part
    }
    particles
  }
}
