package chisel3Base

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

  val voq = Vec.fill(2)(Module(new Queue(new QueueBundle, 2)).io)
}


class VecOfQueuesTester extends SteppedHWIOTester {
  val device_under_test = Module(new VecOfQueues)
  step(1)
}
