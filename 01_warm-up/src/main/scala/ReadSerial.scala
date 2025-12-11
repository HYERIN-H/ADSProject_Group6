// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package readserial

import chisel3._
import chisel3.util._
import firrtl.Utils.True


/** controller class */
class Controller extends Module{

  val io = IO(new Bundle {
    /*
     * TODO: Define IO ports of a the component as stated in the documentation
     */
    val reset_n = Input(UInt(1.W))
    val rxd = Input(UInt(1.W))
    val cnt_s = Input(UInt(1.W))
    val cnt_en = Output(UInt(1.W))
    val valid = Output(UInt(1.W))
    })

  // internal variables
  /*
   * TODO: Define internal variables (registers and/or wires), if needed
   */
  val isBusy = RegInit(false.B)

  // state machine
  /*
   * TODO: Describe functionality if the controller as a state machine
   */
  io.cnt_en := isBusy.asUInt
  io.valid  := 0.U // Default value

  when(io.reset_n === 1.U) {
    isBusy := false.B
  } .otherwise {

    // not busy (Idle state)
    when(!isBusy) {
      when(io.rxd === 0.U) {
        isBusy := true.B
      }
    }

      // busy (Receiving state)
      .otherwise {
        when(io.cnt_s === 1.U) {
          isBusy   := false.B
          io.valid := 1.U
        }
      }
  }
}


/** counter class */
class Counter extends Module{
  val io = IO(new Bundle {
    /* 
     * TODO: Define IO ports of a the component as stated in the documentation
     */
    val reset_n = Input(UInt(1.W))
    val cnt_s = Output(UInt(1.W))
    val cnt_en = Input(UInt(1.W))
    })

  // internal variables
  /* 
   * TODO: Define internal variables (registers and/or wires), if needed
   */
  val icounter = RegInit(0.U(4.W))

  // state machine
  /* 
   * TODO: Describe functionality if the counter as a state machine
   */

  io.cnt_s := 0.U // Default Value

  when(io.cnt_en === 1.U) // the first bit of receive data comes in
  {
    icounter := icounter + 1.U
  } .otherwise(icounter := 0.U)

  when(icounter === 8.U) // internal counter becomes 8
  {
    // icounter := 0.U
    io.cnt_s := 1.U
  } .otherwise(io.cnt_s :=0.U)
}

/** shift register class */
class ShiftRegister extends Module{
  
  val io = IO(new Bundle {
    /* 
     * TODO: Define IO ports of a the component as stated in the documentation
     */
    val rxd = Input(UInt(1.W))
    val data = Output(UInt(8.W))
    val valid = Input(UInt(1.W))
    val cnt_en = Input(UInt(1.W))
    })

  // internal variables
  /* 
   * TODO: Define internal variables (registers and/or wires), if needed
   */
  val shiftReg  =  RegInit(0.U(8.W))


  // functionality
  /* 
   * TODO: Describe functionality if the shift register
   */
  io.data := 0.U // Default Value

  when(io.cnt_en === 1.U)
  {
    shiftReg  :=  shiftReg(6,0)  ##  io.rxd
  }

  when(io.valid === 1.U)
  {
    io.data := shiftReg
  }
}

/** 
  * The last warm-up task deals with a more complex component. Your goal is to design a serial receiver.
  * It scans an input line (“serial bus”) named rxd for serial transmissions of data bytes. A transmission 
  * begins with a start bit ‘0’ followed by 8 data bits. The most significant bit (MSB) is transmitted first. 
  * There is no parity bit and no stop bit. After the last data bit has been transferred a new transmission 
  * (beginning with a start bit, ‘0’) may immediately follow. If there is no new transmission the bus line 
  * goes high (‘1’, this is considered the “idle” bus signal). In this case the receiver waits until the next 
  * transmission begins. The outputs of the design are an 8-bit parallel data signal and a valid signal. 
  * The valid signal goes high (‘1’) for one clock cycle after the last serial bit has been transmitted, 
  * indicating that a new data byte is ready.
  */
class ReadSerial extends Module {

  val io = IO(new Bundle {
    /* 
     * TODO: Define IO ports of a the component as stated in the documentation
     */
    val reset_n = Input(UInt(1.W))
    val rxd = Input(UInt(1.W))
    val valid = Output(UInt(1.W))
    val data = Output(UInt(8.W))
    //val cnt_s = Input(UInt(1.W))
    //val cnt_en = Output(UInt(1.W))
  })


  // instanciation of modules
  /* 
   * TODO: Instanciate the modules that you need
   */
  val Controller = Module(new Controller)
  val Counter = Module(new Counter)
  val ShiftRegister = Module(new ShiftRegister)

  // connections between modules
  /* 
   * TODO: connect the signals between the modules
   */
  Counter.io.cnt_en := Controller.io.cnt_en
  Controller.io.cnt_s := Counter.io.cnt_s
  ShiftRegister.io.cnt_en := Controller.io.cnt_en
  ShiftRegister.io.valid  := Controller.io.valid
  Controller.io.reset_n := io.reset_n
  Counter.io.reset_n := io.reset_n
  Controller.io.rxd := io.rxd
  ShiftRegister.io.rxd := io.rxd
  io.valid := Controller.io.valid
  io.data := ShiftRegister.io.data

  val shiftReg = RegInit(0.U(8.W))

  // global I/O 
  /* 
   * TODO: Describe output behaviour based on the input values and the internal signals
   */
  io.valid := Controller.io.valid
  io.data := ShiftRegister.io.data
}

object ReadSerial extends App {
  println(getVerilogString(new ReadSerial))
}