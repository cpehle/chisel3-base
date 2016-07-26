package chisel3Base

import chisel3._

// This test confirms how the toBits and fromBits methods will
// physically lay out a Bundle

class TestBundle extends Bundle {
  val a = Bool()
  val b = UInt(width = 15)
  val c = UInt(width = 4)
}

class TestBundleIsIO extends Module {
  val io = new Bundle {
    val in = new TestBundle
    val out = new TestBundle
  }
}

class BundleFromBits extends TestBundleIsIO {
  val bits = Wire(UInt(width = io.width))

  bits := io.in.toBits
  io.out.fromBits(bits)
}

class BundleToBits extends TestBundleIsIO {
  val bits = Wire(UInt(width = io.width))

  bits := io.in.toBits
  io.out.toBits := bits
}
