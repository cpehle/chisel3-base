package chisel3Base

import chisel3._

// This would be a good example to use for the website

class BundleParent extends Bundle      { val a = Bool(INPUT) }
class BundleChild extends BundleParent { val b = Bool(INPUT) }

class IoParent extends Bundle  {          val x = new BundleParent }
class IoChild extends IoParent { override val x = new BundleChild  }

class ModuleParent extends Module      {          lazy val io = new IoParent }
class ModuleChild extends ModuleParent { override lazy val io = new IoChild }

class VecOfModAsParamBase[T <: Bundle, U <: Vec[T]](gen: => U)
    extends Module {
  val buildIt = gen
  lazy val io = new Bundle {
    val data = Vec(buildIt.length, buildIt(0).cloneType)
  }
  io.data <> buildIt
}

class VecOfModAsParam extends VecOfModAsParamBase[IoParent, Vec[IoParent]] (
  Vec.fill(2)(Module(new ModuleParent).io))

class VecOfModAsParamChild
    extends VecOfModAsParamBase[IoChild, Vec[IoChild]] (
  Vec.fill(2)(Module(new ModuleChild).io))

abstract class VecOfModViaTypeBase[T <: Bundle] extends Module {
  val buildIt: Vec[T]
  val io = new Bundle {
    lazy val data = Vec(buildIt.length, buildIt(0).cloneType)
  }
  def init {
    io.data <> buildIt
  }
}

class VecOfModViaType extends VecOfModViaTypeBase[IoParent] {
  val buildIt = Vec.fill(2)(Module(new ModuleParent).io)
  this.init
}

class VecOfModViaSeqBase[T <: Module, U <: Seq[T]](gen: => U) extends Module {
  val buildIt = gen
  val io = new Bundle {
    val data = Vec(buildIt.length, buildIt(0).io.cloneType)
  }
  (0 until buildIt.length).map(i => io.data(i) <> buildIt(i).io)
}

class VecOfModViaSeq extends VecOfModViaSeqBase[ModuleParent, Seq[ModuleParent]] (
  Seq.fill(2)(Module(new ModuleParent)))
