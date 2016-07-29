package chisel3Base.blackBoxTests

import chisel3._

class MyBlackBoxIO(n: Int = 1) extends Bundle {
  val a = UInt(INPUT, width = n)
  val b = UInt(OUTPUT, width = n)
  override def cloneType = new MyBlackBoxIO(n).asInstanceOf[this.type]
}

trait ExplicitClkAndReset {
  val clk = Clock
  val reset = UInt(INPUT, width = 1)
}

trait Internals {
  val io = new MyBlackBoxIO
  io.b := io.a
}

trait BlackBoxInternals {
  val io = new MyBlackBoxIO with ExplicitClkAndReset
  io.b := io.a
}

class MyModule   extends Module   with Internals
class MyBlackBox extends BlackBox with Internals

class BlackBoxSubModule extends Module {
  val io = new Bundle {
    val data = Vec(2, new MyBlackBoxIO)
  }

  val mod = Module(new MyModule)
  val bbox = Module(new MyBlackBox)

  // Bulk connections
  io.data(0) <> mod.io
  // io.data(1) <> bbox.io              // <-- This fails!
  // bbox.io <> io.data(1)

  // Explicit connection of bbox
  bbox.io.a := io.data(1).a          // <-- These are ok
  io.data(1).b := bbox.io.b
}

class MyBlackBoxNoFlip(n: Int) extends BlackBox {
  val io = new MyBlackBoxIO(n)
}

class MyBlackBoxFlip(n: Int) extends BlackBox {
  val io = (new MyBlackBoxIO(n)).flip
}

class BlackBoxFlips extends Module {
  val bboxNoFlip = Module(new MyBlackBoxNoFlip(4))
  val bboxFlip = Module(new MyBlackBoxFlip(4))
  val io = new Bundle {}
}
