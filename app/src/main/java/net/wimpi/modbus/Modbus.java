//License
/***
 * Java Modbus Library (jamod)
 * Copyright (c) 2002-2004, jamod development team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the author nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER AND CONTRIBUTORS ``AS
 * IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ***/
package net.wimpi.modbus;

/**
 * Interface defining all constants related to the
 * Modbus protocol.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public interface Modbus {


  /**
   * JVM flag for debug mode. Can be set passing the system property
   * net.wimpi.modbus.debug=false|true (-D flag to the jvm).
   */
  boolean debug = "true".equals(System.getProperty("net.wimpi.modbus.debug"));
  
  /**
   * Defines the class 0 function code
   * for <tt>read multiple registers</tt>.
   */
  int READ_MULTIPLE_REGISTERS = 3;

  /**
   * Defines the class 0 function code
   * for <tt>write multiple registers</tt>.
   */
  int WRITE_MULTIPLE_REGISTERS = 16;

  /**
   * Defines the class 1 function code
   * for <tt>read coils</tt>.
   */
  int READ_COILS = 1;

  /**
   * Defines a class 1 function code
   * for <tt>read input discretes</tt>.
   */
  int READ_INPUT_DISCRETES = 2;

  /**
   * Defines a class 1 function code
   * for <tt>read input registers</tt>.
   */
  int READ_INPUT_REGISTERS = 4;

  /**
   * Defines a class 1 function code
   * for <tt>write coil</tt>.
   */
  int WRITE_COIL = 5;

  /**
   * Defines a standard function code
   * for <tt>write multiple coils</tt>.
   */
  int WRITE_MULTIPLE_COILS = 15;

  /**
   * Defines a class 1 function code
   * for <tt>write single register</tt>.
   */
  int WRITE_SINGLE_REGISTER = 6;

  /**
   * Defines the byte representation of the coil state <b>on</b>.
   */
  int COIL_ON = (byte) 255;

  /**
   * Defines the byte representation of the coil state <b>pos</b>.
   */
  int COIL_OFF = 0;

  /**
   * Defines the word representation of the coil state <b>on</b>.
   */
  byte[] COIL_ON_BYTES = {(byte) COIL_ON, (byte) COIL_OFF};

  /**
   * Defines the word representation of the coil state <b>pos</b>.
   */
  byte[] COIL_OFF_BYTES = {(byte) COIL_OFF, (byte) COIL_OFF};

  /**
   * Defines the maximum number of bits in multiple read/write
   * of input discretes or coils (<b>2000</b>).
   */
  int MAX_BITS = 2000;

  /**
   * Defines the Modbus slave exception offset that is added to the
   * function code, to flag an exception.
   */
  int EXCEPTION_OFFSET = 128;			//the last valid function code is 127

  /**
   * Defines the Modbus slave exception type <tt>illegal function</tt>.
   * This exception code is returned if the slave:
   * <ul>
   *   <li>does not implement the function code <b>or</b></li>
   *   <li>is not in a state that allows it to process the function</li>
   * </ul>
   */
  int ILLEGAL_FUNCTION_EXCEPTION = 1;

  /**
   * Defines the Modbus slave exception type <tt>illegal data address</tt>.
   * This exception code is returned if the reference:
   * <ul>
   *   <li>does not exist on the slave <b>or</b></li>
   *   <li>the combination of reference and length exceeds the bounds
   *       of the existing registers.
   *   </li>
   * </ul>
   */
  int ILLEGAL_ADDRESS_EXCEPTION = 2;

  /**
   * Defines the Modbus slave exception type <tt>illegal data value</tt>.
   * This exception code indicates a fault in the structure of the data values
   * of a complex request, such as an incorrect implied length.<br>
   * <b>This code does not indicate a problem with application specific validity
   * of the value.</b>
   */
  int ILLEGAL_VALUE_EXCEPTION = 3;


  /**
   * Defines the default port number of Modbus
   * (=<tt>502</tt>).
   */
  int DEFAULT_PORT = 1030;

  /**
   * Defines the maximum message length in bytes
   * (=<tt>256</tt>).
   */
  int MAX_MESSAGE_LENGTH = 256;

  /**
   * Defines the default transaction identifier (=<tt>0</tt>).
   */
  int DEFAULT_TRANSACTION_ID = 0;

  /**
   * Defines the default protocol identifier (=<tt>0</tt>).
   */
  int DEFAULT_PROTOCOL_ID = 0;

  /**
   * Defines the default unit identifier (=<tt>0</tt>).
   */
  int DEFAULT_UNIT_ID = 0;

  /**
   * Defines the default setting for validity checking
   * in transactions (=<tt>true</tt>).
   */
  boolean DEFAULT_VALIDITYCHECK = true;

  /**
   * Defines the default setting for I/O operation timeouts
   * in milliseconds (=<tt>3000</tt>).
   */
  int DEFAULT_TIMEOUT = 3000;

  /**
   * Defines the default reconnecting setting for
   * transactions (=<tt>false</tt>).
   */
  boolean DEFAULT_RECONNECTING = false;

  /**
   * Defines the default amount of retires for opening
   * a connection (=<tt>3</tt>).
   */
  int DEFAULT_RETRIES = 3;

  /**
   * Defines the default number of msec to delay before transmission
   * (=<tt>50</tt>).
   */
  int DEFAULT_TRANSMIT_DELAY = 0;

  /**
   * Defines the maximum value of the transaction identifier.
   */
  int MAX_TRANSACTION_ID = (Short.MAX_VALUE * 2);


  /**
   * Defines the serial encoding "ASCII".
   */
  String SERIAL_ENCODING_ASCII = "ascii";

  /**
   * Defines the serial encoding "RTU".
   */
  String SERIAL_ENCODING_RTU = "rtu";

  /**
   * Defines the serial encoding "BIN".
   */
  String SERIAL_ENCODING_BIN = "bin";

  /**
   * Defines the default serial encoding (ASCII).
   */
  String DEFAULT_SERIAL_ENCODING = SERIAL_ENCODING_ASCII;

}//class Modbus

