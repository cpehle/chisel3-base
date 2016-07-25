package chisel3Base

import chisel3._
import chisel3.util._
import chisel3.iotesters.SteppedHWIOTester

abstract class BooleanInside[T <: Boolean](genInside: => T) extends Module {
  val inside = genInside

  val io = inside.io.cloneType
  io <> inside.io
}

abstract class VecBooleanInside[T <: Vec[BooleanIO]](vecInside: => T)
    extends Module {
  val inside = vecInside
  val n = inside.length
  val io = new Bundle {
    val data = inside(0).cloneType
    val sel = UInt(width = log2Up(n))
  }

  (0 until n).map(i => inside(i).x := io.data.x)
  io.data.f := inside(io.sel).f
}

class AndInside extends BooleanInside(Module(new And))
class OrInside extends BooleanInside(Module(new Or))
class VecAndInside extends VecBooleanInside(Vec(2, Module(new And).io))

class AndInsideTester extends SteppedHWIOTester {
  val device_under_test = Module(new AndInside)
  for (i <- 0 until 2) {
    poke(device_under_test.io.x(0), i)
    for (j <- 0 until 2) {
      poke(device_under_test.io.x(1), j)
      expect(device_under_test.io.f, i & j)
      step(1)
    }
  }
}

class OrInsideTester extends SteppedHWIOTester {
  val device_under_test = Module(new OrInside)
  for (i <- 0 until 2) {
    poke(device_under_test.io.x(0), i)
    for (j <- 0 until 2) {
      poke(device_under_test.io.x(1), j)
      expect(device_under_test.io.f, i | j)
      step(1)
    }
  }
}
