sealed trait Type
case object T1 extends Type
case object T2 extends Type

val obj1 = T1
val obj2 = T2
val obj3 = T1

obj1.equals(obj2)
obj1.equals(obj3)