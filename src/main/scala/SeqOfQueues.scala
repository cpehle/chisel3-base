package chisel3Base

import chisel3._
import chisel3.util.Queue

class SecOfQueues extends Module {
  val io = new Bundle {
    val x = Bool(INPUT)
    val y = Bool(OUTPUT)
  }

  val soq = Seq.fill(2)(Module(new Queue(new QueueBundle, 2)))
}
