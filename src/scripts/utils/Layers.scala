package scripts.utils

import scala.collection.mutable.ArrayBuffer

/**
 * Container managing multiple layers of elements, each identified by a Z-index.
 *
 * @param depth Initial number of layers to create (indexed from 0 to depth-1).
 */
class Layers[T](var depth: Int) {
  /** Internal buffer storing each layer. */
  private val layers: ArrayBuffer[Layer[T]] = ArrayBuffer.tabulate(depth)(i => new Layer(i))

  /**
   * Add an element to the existing layer at Z-index `z`.
   *
   * @param z       The Z-index of the layer.
   * @param element The element to insert into that layer.
   */
  def add(z: Int, element: T): Unit = layers(z).add(element)

  /**
   * Add a pre-constructed Layer[T] to the end of this Layers container.
   * The new layer will receive the current `depth` as its Z-index.
   *
   * @param layer The Layer[T] to append.
   */
  def add(layer: Layer[T]): Unit = {
    layer.z = depth   // Assign this layer the next Z-index
    layers += layer   // Append the new layer
    depth += 1        // Increase the total layer count
  }

  /**
   * Create and append a new, empty Layer[T] with the next available Z-index.
   */
  def add(): Unit = add(new Layer[T]())

  /**
   * Remove the specified element from whichever layer it resides in.
   * Iterates through each layer until `remove(element)` returns true.
   *
   * @param element The element to remove.
   */
  def remove(element: T): Unit = layers.exists(_.remove(element))

  /**
   * Remove the layer at Z-index `z` completely.
   * If no layer exists at that index, this is a no-op.
   *
   * @param z The Z-index of the layer to remove.
   */
  def remove(z: Int): Unit = layers -= this.get(z).getOrElse(return)

  /**
   * Move an existing layer to a new Z-index position.
   *
   * @param layer The Layer[T] to relocate.
   * @param new_z The new Z-index to assign.
   */
  def move(layer: Layer[T], new_z: Int): Unit = {
    layers -= layer           // Remove from current position
    layers.insert(new_z, layer) // Reinsert at the new index
  }

  /**
   * Swap two layers given by their Z-index values.
   * If either index is invalid, this does nothing.
   *
   * @param layerA Z-index of the first layer.
   * @param layerB Z-index of the second layer.
   */
  def swap(layerA: Int, layerB: Int): Unit = {
    val temp: Layer[T] = get(layerA).getOrElse(return)
    layers(layerA) = get(layerB).getOrElse(return) // Place layerB into position A
    layers(layerB) = temp                           // Place saved layerA into position B
  }

  /**
   * Retrieve the Layer[T] at Z-index `z` if it exists.
   *
   * @param z Z-index of the desired layer.
   * @return Some(layer) if index is valid, otherwise None.
   */
  def get(z: Int): Option[Layer[T]] = layers.lift(z)

  /**
   * Retrieve all layers as an ArrayBuffer of Layer[T].
   *
   * @return Internal buffer of layers.
   */
  def get(): ArrayBuffer[Layer[T]] = layers

  def nbOfElements(): Int = {
    var nb: Int = 0
    layers.foreach(layer => nb += layer.elements.size)
    nb
  }
}


/**
 * Represents a single layer of elements of type T.
 * @param z Z-index identifying this layer.
 */
class Layer[T](var z: Int = 0) {
  /** Buffer holding all elements belonging to this layer. */
  val elements: ArrayBuffer[T] = ArrayBuffer.empty

  // Tracks the number of elements in this layer (same as elements.size).
  private var _size: Int = 0

  /**
   * Add an element to this layer.
   * @param element The element to insert.
   */
  def add(element: T): Unit = {
    elements += element
    _size += 1
  }

  /**
   * Remove the specified element from this layer.
   * @param element The element to remove.
   * @return True if the element was found and removed, false otherwise.
   */
  def remove(element: T): Boolean =
    elements.indexOf(element) match {
      case -1 => false // Element not found
      case i  =>
        elements.remove(i) // Remove the element at index i
        _size -= 1         // Decrement the size counter
        true
    }

  /**
   * Retrieve the current number of elements in this layer.
   * @return The number of elements.
   */
  def size: Int = _size
}
