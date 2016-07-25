import chisel3._
import chisel3.iotesters.SteppedHWIOTester

abstract class BooleanInside[T <: BooleanIO](a: => T) extends Module {
  val io = new BooleanIO(2)
  io <> a
}

class AndInside extends BooleanInside(Module(new And).io)
class OrInside extends BooleanInside(Module(new Or).io)

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
