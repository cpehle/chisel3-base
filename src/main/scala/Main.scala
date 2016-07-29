package chisel3Base

import chisel3._
import chisel3.testers.TesterDriver
import java.io._
import chisel3Base.traitAndClassOrdering.TraitOrdering
import chisel3Base.blackBoxTests.BlackBoxSubModule

import issues._

object Builder {
  val dirTop = new File(".").getCanonicalPath()
  val dirBuild = s"${dirTop}/build"

  def do_all(dut: => () => Module) {
    val name = Chisel.Driver.elaborate(dut).name

    val s = Chisel.Driver.emit(dut)
    val fileFirrtl = s"${dirBuild}/${name}.fir"
    val writeFirrtl = new PrintWriter(new File(fileFirrtl))
    writeFirrtl.write(s)
    writeFirrtl.close()

    val circuit = firrtl.Parser.parse(s.split("\n"))
    val fileVerilog = s"${dirBuild}/${name}.v"
    val writer = new PrintWriter(new File(fileVerilog))
    val compiler = new firrtl.VerilogCompiler

    compiler.compile(circuit, Seq(), writer)
    writer.close()
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val argsArray = args.slice(1, args.length)
    val module = args(0)
    val res = args(0) match {
      case "And" => Builder.do_all           { () => new And               }
      case "Or" => Builder.do_all            { () => new Or                }
      case "VecOfQueues" => Builder.do_all   { () => new VecOfQueues       }
      case "SecOfQueues" => Builder.do_all   { () => new SecOfQueues       }
      case "AndInside" => Builder.do_all     { () => new AndInside         }
      case "OrInside" => Builder.do_all      { () => new OrInside          }
      case "VecAndInside" => Builder.do_all  { () => new VecAndInside      }
      case "VecModParam" => Builder.do_all   { () => new VecModParamTest   }
      case "VecModNoParam" => Builder.do_all { () => new VecModNoParamTest }
      case "BundleFromBits" =>Builder.do_all { () => new BundleFromBits    }
      case "VecOfModAsParam"=>Builder.do_all { () => new VecOfModAsParam   }
      case "VecOfModAsParamChild"=>Builder.do_all{()=>new VecOfModAsParamChild }
      case "VecOfModViaType"=>Builder.do_all { () => new VecOfModViaType   }
      case "VecOfModViaSeq" =>Builder.do_all { () => new VecOfModViaSeq    }
      case "TraitOrdering" => Builder.do_all { () => new TraitOrdering     }
      case "BlackBoxSub" =>   Builder.do_all { () => new BlackBoxSubModule }

      // case "And" => TesterDriver.execute { () => new AndTester }
      // case "Or" => TesterDriver.execute  { () => new OrTester }
      // case "VecOfQueues" => TesterDriver.execute {
      //   () => new VecOfQueuesTester }
      // case "AndInside" => TesterDriver.execute {
      //   () => new AndInsideTester }
      // case "OrInside" => TesterDriver.execute {
      //   () => new OrInsideTester }
      // case "VecAndInside" => TesterDriver.execute {
      //   () => new VecAndInsideTester }
    }
  }
}
