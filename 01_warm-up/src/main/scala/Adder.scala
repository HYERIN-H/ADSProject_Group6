// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package adder

import chisel3._
import chisel3.util._
import utest.*


/**
  * Half Adder Class
  *
  * Your task is to implement a basic half adder as presented in the lecture.
  * Each signal should only be one bit wide (inputs and outputs).
  * There should be no delay between input and output signals, we want to have
  * a combinational behaviour of the component.
  */
class HalfAdder extends Module{

  val io = IO(new Bundle {
    /*
     * TODO: Define IO ports of a half adder as presented in the lecture
     */
    val a = Input(UInt(1.W))
    val b = Input(UInt(1.W))
    val s = Output(UInt(1.W))
    val co = Output(UInt(1.W))
    })

  /*
   * TODO: Describe output behaviour based on the input values
   */
    io.s:=io.a^io.b
    io.co:=io.a&io.b
}

/**
  * Full Adder Class
  *
  * Your task is to implement a basic full adder. The component's behaviour should
  * match the characteristics presented in the lecture. In addition, you are only allowed
  * to use two half adders (use the class that you already implemented) and basic logic
  * operators (AND, OR, ...).
  * Each signal should only be one bit wide (inputs and outputs).
  * There should be no delay between input and output signals, we want to have
  * a combinational behaviour of the component.
  */
class FullAdder extends Module{

  val io = IO(new Bundle {
    /*
     * TODO: Define IO ports of a half adder as presented in the lecture
     */
    val a = Input(UInt(1.W))
    val b = Input(UInt(1.W))
    val ci = Input(UInt(1.W))
    val s = Output(UInt(1.W))
    val co = Output(UInt(1.W))
    })


  /*
   * TODO: Instanciate the two half adders you want to use based on your HalfAdder class
   */
    val halfa1 = Module(new HalfAdder)
    val halfa2 = Module(new HalfAdder)

  /*
   * TODO: Describe output behaviour based on the input values and the internal signals
   */
  halfa1.io.a := io.a
  halfa1.io.b := io.b
  halfa2.io.a := halfa1.io.s
  halfa2.io.b := io.ci

  io.s := halfa2.io.s
  io.co := halfa1.io.co|halfa2.io.co
}

/**
  * 4-bit Adder class
  *
  * Your task is to implement a 4-bit ripple-carry-adder. The component's behaviour should
  * match the characteristics presented in the lecture.  Remember: An n-bit adder can be
  * build using one half adder and n-1 full adders.
  * The inputs and the result should all be 4-bit wide, the carry-out only needs one bit.
  * There should be no delay between input and output signals, we want to have
  * a combinational behaviour of the component.
  */
class FourBitAdder extends Module{

  val io = IO(new Bundle {
    /*
     * TODO: Define IO ports of a 4-bit ripple-carry-adder as presented in the lecture
     */
    val a = Input(UInt(4.W))
    val b = Input(UInt(4.W))
    val s = Output(UInt(4.W))
    val co = Output(UInt(1.W))
  })

  /*
   * TODO: Instanciate the full adders and one half adderbased on the previously defined classes
   */
  val halfa = Module(new HalfAdder)
  val fulla1 = Module(new FullAdder)
  val fulla2 = Module(new FullAdder)
  val fulla3 = Module(new FullAdder)

  /*
   * TODO: Describe output behaviour based on the input values and the internal
   */
  halfa.io.a := io.a(0)
  halfa.io.b := io.b(0)
  fulla1.io.a := io.a(1)
  fulla1.io.b := io.b(1)
  fulla2.io.a := io.a(2)
  fulla2.io.b := io.b(2)
  fulla3.io.a := io.a(3)
  fulla3.io.b := io.b(3)

  io.s := Cat(fulla3.io.s, fulla2.io.s, fulla1.io.s, halfa.io.s)

  fulla1.io.ci := halfa.io.co
  fulla2.io.ci := fulla1.io.co
  fulla3.io.ci := fulla2.io.co
  io.co := fulla3.io.co

}
