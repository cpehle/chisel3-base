package chisel3Base

import chisel3._

// This test confirms how the toBits and fromBits methods will
// physically lay out a Bundle

class TestBundle extends Bundle {
  val a = Bool()
  val b = UInt(width = 15)
}

class TestBundleIsIO(n: Int) extends Module {
  val io = new Bundle {
    val out = Vec(n, new TestBundle)
    val in = UInt(width = out.width.get * n)
  }
  io.out := Vec(n, new TestBundle).fromBits(io.in)
}

class BundleFromBits extends TestBundleIsIO(2)
