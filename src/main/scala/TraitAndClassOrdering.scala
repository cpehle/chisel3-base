package chisel3Base.traitAndClassOrdering

import chisel3._

// This is more related to how composition of classes and traits
// results in the specific ordering of vals. This is critical if
// you're trying to construct specific data structures that will have
// a specific packed layout.

trait TraitA {
  val a = Bool()
}

trait TraitB {
  val b = Bool()
}

class BundleC extends Bundle {
  val c = Bool()
}

class BundleD extends BundleC {
  val d = Bool()
}

class TraitOrdering extends Module {
  val io = new BundleD with TraitA with TraitB
  // The packed ordering for `val io` will then be:
  //
  //   |-------+---+---+---+---|
  //   | Bit   | 3 | 2 | 1 | 0 |
  //   |-------+---+---+---+---|
  //   | Field | c | d | a | b |
  //   |-------+---+---+---+---|
  //
}
