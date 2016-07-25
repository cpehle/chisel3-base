package chisel3Base

import chisel3._
import chisel3.iotesters.SteppedHWIOTester

class BooleanIO(n: Int) extends Bundle {
  val x = Vec(n, Bool(INPUT))
  val f = Bool(OUTPUT)
  override def cloneType = new BooleanIO(n).asInstanceOf[this.type]
}

abstract class Boolean(n: Int, f: Vec[Bool] => Bool) extends Module {
  val io = new BooleanIO(n)
  io.f := f(io.x)
}

class And extends Boolean(2, x => x.toBits.andR)
class Or  extends Boolean(2, x => x.toBits.orR)

class AndTester extends SteppedHWIOTester {
  val device_under_test = Module(new And)
  for (i <- 0 until 2) {
    poke(device_under_test.io.x(0), i)
    for (j <- 0 until 2) {
      poke(device_under_test.io.x(1), j)
      expect(device_under_test.io.f, i & j)
      step(1)
    }
  }
}

class OrTester extends SteppedHWIOTester {
  val device_under_test = Module(new Or)
  for (i <- 0 until 2) {
    poke(device_under_test.io.x(0), i)
    for (j <- 0 until 2) {
      poke(device_under_test.io.x(1), j)
      expect(device_under_test.io.f, i | j)
      step(1)
    }
  }
}
