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

class ALUSLLTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_SLL_Tester" should "test SLL operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      // Shift left by the low 5 bits of OperandB
      dut.io.operandA.poke("h40000001".U)
      dut.io.operandB.poke("h00100001".U)
      dut.io.operation.poke(ALUOp.SLL)
      dut.io.aluResult.expect("h80000002".U)

      // Wraparround
      dut.io.operandA.poke("hF0000001".U)
      dut.io.operandB.poke(1.U)
      dut.io.operation.poke(ALUOp.SLL)
      dut.io.aluResult.expect("hE0000002".U)
    }
  }
}

class ALUSRLTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_SRL_Tester" should "test SRL operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      // Shift negative number (0x80000000) right by the low 5 bits of OperandB -> should not keep sign bit (logic right shift)
      dut.io.operandA.poke("h80000000".U)
      dut.io.operandB.poke("h00100001".U)
      dut.io.operation.poke(ALUOp.SRL)
      dut.io.aluResult.expect("h40000000".U)

      // Wraparround
      dut.io.operandA.poke(1.U)
      dut.io.operandB.poke(2.U)
      dut.io.operation.poke(ALUOp.SRL)
      dut.io.aluResult.expect(0.U)
    }
  }
}

class ALUSRATest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_SRA_Tester" should "test SRA operation" in {
    test(new ALU) { dut =>
      dut.clock.setTimeout(0)

      // Shift negative number (0x80000000) right by 1 -> should keep sign bit
      dut.io.operandA.poke("h80000000".U)
      dut.io.operandB.poke(1.U)
      dut.io.operation.poke(ALUOp.SRA)
      dut.io.aluResult.expect("hC0000000".U) 
      dut.clock.step(1)

      // Shift amount > 31: only low 5 bits used (33 becomes 1)
      dut.io.operandA.poke("h00000004".U)
      dut.io.operandB.poke(33.U) // 33 % 32 = 1
      dut.io.operation.poke(ALUOp.SRA)
      dut.io.aluResult.expect(2.U)
      dut.clock.step(1)
    }
  }
}

class ALUSLTTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_SLT_Tester" should "test SLT operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      // OperandA(9) < Operand B(10) results in Ture (1)
      dut.io.operandA.poke(9.U)
      dut.io.operandB.poke(10.U) 
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect(1.U) // True
      dut.clock.step(1) 

      // OperandA(-1) < Operand B(1) results in Ture (1)
      dut.io.operandA.poke("hFFFFFFFF".U)
      dut.io.operandB.poke(1.U)
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect(1.U) // True
      dut.clock.step(1)

      // OperandA(-2) < Operand B(-1) results in Ture (1)
      dut.io.operandA.poke("hFFFFFFFE".U)
      dut.io.operandB.poke("hFFFFFFFF".U)
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect(1.U) // True
      dut.clock.step(1)

      // OperandA(1) < Operand B(-1) results in False (0)
      dut.io.operandA.poke(1.U)
      dut.io.operandB.poke("hFFFFFFFF".U)
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect(0.U) // False
      dut.clock.step(1)

      dut.io.operandA.poke(10.U)
      dut.io.operandB.poke(9.U) 
      dut.io.operation.poke(ALUOp.SLT)
      dut.io.aluResult.expect("h00000000".U) // True
      dut.clock.step(1) 
    }
  }
}

class ALUSLTUTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_SLTU_Tester" should "test SLTU operation" in {
    test(new ALU).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(0)

      // OperandA(9) < Operand B(10) results in True (1)
      dut.io.operandA.poke(9.U)
      dut.io.operandB.poke(10.U) 
      dut.io.operation.poke(ALUOp.SLTU)
      dut.io.aluResult.expect(1.U) // True
      dut.clock.step(1) 

      // OperandA(10) < Operand B(9) results in False (0)
      dut.io.operandA.poke(10.U)
      dut.io.operandB.poke(9.U)
      dut.io.operation.poke(ALUOp.SLTU)
      dut.io.aluResult.expect(0.U) // False
      dut.clock.step(1)

      // OperandA(-1) < Operand B(0) results in False (0)
      dut.io.operandA.poke("hFFFFFFFF".U)
      dut.io.operandB.poke(0.U)
      dut.io.operation.poke(ALUOp.SLTU)
      dut.io.aluResult.expect(0.U) // False
      dut.clock.step(1)
    }
  }
}

class ALUPassBTest extends AnyFlatSpec with ChiselScalatestTester {
  "ALU_PassB_Tester" should "test PASSB operation" in {
    test(new ALU) { dut =>
      dut.clock.setTimeout(0)

      dut.io.operandA.poke("hAAAAAAAA".U) // Noise
      dut.io.operandB.poke("h12345678".U)
      dut.io.operation.poke(ALUOp.PASSB)
      dut.io.aluResult.expect("h12345678".U)
      dut.clock.step(1)

      dut.io.operandA.poke("hFFFFFFFF".U) // Max noise
      dut.io.operandB.poke(0.U)
      dut.io.operation.poke(ALUOp.PASSB)
      dut.io.aluResult.expect(0.U)
      dut.clock.step(1)

      dut.io.operandA.poke(0.U)
      dut.io.operandB.poke("hFFFFFFFF".U)
      dut.io.operation.poke(ALUOp.PASSB)
      dut.io.aluResult.expect("hFFFFFFFF".U)
      dut.clock.step(1)

      dut.io.operandB.poke("h55555555".U)      
      dut.io.operandA.poke(0.U)
      dut.io.operation.poke(ALUOp.PASSB)
      dut.io.aluResult.expect("h55555555".U)
      dut.clock.step(1)
      
      dut.io.operandA.poke("hFFFFFFFF".U)
      dut.io.operation.poke(ALUOp.PASSB)
      dut.io.aluResult.expect("h55555555".U)
      dut.clock.step(1)
    }
  }
}