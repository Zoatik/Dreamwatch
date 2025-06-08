package scripts.dreamwatch_engine.actors.instantiables

import ch.hevs.gdx2d.lib.renderers.ShaderRenderer
import com.badlogic.gdx.math.Vector2
import scripts.dreamwatch_engine.actors.abstracts.Object2D

import scala.collection.mutable

class Particle2D(shaderPath: String, pos: Vector2, var width: Int, var height: Int, lifeTime: Option[Float] = None)
  extends Object2D(pos, lifeTime){

  var shaderRenderer: ShaderRenderer = _
  private val uniforms: mutable.HashMap[String, Any] = mutable.HashMap()

  override def instantiate(): Particle2D = {
    shaderRenderer = new ShaderRenderer(shaderPath, width, height)
    super.instantiate()
    this
  }

  def setUniform(name: String, value: Any): Unit = {
    uniforms.update(name, value)
  }

  def render(): Unit = {
    shaderRenderer.render(pos.x.toInt, pos.y.toInt, timeFromCreation) // pos not working
    uniforms.foreach(u => u._2 match {
      case boolean: Boolean => shaderRenderer.setUniform(u._1, boolean)
      case int: Int => shaderRenderer.setUniform(u._1, int)
      case float: Float => shaderRenderer.setUniform(u._1, float)
      case vec2: Vector2 => shaderRenderer.setUniform(u._1, vec2)
      case vec3: Float => shaderRenderer.setUniform(u._1, vec3)
      case floatArray: Array[Float] => shaderRenderer.setUniform(u._1, floatArray)
      case _ => throw new Exception("Wrong type given for shader uniform !")
    })

  }

  override def destroy(): Unit = {
    super.destroy()
    println("Particle destroyed")
  }
}
