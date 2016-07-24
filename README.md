A bare-bones Chisel 3 project that works as a starting point for larger projects, based off of Chick Markley's (slightly less bare-bones) [`dsp-chisel3`](https://github.com/chick/dsp-chisel3).

Alternatively, a better approach may be to just use Jack Koenig's [`chisel3-skeleton`](https://github.com/jackkoenig/chisel3-skeleton) as this uses fewer dependencies, doesn't require all the testing infrastructure, and just generates the Verilog.

## Dependencies
Currently, all Chisel 3 development is not available in a Maven repository, so you must build and publish the necessary dependencies locally. For the following repositories, clone them and publish them locally:
* [chisel3](https://github.com/ucb-bar/chisel3) -- provides `chisel3`, `chiselfrontend`, and `coremacros`
* [chisel-testers](https://github.com/ucb-bar/chisel-testers) -- provides `chisel3.iotesters`
* [firrtl](https://github.com/ucb-bar/firrtl) -- provides `firrtl`
* [firrtl-interpreter](https://github.com/ucb-bar/firrtl-interpreter) -- provides `firrtl-interpreter`

For all of these run `sbt publish-local`. There may be some necessary ordering here, however. After publishing them locally, you will then see the associated directories in `~/.ivy2/local/edu.berkeley.cs/`.

## Usage
Modify `./build.sbt` with the appropriate `organization`, `version`, and `name` fields. You can then write your Chisel 3 HDL code. The rough structure is as follows:
``` scala
import chisel3._
import chisel3.iotesters.SteppedHWIOTester
import chisel3.testers.TesterDriver

class MyModule extends Module {}

class MyModuleTester extends SteppedHWIOTester {
  val device_under_test = Module(new MyModule)
}

object Tests {
  def main(args: Array[String]): Unit = {
    // Slice up the input args to 'run' so the first is the DUT
    val argsArray = args.slice(1, args.length)
    val res = args(0) match {
      case "MyModule" => TesterDriver.execute { () => new MyModuleTester }
    }
  }
}
```

An example is provided in [`Boolean.scala`](https://github.com/seldridge/chisel3-base/blob/master/src/main/scala/Boolean.scala).

## Notes
The testers provided by `chisel3.iotesters` are abstract and rely on a `val device_under_test` being defined.
