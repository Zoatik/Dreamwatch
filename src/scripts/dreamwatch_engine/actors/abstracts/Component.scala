package scripts.dreamwatch_engine.actors.abstracts

trait Component[T] {
  val parent: T
}
