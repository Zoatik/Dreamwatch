package scripts

import scala.collection.mutable.ArrayBuffer

/**
 * Layers class - contains all layers infos
 */
class Layers[T](private var depth: Int) {
  // layers creation
  private val layers: ArrayBuffer[Layer[T]] = ArrayBuffer.tabulate(depth)(i => new Layer(i))

  def add(z: Int, element: T): Unit = layers(z).add(element)

  def add(layer: Layer[T]): Unit = {
    layer.z = depth
    layers += layer
    depth += 1
  }

  def add(): Unit = add(new Layer[T]())

  def remove(element: T): Unit = layers.exists(_.remove(element))

  def remove(z: Int): Unit = layers -= get(z).getOrElse(return)

  def move(layer: Layer[T], new_z: Int): Unit = {
    layers -= layer
    layers.insert(new_z, layer)
  }

  def swap(layerA: Int, layerB: Int): Unit = {
    val temp: Layer[T] = get(layerA).getOrElse(return)
    layers(layerA) = get(layerB).getOrElse(return)
    layers(layerB) = temp
  }

  def get(z: Int): Option[Layer[T]] = layers.lift(z)

  def get(): ArrayBuffer[Layer[T]] = layers
}


class Layer[T](var z: Int = 0) {
  val elements: ArrayBuffer[T] = ArrayBuffer.empty
  private var _size: Int = 0

  def add(element: T): Unit = {
    elements += element
    _size += 1
  }
  def remove(element: T): Boolean =
    elements.indexOf(element) match {
      case -1 => false
      case i  =>
        elements.remove(i);
        _size -= 1
        true
    }

  def size: Int = _size
}
