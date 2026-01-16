// ADS I Class Project
// Pipelined RISC-V Core with Hazard Detection and Resolution
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 10/31/2025 by Tobias Jauch (tobias.jauch@rptu.de)

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import Assignment02._

// Test ADD operation
class ALUAddTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Add_Tester" should "test ADD operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      dut.io.operandA.poke(10.U)
      dut.io.operandB.poke(10.U)
      dut.io.operation.poke(ALUOp.ADD)
      dut.io.aluResult.expect(20.U)
      dut.clock.step(1)

      //ToDo: add more test cases for ADD operation
      // Corner Case: Overflow/Wraparound
      // 0xFFFFFFFF + 1 should result in 0
      dut.io.operandA.poke("hFFFFFFFF".U)
      dut.io.operandB.poke(1.U)
      dut.io.operation.poke(ALUOp.ADD)
      dut.io.aluResult.expect(0.U)
      dut.clock.step(1)

      dut.io.operandA.poke("h80000000".U)
      dut.io.operandB.poke("h80000000".U)
      dut.io.operation.poke(ALUOp.ADD)
      dut.io.aluResult.expect(0.U)
      dut.clock.step(1)

      dut.io.operandA.poke("hFFFFFFFB".U)
      dut.io.operandB.poke(5.U)
      dut.io.operation.poke(ALUOp.ADD)
      dut.io.aluResult.expect(0.U)
      dut.clock.step(1)

      dut.io.operandA.poke("hFFFFFFFF".U)
      dut.io.operandB.poke("hFFFFFFFF".U)
      dut.io.operation.poke(ALUOp.ADD)
      dut.io.aluResult.expect("hFFFFFFFE".U)
      dut.clock.step(1)
    }
  }
}

// ---------------------------------------------------
// ToDo: Add test classes for all other ALU operations
//---------------------------------------------------

class ALUSubTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Sub_Tester" should "test SUB operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      //10 - 10 = 0
      dut.io.operandA.poke(10.U)
      dut.io.operandB.poke(10.U)
      dut.io.operation.poke(ALUOp.SUB)
      dut.io.aluResult.expect(0.U)
      dut.clock.step(1)

      //0 - 1 = - 1 = hFFFFFFFF
      dut.io.operandA.poke(0.U)
      dut.io.operandB.poke(1.U)
      dut.io.operation.poke(ALUOp.SUB)
      dut.io.aluResult.expect("hFFFFFFFF".U)
      dut.clock.step(1)

      dut.io.operandA.poke(0.U)
      dut.io.operandB.poke("hFFFFFFFF".U)
      dut.io.operation.poke(ALUOp.SUB)
      dut.io.aluResult.expect(1.U)
      dut.clock.step(1)

      dut.io.operandA.poke(10.U)
      dut.io.operandB.poke("hFFFFFFFF".U)
      dut.io.operation.poke(ALUOp.SUB)
      dut.io.aluResult.expect(11.U)
      dut.clock.step(1)

      dut.io.operandA.poke("h80000000".U)
      dut.io.operandB.poke(1.U)
      dut.io.operation.poke(ALUOp.SUB)
      dut.io.aluResult.expect("h7FFFFFFF".U)
      dut.clock.step(1)
    }
  }
}

class ALUAndTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_And_Tester" should "test AND operation" in {
    test(new ALU) { dut =>
      dut.clock.setTimeout(0)

      dut.io.operandA.poke("h0000FFFF".U)
      dut.io.operandB.poke("h00001234".U)
      dut.io.operation.poke(ALUOp.AND)
      dut.io.aluResult.expect("h00001234".U)
      dut.clock.step(1)

      dut.io.operandA.poke("hABCDEF12".U)
      dut.io.operandB.poke("hFFFFFFFF".U)
      dut.io.operation.poke(ALUOp.AND)
      dut.io.aluResult.expect("hABCDEF12".U)
      dut.clock.step(1)

      dut.io.operandA.poke("hFFFFFFFF".U)
      dut.io.operandB.poke(0.U)
      dut.io.operation.poke(ALUOp.AND)
      dut.io.aluResult.expect(0.U)
      dut.clock.step(1)

      dut.io.operandA.poke("hAAAAAAAA".U) 
      dut.io.operandB.poke("h55555555".U) 
      dut.io.operation.poke(ALUOp.AND)
      dut.io.aluResult.expect(0.U)
      dut.clock.step(1)
    }
  }
}

class ALUOrTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Or_Tester" should "test OR operation" in {
    test(new ALU) { dut =>
      dut.clock.setTimeout(0)

      dut.io.operandA.poke("hF0F0F0F0".U)
      dut.io.operandB.poke("h0F0F0F0F".U)
      dut.io.operation.poke(ALUOp.OR)
      dut.io.aluResult.expect("hFFFFFFFF".U)
      dut.clock.step(1)

      dut.io.operandA.poke("h12345678".U)
      dut.io.operandB.poke(0.U)
      dut.io.operation.poke(ALUOp.OR)
      dut.io.aluResult.expect("h12345678".U)
      dut.clock.step(1)

      dut.io.operandA.poke("h00000000".U)
      dut.io.operandB.poke("hFFFFFFFF".U)
      dut.io.operation.poke(ALUOp.OR)
      dut.io.aluResult.expect("hFFFFFFFF".U)
      dut.clock.step(1)
    }
  }
}

class ALUXorTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_Xor_Tester" should "test XOR operation" in {
    test(new ALU) { dut =>
      dut.clock.setTimeout(0)

      dut.io.operandA.poke("hFFFF0000".U)
      dut.io.operandB.poke("hF0F0F0F0".U)
      dut.io.operation.poke(ALUOp.XOR)
      dut.io.aluResult.expect("h0F0FF0F0".U)
      dut.clock.step(1)

      dut.io.operandA.poke("hDEADBEEF".U)
      dut.io.operandB.poke("hDEADBEEF".U)
      dut.io.operation.poke(ALUOp.XOR)
      dut.io.aluResult.expect(0.U)
      dut.clock.step(1)

      dut.io.operandA.poke("hABCDEF12".U)
      dut.io.operandB.poke(0.U)
      dut.io.operation.poke(ALUOp.XOR)
      dut.io.aluResult.expect("hABCDEF12".U)
      dut.clock.step(1)

      dut.io.operandA.poke("h00000000".U)
      dut.io.operandB.poke("hFFFFFFFF".U)
      dut.io.operation.poke(ALUOp.XOR)
      dut.io.aluResult.expect("hFFFFFFFF".U)
      dut.clock.step(1)
    }
  }
}
