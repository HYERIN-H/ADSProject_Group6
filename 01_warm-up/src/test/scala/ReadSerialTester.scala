// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package readserial

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec


/**
 *read serial tester
 */
class ReadSerialTester extends AnyFlatSpec with ChiselScalatestTester {

  // Define constants for clarity
  val IDLE: Int = 1
  val START_BIT: Int = 0
  val DATA_BITS: Int = 8

  /**
   * Helper function to transmit one byte of data serially.
   * Assumes the receiver is already in a state ready to receive (Idle).
   * @param dut The ReadSerial DUT.
   * @param dataByte The 8-bit UInt data to transmit (MSB first).
   * @param expectedValue The 8-bit UInt value expected on the 'data' output.
   */
  def transmitByte(dut: ReadSerial, dataByte: UInt, expectedValue: UInt): Unit = {
    dut.io.rxd.poke(START_BIT.U) // start with "0" bit
    dut.clock.step(1) // wait for 1clock to make sure initalization


    for (i <- DATA_BITS - 1 to 0 by -1) {
      val bit = (dataByte.litValue >> i) & 0x1

      dut.io.rxd.poke(bit.U) // rxd bit insert

      // Data is only valid on the cycle after the last bit is shifted
      if (i > 0) {
        dut.io.valid.expect(0.U)
      }
      dut.clock.step(1)
    }

    dut.io.valid.expect(1.U)
    dut.io.data.expect(expectedValue)

    dut.io.rxd.poke(IDLE.U) // Set bus high for idle state
    dut.clock.step(1)
    dut.io.valid.expect(0.U)
  }


  "ReadSerial" should "validate basic single byte transmission (0xAA)" in {
    test(new ReadSerial).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.reset_n.poke(1.U)
      dut.io.rxd.poke(IDLE.U)
      dut.clock.step(2)
      dut.io.reset_n.poke(0.U)

      // Test Case 1: Transmit 0xAA (10101010), Left shift
      val transmit_0xAA = "b10101010".U(8.W)
      val expected_0xAA = transmit_0xAA

      transmitByte(dut, transmit_0xAA, expected_0xAA)

      println("Test 1 Passed")
    }
  }

  "ReadSerial" should "validate transmission of 0xCC and immediate chaining (0x33)" in {
    test(new ReadSerial).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.reset_n.poke(1.U)
      dut.io.rxd.poke(IDLE.U)
      dut.clock.step(2)
      dut.io.reset_n.poke(0.U)

      // Transmit 0xCC (11001100)
      val transmit_0xCC = "b11001100".U(8.W)
      val expected_0xCC = transmit_0xCC
      transmitByte(dut, transmit_0xCC, expected_0xCC)

      // Continuous receiving data
      val transmit_0xFF = "b11111111".U(8.W)
      val expected_0xFF = "b11111111".U(8.W)

      // Cycle 1
      dut.io.rxd.poke(START_BIT.U)
      dut.clock.step(1)
      dut.io.valid.expect(0.U)

      // Cycles
      for (i <- DATA_BITS - 1 to 0 by -1) {
        val bit = (transmit_0xFF.litValue >> i) & 0x1
        dut.io.rxd.poke(bit.U)
        dut.clock.step(1)
      }

      // Final Cycle
      dut.io.valid.expect(1.U)
      dut.io.data.expect(expected_0xFF)
      dut.clock.step(1)

      println("Test 2 Passed")
    }
  }

  "ReadSerial" should "handle reset during transmission" in {
    test(new ReadSerial).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.reset_n.poke(1.U)
      dut.io.rxd.poke(IDLE.U)
      dut.clock.step(2)
      dut.io.reset_n.poke(0.U)

      // Start transmission (0xF0 = 11110000)
      dut.io.rxd.poke(START_BIT.U)
      dut.clock.step(1) // make sure Start bit received

      // Send 3 data bits (1, 1, 1)
      dut.io.rxd.poke(1.U); dut.clock.step(1)
      dut.io.rxd.poke(1.U); dut.clock.step(1)
      dut.io.rxd.poke(1.U); dut.clock.step(1)

      // ASSERT RESET (Reset goes high)
      dut.io.reset_n.poke(1.U)
      dut.io.valid.expect(0.U)
      dut.clock.step(1)

      // De-assert Reset
      dut.io.reset_n.poke(0.U)
      dut.io.rxd.poke(IDLE.U) // Bus goes back to idle
      dut.clock.step(2)

      // Start new transmission (0x0F = 00001111)
      val transmit_0x0F = "b00001111".U(8.W)
      val expected_0x0F = transmit_0x0F
      transmitByte(dut, transmit_0x0F, expected_0x0F)

      println("Test 3 Passed")
    }
  }
}