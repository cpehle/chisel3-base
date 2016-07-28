package issues

// Chisel3 #247

import chisel3._
import chisel3.util._

class PassThroughBundle extends Bundle {
  val x = Bool(INPUT)
  val y = Bool(OUTPUT)
}

class PassThrough extends Module {
  val io = new PassThroughBundle
  io.y := io.x
}

class VecModParam[T <: Vec[PassThroughBundle]](gen: => T) extends Module {
  val inside = gen
  val n = inside.length
  val io = new Bundle {
    val data = inside(0).cloneType
    val sel = UInt(width = log2Up(n))
  }

  (0 until n).map(i => inside(i).x := io.data.x)
  io.data.y := inside(io.sel).y
}

class VecModParamTest extends VecModParam(Vec(2, Module(new PassThrough).io))
// class VecModParamTest extends VecModParam(Vec.fill(2)(Module(new PassThrough).io))

class VecModNoParamTest extends Module {
  val n = 2
  val inside = Vec.fill(n)(Module(new PassThrough).io)
  // val inside = Vec(n, Module(new PassThrough).io)
  val io = new Bundle {
    val data = inside(0).cloneType
    val sel = UInt(width = log2Up(n))
  }

  (0 until n).map(i => inside(i).x := io.data.x)
  io.data.y := inside(io.sel).y
}
