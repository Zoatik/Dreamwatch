package scripts.dreamwatch_engine.actors.instantiables

import ch.hevs.gdx2d.lib.renderers.ShaderRenderer
import com.badlogic.gdx.math.{Vector2, Vector3}
import scripts.dreamwatch_engine.actors.abstracts.Object2D
import scripts.game.GameManager
import scripts.utils.Globals

import scala.collection.mutable

class Particle2D(shaderPath: String, pos: Vector2, angle: Float, lifeTime: Option[Float] = None)
  extends Object2D(pos, angle, lifeTime){

  var shaderRenderer: ShaderRenderer = new ShaderRenderer(shaderPath, Globals.WINDOW_WIDTH, Globals.WINDOW_HEIGHT)
  private val uniforms: mutable.HashMap[String, Any] = mutable.HashMap()

  override def instantiate(): Particle2D = {
    super.instantiate()
    this
  }

  def setUniform(name: String, value: Any): Unit = {
    uniforms.update(name, value)
  }

  def render(): Unit = {

    uniforms.foreach(u => u._2 match {
      case boolean: Boolean => shaderRenderer.setUniform(u._1, boolean)
      case int: Int => shaderRenderer.setUniform(u._1, int)
      case float: Float => shaderRenderer.setUniform(u._1, float)
      case vec2: Vector2 => shaderRenderer.setUniform(u._1, vec2)
      case vec3: Vector3 => shaderRenderer.setUniform(u._1, vec3)
      case floatArray: Array[Float] => shaderRenderer.setUniform(u._1, floatArray)
      case _ => throw new Exception("Wrong type given for shader uniform !")
    })

    GameManager.g.setShaderRenderer(shaderRenderer)
    GameManager.g.drawShader(timeFromCreation)

  }

  override def destroy(): Unit = {
    super.destroy()
  }
}
