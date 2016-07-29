package chisel3Base.vecFlip

import chisel3._

class VecIOBundle(n: Int) extends Bundle {
  val i_a = Vec(n, Bool()).asInput
  // val i_b = Vec(n, Bool().asInput)
  val i_c = Vec(n,Bool(INPUT))

  val o_a = Vec(n, Bool()).asOutput
  // val o_b = Vec(n, Bool().asOutput)
  val o_c = Vec(n, Bool(OUTPUT))
  override def cloneType = new VecIOBundle(n).asInstanceOf[this.type]
}

class VecIO extends Module {
  val io = new VecIOBundle(2)
  io.o_a := io.i_a
  // io.o_b := io.i_b
  io.o_c := io.i_c
}

class VecIOFlip extends Module {
  val io = (new VecIOBundle(2)).flip
  io.i_a := io.o_a
  // io.i_b := io.o_b
  io.i_c := io.o_c
}
