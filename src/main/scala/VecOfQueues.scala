import chisel3._
import chisel3.util.Queue
import chisel3.iotesters.SteppedHWIOTester

class QueueBundle extends Bundle {
  val x = Bool()
  val y = UInt(width = 10)
}

class VecOfQueues extends Module {
  val io = new Bundle {
    val x = Bool(INPUT)
    val y = Bool(OUTPUT)
  }

  val voq = Vec(Module(new Queue(new QueueBundle, 2)).io, 1)
}


class VecOfQueuesTester extends SteppedHWIOTester {
  val device_under_test = Module(new VecOfQueues)
  step(1)
}
