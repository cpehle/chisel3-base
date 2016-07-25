import chisel3._
import chisel3.testers.TesterDriver

object Main {
  def main(args: Array[String]): Unit = {
    val argsArray = args.slice(1, args.length)
    val res = args(0) match {
      case "And" => TesterDriver.execute { () => new AndTester }
      case "Or" => TesterDriver.execute  { () => new OrTester }
      case "VecOfQueues" => TesterDriver.execute {
        () => new VecOfQueuesTester }
      case "AndInside" => TesterDriver.execute {
        () => new AndInsideTester }
      case "OrInside" => TesterDriver.execute {
        () => new OrInsideTester }
    }
  }
}
