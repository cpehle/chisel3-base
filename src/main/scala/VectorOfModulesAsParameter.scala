package chisel3Base

import chisel3._

class MyBundle extends Bundle {
  val x = Bool(INPUT)
  val y = Bool(OUTPUT)
}

class MyModule extends Module {
  val io = new MyBundle
}

class VecOfModAsParamBase[T <: Bundle, U <: Vec[T]](gen: => U)
    extends Module {
  val buildIt = gen
  val io = new Bundle {
    val data = Vec(buildIt.length, buildIt(0).cloneType)
  }
  io.data <> buildIt
}

class VecOfModAsParam extends VecOfModAsParamBase[MyBundle,Vec[MyBundle]] (
  Vec.fill(2)(Module(new MyModule).io))

abstract class VecOfModViaTypeBase[T <: MyBundle] extends Module {
  val buildIt: Vec[T]
  val io = new Bundle {
    lazy val data = Vec(buildIt.length, buildIt(0).cloneType)
  }
  def init {
    io.data <> buildIt
  }
}

class VecOfModViaType extends VecOfModViaTypeBase[MyBundle] {
  val buildIt = Vec.fill(2)(Module(new MyModule).io)
  this.init
}

class VecOfModViaSeqBase[T <: Module, U <: Seq[T]](gen: => U) extends Module {
  val buildIt = gen
  val io = new Bundle {
    val data = Vec(buildIt.length, buildIt(0).io.cloneType)
  }
  (0 until buildIt.length).map(i => io.data(i) <> buildIt(i).io)
}

class VecOfModViaSeq extends VecOfModViaSeqBase[MyModule, Seq[MyModule]] (
  Seq.fill(2)(Module(new MyModule)))
