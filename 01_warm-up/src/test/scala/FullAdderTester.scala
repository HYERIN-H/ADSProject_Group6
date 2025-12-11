// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package adder

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec


/**
 * Full adder tester
 * Use the truth table from the exercise sheet to test all possible input combinations and the corresponding results exhaustively
 */
class FullAdderTester extends AnyFlatSpec with ChiselScalatestTester {

  "FullAdder" should "work" in {
    test(new FullAdder).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      // 0 0 0 -> 0 0
      dut.io.a.poke(0.U)
      dut.io.b.poke(0.U)
      dut.io.ci.poke(0.U)
      dut.io.s.expect(0.U)
      dut.io.co.expect(0.U)

      // 0 0 1 -> 1 0
      dut.io.a.poke(0.U)
      dut.io.b.poke(0.U)
      dut.io.ci.poke(1.U)
      dut.io.s.expect(1.U)
      dut.io.co.expect(0.U)

      // 0 1 0 -> 1 0
      dut.io.a.poke(0.U)
      dut.io.b.poke(1.U)
      dut.io.ci.poke(0.U)
      dut.io.s.expect(1.U)
      dut.io.co.expect(0.U)

      // 0 1 1 -> 0 1
      dut.io.a.poke(0.U)
      dut.io.b.poke(1.U)
      dut.io.ci.poke(1.U)
      dut.io.s.expect(0.U)
      dut.io.co.expect(1.U)

      // 1 0 0 -> 1 0
      dut.io.a.poke(1.U)
      dut.io.b.poke(0.U)
      dut.io.ci.poke(0.U)
      dut.io.s.expect(1.U)
      dut.io.co.expect(0.U)

      // 1 0 1 -> 0 1
      dut.io.a.poke(1.U)
      dut.io.b.poke(0.U)
      dut.io.ci.poke(1.U)
      dut.io.s.expect(0.U)
      dut.io.co.expect(1.U)

      // 1 1 0 -> 0 1
      dut.io.a.poke(1.U)
      dut.io.b.poke(1.U)
      dut.io.ci.poke(0.U)
      dut.io.s.expect(0.U)
      dut.io.co.expect(1.U)

      // 1 1 1 -> 1 1
      dut.io.a.poke(1.U)
      dut.io.b.poke(1.U)
      dut.io.ci.poke(1.U)
      dut.io.s.expect(1.U)
      dut.io.co.expect(1.U)

    }
  }
}