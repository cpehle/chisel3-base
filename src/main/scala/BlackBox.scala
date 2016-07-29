package chisel3Base.blackBoxTests

import chisel3._

class MyBlackBoxIO extends Bundle {
  val a = Bool(INPUT)
  val b = Bool(OUTPUT)
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

  // Explicit connection of bbox
  // bbox.io.a := io.data(1).a          // <-- These are ok
  // io.data(1).b := bbox.io.b
}
