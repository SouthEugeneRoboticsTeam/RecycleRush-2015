// GyroITG3200 I2C device class header file
// Based on InvenSense ITG-3200 datasheet rev. 1.4, 3/30/2010 (PS-ITG-3200A-00-01.4)
// Original work by 7/31/2011 by Jeff Rowberg <jeff@rowberg.net>
// Java implementation for First Robotics Competition using WPILibj 
// 1/27/2015 by Joe Bussell <joe dot bussell at gmail dot com>
// Updates should (hopefully) always be available at https://github.com/bussell
//
// Changelog:
//     2011-07-31 - initial release

/* ============================================
GyroITG3200 device library code is placed under the MIT license
Copyright (c) 2015 Joe Bussell
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
===============================================
*/

package org.usfirst.frc.team2521.robot.subsystems;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.I2C;

/**
 * @author Joe Bussell
 * With thanks to the c++ version authors at: https://github.com/jrowberg/i2cdevlib/tree/master/Arduino/ITG3200
 *
 */
public class GyroITG3200 extends SensorBase implements PIDSource, LiveWindowSendable 
{
	byte devAddr;
	byte buffer[] = new byte[6];
	
	I2C m_i2c;
    		
	/** Default constructor, uses default I2C address.
	 * @see ITG3200_DEFAULT_ADDRESS
	 */
	public GyroITG3200( I2C.Port port )
	{
	    devAddr = ITG3200_DEFAULT_ADDRESS;

		m_i2c = new I2C(port, devAddr);

		// ToDo: This report is incorrect.  Need to create instance for I2C ITG3200 Gyro
		UsageReporting.report(tResourceType.kResourceType_I2C, tInstances.kADXL345_I2C);
		LiveWindow.addSensor("ITG3200_Gyro_I2C", port.getValue(), this);
	}
	
	/** Specific address constructor.
	 * @param address I2C address
	 * @see ITG3200_DEFAULT_ADDRESS
	 * @see ITG3200_ADDRESS_AD0_LOW
	 * @see ITG3200_ADDRESS_AD0_HIGH
	 */
	public GyroITG3200( I2C.Port port, byte address )
	{
	    devAddr = address;

		m_i2c = new I2C(port, devAddr);

		// ToDo: This report is incorrect.  Need to create instance for I2C ITG3200 Gyro
		UsageReporting.report(tResourceType.kResourceType_I2C, tInstances.kADXL345_I2C);
		LiveWindow.addSensor("ITG3200_Gyro_I2C", port.getValue(), this);
	}
	 
	/** Power on and prepare for general usage.
	 * This will activate the gyroscope, so be sure to adjust the power settings
	 * after you call this method if you want it to enter standby mode, or another
	 * less demanding mode of operation. This also sets the gyroscope to use the
	 * X-axis gyro for a clock source. Note that it doesn't have any delays in the
	 * routine, which means you might want to add ~50ms to be safe if you happen
	 * to need to read gyro data immediately after initialization. The data will
	 * flow in either case, but the first reports may have higher error offsets.
	 */
	public void initialize()
	{
	    setFullScaleRange( ITG3200_FULLSCALE_2000 );
	    setClockSource( ITG3200_CLOCK_PLL_XGYRO );
	}
	
	/** Verify the I2C connection.
	 * Make sure the device is connected and responds as expected.
	 * @return True if connection is valid, false otherwise
	 */
	public boolean testConnection()
	{
	    return getDeviceID() == 0b110100;
	}
	
	private void writeBit( byte register, byte bit, boolean value )
	{
		m_i2c.read( register, 1, buffer);
		byte newValue = (byte) ( value ? (buffer[0] | (1 << bit )) 
				                       : ( buffer[0] & ~(1 << bit ) ) );
		m_i2c.write( register, newValue );		
	}
	
	// this routine should update the original byte with the new data properly shifted to the correct bit location
	public static byte updateByte( byte original, int bit, int numBits, byte value )
	{
		byte x = (byte) ( 0 << (bit - 1) );
		byte y = (byte) ( 0 >> (8 - numBits ) );
		byte mask = (byte) ( x | y );
		byte maskedOriginal = (byte) ( original & mask );
		
		byte shiftedValue = (byte) ( (value & 0xFF) << bit );		
		
		byte result = (byte) ( shiftedValue | maskedOriginal );
				
		return result;
	}
	
	// 
    // I2Cdev::writeBits(devAddr, ITG3200_RA_WHO_AM_I, ITG3200_DEVID_BIT, ITG3200_DEVID_LENGTH, id);
	private void writeBits( byte register, int bit, int numBits, byte value )
	{
		m_i2c.read( register, 1, buffer );
		byte newValue = updateByte( buffer[0], bit, numBits, value );
		m_i2c.write( register, newValue );
	}
	
	private boolean readBit( byte register, byte bit )
	{
		m_i2c.read( register, 1, buffer);
	    return ( buffer[0] & bit) != 0;
	}
	
	// Get n bits from the byte to form a byte slice
	private byte getBits( byte bitField, int bit, int numBits )
	{
		byte result = 0;
		
		result = (byte) ( bitField << (bit - 1) );
		result = (byte) ( result >>> (8 - numBits) );
		
		return result;
	}

	private byte getRegisterByte( byte register ) 
	{
		m_i2c.read( register, 1, buffer );
	    return buffer[0];
	}

	/** Get specified bits from the specified register.
	 * Form a new value from 
	 * a byte (0b10110100) 
	 * get the 3rd bit
	 * request 6 bits
	 * and you should get a new byte (0b00110100).
	 */
	private byte getRegisterBits( byte register, int bit, int numBits )
	{
		byte containingByte = getRegisterByte( register );
		return getBits( containingByte, bit, numBits );		
	}
	
	// WHO_AM_I register
	/** Get Device ID.
	 * This register is used to verify the identity of the device (0b110100).
	 * @return Device ID (should be 0x34, 52 dec, 64 oct)
	 * @see ITG3200_RA_WHO_AM_I
	 * @see ITG3200_RA_DEVID_BIT
	 * @see ITG3200_RA_DEVID_LENGTH
	 */
	public byte getDeviceID() 
	{
		return getRegisterBits( ITG3200_RA_WHO_AM_I, ITG3200_DEVID_BIT, ITG3200_DEVID_LENGTH );
	}
	
	/** Set Device ID.
	 * Write a new ID into the WHO_AM_I register (no idea why this should ever be
	 * necessary though).
	 * @param id New device ID to set.
	 * @see getDeviceID()
	 * @see ITG3200_RA_WHO_AM_I
	 * @see ITG3200_RA_DEVID_BIT
	 * @see ITG3200_RA_DEVID_LENGTH
	 */
	public void setDeviceID(byte id)
	{
	    writeBits( ITG3200_RA_WHO_AM_I, ITG3200_DEVID_BIT, ITG3200_DEVID_LENGTH, id );
	}
	 
	// SMPLRT_DIV register

	/** Get sample rate.
	 * This register determines the sample rate of the ITG-3200 gyros. The gyros'
	 * outputs are sampled internally at either 1kHz or 8kHz, determined by the
	 * DLPF_CFG setting (see register 22). This sampling is then filtered digitally
	 * and delivered into the sensor registers after the number of cycles determined
	 * by this register. The sample rate is given by the following formula:
	 *
	 * F_sample = F_internal / (divider+1), where F_internal is either 1kHz or 8kHz
	 *
	 * As an example, if the internal sampling is at 1kHz, then setting this
	 * register to 7 would give the following:
	 *
	 * F_sample = 1kHz / (7 + 1) = 125Hz, or 8ms per sample
	 *
	 * @return Current sample rate
	 * @see setDLPFBandwidth()
	 * @see ITG3200_RA_SMPLRT_DIV
	 */
	public byte getRate()
	{
		return getRegisterByte( ITG3200_RA_SMPLRT_DIV );
	    // I2Cdev::readByte(devAddr, ITG3200_RA_SMPLRT_DIV, buffer);
	    // return buffer[0];
	}
	
	/** Set sample rate.
	 * @param rate New sample rate
	 * @see getRate()
	 * @see setDLPFBandwidth()
	 * @see ITG3200_RA_SMPLRT_DIV
	 */
	public void setRate(byte rate)
	{
		m_i2c.write( ITG3200_RA_SMPLRT_DIV, rate );
	    // I2Cdev::writeByte(devAddr, ITG3200_RA_SMPLRT_DIV, rate);
	}
	 
	// DLPF_FS register
	/** Full-scale range.
	 * The FS_SEL parameter allows setting the full-scale range of the gyro sensors,
	 * as described in the table below. The power-on-reset value of FS_SEL is 00h.
	 * Set to 03h for proper operation.
	 *
	 * 0 = Reserved
	 * 1 = Reserved
	 * 2 = Reserved
	 * 3 = +/- 2000 degrees/sec
	 *
	 * @return Current full-scale range setting
	 * @see ITG3200_FULLSCALE_2000
	 * @see ITG3200_RA_DLPF_FS
	 * @see ITG3200_DF_FS_SEL_BIT
	 * @see ITG3200_DF_FS_SEL_LENGTH
	 */
	public byte getFullScaleRange()
	{
		return getRegisterBits( ITG3200_RA_DLPF_FS, ITG3200_DF_FS_SEL_BIT, ITG3200_DF_FS_SEL_LENGTH );
	    // I2Cdev::readBits(devAddr, ITG3200_RA_DLPF_FS, ITG3200_DF_FS_SEL_BIT, ITG3200_DF_FS_SEL_LENGTH, buffer);
	}
	
	/** Set full-scale range setting.
	 * @param range New full-scale range value
	 * @see getFullScaleRange()
	 * @see ITG3200_FULLSCALE_2000
	 * @see ITG3200_RA_DLPF_FS
	 * @see ITG3200_DF_FS_SEL_BIT
	 * @see ITG3200_DF_FS_SEL_LENGTH
	 */
	public void setFullScaleRange(byte range)
	{
		m_i2c.write( ITG3200_RA_DLPF_FS, range);
		// ToDo: mask the output such that we only change the bits of interest
	    writeBits( ITG3200_RA_DLPF_FS, ITG3200_DF_FS_SEL_BIT, ITG3200_DF_FS_SEL_LENGTH, range );
	}
	
	/** Get digital low-pass filter bandwidth.
	 * The DLPF_CFG parameter sets the digital low pass filter configuration. It
	 * also determines the internal sampling rate used by the device as shown in
	 * the table below.
	 *
	 * DLPF_CFG | Low-Pass Filter Bandwidth | Internal Sample Rate
	 * ---------+---------------------------+---------------------
	 * 0        | 256Hz                     | 8kHz
	 * 1        | 188Hz                     | 1kHz
	 * 2        | 98Hz                      | 1kHz
	 * 3        | 42Hz                      | 1kHz
	 * 4        | 20Hz                      | 1kHz
	 * 5        | 10Hz                      | 1kHz
	 * 6        | 5Hz                       | 1kHz
	 * 7        | Reserved                  | Reserved
	 *
	 * @return DLFP bandwidth setting
	 * @see ITG3200_RA_DLPF_FS
	 * @see ITG3200_DF_DLPF_CFG_BIT
	 * @see ITG3200_DF_DLPF_CFG_LENGTH
	 */
	public byte getDLPFBandwidth() 
	{
		return getRegisterBits( ITG3200_RA_DLPF_FS, ITG3200_DF_DLPF_CFG_BIT, ITG3200_DF_DLPF_CFG_LENGTH );
	    // I2Cdev::readBits(devAddr, ITG3200_RA_DLPF_FS, ITG3200_DF_DLPF_CFG_BIT, ITG3200_DF_DLPF_CFG_LENGTH, buffer);
	}
	
	/** Set digital low-pass filter bandwidth.
	 * @param bandwidth New DLFP bandwidth setting
	 * @see getDLPFBandwidth()
	 * @see ITG3200_DLPF_BW_256
	 * @see ITG3200_RA_DLPF_FS
	 * @see ITG3200_DF_DLPF_CFG_BIT
	 * @see ITG3200_DF_DLPF_CFG_LENGTH
	 */
	public void setDLPFBandwidth(byte bandwidth)
	{
		// m_i2c.write( ITG3200_RA_DLPF_FS, bandwidth);
		// ToDo: mask the output such that we only change the bits of interest
	    writeBits( ITG3200_RA_DLPF_FS, ITG3200_DF_DLPF_CFG_BIT, ITG3200_DF_DLPF_CFG_LENGTH, bandwidth );
	}
	 
	// INT_CFG register
	
	/** Get interrupt logic level mode.
	 * Will be set 0 for active-high, 1 for active-low.
	 * @return Current interrupt mode (0=active-high, 1=active-low)
	 * @see ITG3200_RA_INT_CFG
	 * @see ITG3200_INTCFG_ACTL_BIT
	 */
	public boolean getInterruptMode()
	{
		return readBit( ITG3200_RA_INT_CFG, ITG3200_INTCFG_ACTL_BIT );
	    // I2Cdev::readBit(devAddr, ITG3200_RA_INT_CFG, ITG3200_INTCFG_ACTL_BIT, buffer);
	}
	
	/** Set interrupt logic level mode.
	 * @param mode New interrupt mode (0=active-high, 1=active-low)
	 * @see getInterruptMode()
	 * @see ITG3200_RA_INT_CFG
	 * @see ITG3200_INTCFG_ACTL_BIT
	 */
	public void setInterruptMode(boolean mode)
	{
	    writeBit( ITG3200_RA_INT_CFG, ITG3200_INTCFG_ACTL_BIT, mode );
	}
	
	/** Get interrupt drive mode.
	 * Will be set 0 for push-pull, 1 for open-drain.
	 * @return Current interrupt drive mode (0=push-pull, 1=open-drain)
	 * @see ITG3200_RA_INT_CFG
	 * @see ITG3200_INTCFG_OPEN_BIT
	 */
	public boolean getInterruptDrive()
	{
		return readBit( ITG3200_RA_INT_CFG, ITG3200_INTCFG_OPEN_BIT );
	}
	
	/** Set interrupt drive mode.
	 * @param drive New interrupt drive mode (0=push-pull, 1=open-drain)
	 * @see getInterruptDrive()
	 * @see ITG3200_RA_INT_CFG
	 * @see ITG3200_INTCFG_OPEN_BIT
	 */
	public void setInterruptDrive(boolean drive)
	{
	    writeBit( ITG3200_RA_INT_CFG, ITG3200_INTCFG_OPEN_BIT, drive );
	}

	/** Get interrupt latch mode.
	 * Will be set 0 for 50us-pulse, 1 for latch-until-int-cleared.
	 * @return Current latch mode (0=50us-pulse, 1=latch-until-int-cleared)
	 * @see ITG3200_RA_INT_CFG
	 * @see ITG3200_INTCFG_LATCH_INT_EN_BIT
	 */
	public boolean getInterruptLatch()
	{
		return readBit( ITG3200_RA_INT_CFG, ITG3200_INTCFG_LATCH_INT_EN_BIT );
	}
	
	/** Set interrupt latch mode.
	 * @param latch New latch mode (0=50us-pulse, 1=latch-until-int-cleared)
	 * @see getInterruptLatch()
	 * @see ITG3200_RA_INT_CFG
	 * @see ITG3200_INTCFG_LATCH_INT_EN_BIT
	 */
	public void setInterruptLatch(boolean latch)
	{
	    writeBit( ITG3200_RA_INT_CFG, ITG3200_INTCFG_LATCH_INT_EN_BIT, latch );
	}
	
	/** Get interrupt latch clear mode.
	 * Will be set 0 for status-read-only, 1 for any-register-read.
	 * @return Current latch clear mode (0=status-read-only, 1=any-register-read)
	 * @see ITG3200_RA_INT_CFG
	 * @see ITG3200_INTCFG_INT_ANYRD_2CLEAR_BIT
	 */
	public boolean getInterruptLatchClear()
	{
		return readBit( ITG3200_RA_INT_CFG, ITG3200_INTCFG_INT_ANYRD_2CLEAR_BIT );
	}
	
	/** Set interrupt latch clear mode.
	 * @param clear New latch clear mode (0=status-read-only, 1=any-register-read)
	 * @see getInterruptLatchClear()
	 * @see ITG3200_RA_INT_CFG
	 * @see ITG3200_INTCFG_INT_ANYRD_2CLEAR_BIT
	 */
	public void setInterruptLatchClear(boolean clear) 
	{
	    writeBit( ITG3200_RA_INT_CFG, ITG3200_INTCFG_INT_ANYRD_2CLEAR_BIT, clear );
	}
	
	/** Get "device ready" interrupt enabled setting.
	 * Will be set 0 for disabled, 1 for enabled.
	 * @return Current interrupt enabled setting
	 * @see ITG3200_RA_INT_CFG
	 * @see ITG3200_INTCFG_ITG_RDY_EN_BIT
	 */
	public boolean getIntDeviceReadyEnabled()
	{
	    return readBit( ITG3200_RA_INT_CFG, ITG3200_INTCFG_ITG_RDY_EN_BIT );
	}
	
	/** Set "device ready" interrupt enabled setting.
	 * @param enabled New interrupt enabled setting
	 * @see getIntDeviceReadyEnabled()
	 * @see ITG3200_RA_INT_CFG
	 * @see ITG3200_INTCFG_ITG_RDY_EN_BIT
	 */
	public void setIntDeviceReadyEnabled( boolean enabled )
	{
	    writeBit( ITG3200_RA_INT_CFG, ITG3200_INTCFG_ITG_RDY_EN_BIT, enabled );
	}
	
	/** Get "data ready" interrupt enabled setting.
	 * Will be set 0 for disabled, 1 for enabled.
	 * @return Current interrupt enabled setting
	 * @see ITG3200_RA_INT_CFG
	 * @see ITG3200_INTCFG_RAW_RDY_EN_BIT
	 */
	public boolean getIntDataReadyEnabled() 
	{
	    return readBit( ITG3200_RA_INT_CFG, ITG3200_INTCFG_RAW_RDY_EN_BIT );
	}
	
	/** Set "data ready" interrupt enabled setting.
	 * @param enabled New interrupt enabled setting
	 * @see getIntDataReadyEnabled()
	 * @see ITG3200_RA_INT_CFG
	 * @see ITG3200_INTCFG_RAW_RDY_EN_BIT
	 */
	public void setIntDataReadyEnabled( boolean enabled )
	{
	    writeBit( ITG3200_RA_INT_CFG, ITG3200_INTCFG_RAW_RDY_EN_BIT, enabled );
	}
	 
	// INT_STATUS register
	
	/** Get Device Ready interrupt status.
	 * The ITG_RDY interrupt indicates that the PLL is ready and gyroscopic data can
	 * be read.
	 * @return Device Ready interrupt status
	 * @see ITG3200_RA_INT_STATUS
	 * @see ITG3200_INTSTAT_RAW_DATA_READY_BIT
	 */
	public boolean getIntDeviceReadyStatus()
	{
		return readBit( ITG3200_RA_INT_STATUS, ITG3200_INTSTAT_ITG_RDY_BIT );
	}
	
	/** Get Data Ready interrupt status.
	 * In normal use, the RAW_DATA_RDY interrupt is used to determine when new
	 * sensor data is available in and of the sensor registers (27 to 32).
	 * @return Data Ready interrupt status
	 * @see ITG3200_RA_INT_STATUS
	 * @see ITG3200_INTSTAT_RAW_DATA_READY_BIT
	 */
	public boolean getIntDataReadyStatus()
	{
		return readBit( ITG3200_RA_INT_STATUS, ITG3200_INTSTAT_RAW_DATA_READY_BIT );
	}
	
	
	// TEMP_OUT_* registers
	/** Get current internal temperature.
	 * @return Temperature reading in 16-bit 2's complement format
	 * @see ITG3200_RA_TEMP_OUT_H
	 */
	public short getTemperature()
	{
		m_i2c.read( ITG3200_RA_TEMP_OUT_H, 2, buffer);
	    return (short) ( ((short)(buffer[0]) << 8) | (short)buffer[1] );
	}
	 
	// GYRO_*OUT_* registers

	public static class AllAxes 
	{
		public short XAxis;
		public short YAxis;
		public short ZAxis;
	}
	
	/** Get 3-axis gyroscope readings.
	 * @param x 16-bit signed integer container for X-axis rotation
	 * @param y 16-bit signed integer container for Y-axis rotation
	 * @param z 16-bit signed integer container for Z-axis rotation
	 * @see ITG3200_RA_GYRO_XOUT_H
	 */
	public AllAxes getRotation()
	{
		AllAxes data = new AllAxes();
		m_i2c.read( ITG3200_RA_GYRO_XOUT_H, 6, buffer);
	    data.XAxis = (short) ( (((short)buffer[0]) << 8) | buffer[1] );
	    data.YAxis = (short) ( (((short)buffer[2]) << 8) | buffer[3] );
	    data.ZAxis = (short) ( (((short)buffer[4]) << 8) | buffer[5] );
	    return data;
	}
	
	
	/** Get X-axis gyroscope reading.
	 * @return X-axis rotation measurement in 16-bit 2's complement format
	 * @see ITG3200_RA_GYRO_XOUT_H
	 */
	public short getRotationX()
	{
		m_i2c.read( ITG3200_RA_GYRO_XOUT_H, 2, buffer );
	    return (short) ( (((short)buffer[0]) << 8) | buffer[1] );
	}
	
	/** Get Y-axis gyroscope reading.
	 * @return Y-axis rotation measurement in 16-bit 2's complement format
	 * @see ITG3200_RA_GYRO_YOUT_H
	 */
	public short getRotationY()
	{
		m_i2c.read( ITG3200_RA_GYRO_YOUT_H, 2, buffer);
	    return (short) ( (((short)buffer[0]) << 8) | buffer[1] );
	}
	
	/** Get Z-axis gyroscope reading.
	 * @return Z-axis rotation measurement in 16-bit 2's complement format
	 * @see ITG3200_RA_GYRO_ZOUT_H
	 */
	public short getRotationZ()
	{
		m_i2c.read( ITG3200_RA_GYRO_ZOUT_H, 2, buffer);
	    return (short) ( (((short)buffer[0]) << 8) | buffer[1] );
	}
	 
	// PWR_MGM register
	
	/** Trigger a full device reset.
	 * A small delay of ~50ms may be desirable after triggering a reset.
	 * @see ITG3200_RA_PWR_MGM
	 * @see ITG3200_PWR_H_RESET_BIT
	 */
	
	public void reset()
	{
	    writeBit( ITG3200_RA_PWR_MGM, ITG3200_PWR_H_RESET_BIT, true );
	}
	
	/** Get sleep mode status.
	 * Setting the SLEEP bit in the register puts the device into very low power
	 * sleep mode. In this mode, only the serial interface and internal registers
	 * remain active, allowing for a very low standby current. Clearing this bit
	 * puts the device back into normal mode. To save power, the individual standby
	 * selections for each of the gyros should be used if any gyro axis is not used
	 * by the application.
	 * @return Current sleep mode enabled status
	 * @see ITG3200_RA_PWR_MGM
	 * @see ITG3200_PWR_SLEEP_BIT
	 */
	public boolean getSleepEnabled()
	{
		return readBit( ITG3200_RA_PWR_MGM, ITG3200_PWR_SLEEP_BIT );
	}
	
	/** Set sleep mode status.
	 * @param enabled New sleep mode enabled status
	 * @see getSleepEnabled()
	 * @see ITG3200_RA_PWR_MGM
	 * @see ITG3200_PWR_SLEEP_BIT
	 */
	public void setSleepEnabled( boolean enabled )
	{
		writeBit( ITG3200_RA_PWR_MGM, ITG3200_PWR_SLEEP_BIT, enabled );
	}
	
	/** Get X-axis standby enabled status.
	 * If enabled, the X-axis will not gather or report data (or use power).
	 * @return Current X-axis standby enabled status
	 * @see ITG3200_RA_PWR_MGM
	 * @see ITG3200_PWR_STBY_XG_BIT
	 */
	public boolean getStandbyXEnabled(){
		return readBit( ITG3200_RA_PWR_MGM, ITG3200_PWR_STBY_XG_BIT );
	}
	
	/** Set X-axis standby enabled status.
	 * @param New X-axis standby enabled status
	 * @see getStandbyXEnabled()
	 * @see ITG3200_RA_PWR_MGM
	 * @see ITG3200_PWR_STBY_XG_BIT
	 */
	public void setStandbyXEnabled( boolean enabled ) 
	{
	    writeBit(ITG3200_RA_PWR_MGM, ITG3200_PWR_STBY_XG_BIT, enabled );
	}
	
	/** Get Y-axis standby enabled status.
	 * If enabled, the Y-axis will not gather or report data (or use power).
	 * @return Current Y-axis standby enabled status
	 * @see ITG3200_RA_PWR_MGM
	 * @see ITG3200_PWR_STBY_YG_BIT
	 */
	public boolean getStandbyYEnabled()
	{
	    return readBit( ITG3200_RA_PWR_MGM, ITG3200_PWR_STBY_YG_BIT );
	}
	
	/** Set Y-axis standby enabled status.
	 * @param New Y-axis standby enabled status
	 * @see getStandbyYEnabled()
	 * @see ITG3200_RA_PWR_MGM
	 * @see ITG3200_PWR_STBY_YG_BIT
	 */
	public void setStandbyYEnabled( boolean enabled )
	{
	    writeBit( ITG3200_RA_PWR_MGM, ITG3200_PWR_STBY_YG_BIT, enabled );
	}
	
	/** Get Z-axis standby enabled status.
	 * If enabled, the Z-axis will not gather or report data (or use power).
	 * @return Current Z-axis standby enabled status
	 * @see ITG3200_RA_PWR_MGM
	 * @see ITG3200_PWR_STBY_ZG_BIT
	 */
	public boolean getStandbyZEnabled()
	{
	    return readBit( ITG3200_RA_PWR_MGM, ITG3200_PWR_STBY_ZG_BIT );
	}
	
	/** Set Z-axis standby enabled status.
	 * @param New Z-axis standby enabled status
	 * @see getStandbyZEnabled()
	 * @see ITG3200_RA_PWR_MGM
	 * @see ITG3200_PWR_STBY_ZG_BIT
	 */
	public void setStandbyZEnabled( boolean enabled )
	{
	    writeBit( ITG3200_RA_PWR_MGM, ITG3200_PWR_STBY_ZG_BIT, enabled );
	}
	
	/** Get clock source setting.
	 * @return Current clock source setting
	 * @see ITG3200_RA_PWR_MGM
	 * @see ITG3200_PWR_CLK_SEL_BIT
	 * @see ITG3200_PWR_CLK_SEL_LENGTH
	 */
	public byte getClockSource()
	{
		m_i2c.read( ITG3200_RA_PWR_MGM, 1, buffer );
	    // I2Cdev::readBits(devAddr, ITG3200_RA_PWR_MGM, ITG3200_PWR_CLK_SEL_BIT, ITG3200_PWR_CLK_SEL_LENGTH, buffer);
	    return (byte) ( buffer[0] & ITG3200_PWR_CLK_SEL_BIT );
	}
	
	/** Set clock source setting.
	 * On power up, the ITG-3200 defaults to the internal oscillator. It is highly recommended that the device is configured to use one of the gyros (or an external clock) as the clock reference, due to the improved stability.
	 *
	 * The CLK_SEL setting determines the device clock source as follows:
	 *
	 * CLK_SEL | Clock Source
	 * --------+--------------------------------------
	 * 0       | Internal oscillator
	 * 1       | PLL with X Gyro reference
	 * 2       | PLL with Y Gyro reference
	 * 3       | PLL with Z Gyro reference
	 * 4       | PLL with external 32.768kHz reference
	 * 5       | PLL with external 19.2MHz reference
	 * 6       | Reserved
	 * 7       | Reserved
	 *
	 * @param source New clock source setting
	 * @see getClockSource()
	 * @see ITG3200_RA_PWR_MGM
	 * @see ITG3200_PWR_CLK_SEL_BIT
	 * @see ITG3200_PWR_CLK_SEL_LENGTH
	 */
	public void setClockSource(byte source)
	{
	    writeBits( ITG3200_RA_PWR_MGM, ITG3200_PWR_CLK_SEL_BIT, ITG3200_PWR_CLK_SEL_LENGTH, source );
	}


	private ITable m_table;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initTable(ITable subtable) {
		m_table = subtable;
		updateTable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITable getTable() {
		return m_table;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTable() {
		if (m_table != null) {
			m_table.putNumber("X", getRotationX());
			m_table.putNumber("Y", getRotationY());
			m_table.putNumber("Z", getRotationZ());
			m_table.putNumber("Value", pidGet());
		}
	}

	/* (non-Javadoc)
	 * @see edu.wpi.first.wpilibj.Sendable#getSmartDashboardType()
	 */
	@Override
	public String getSmartDashboardType() 
	{
		return "Gyro";
	}

	/* (non-Javadoc)
	 * @see edu.wpi.first.wpilibj.livewindow.LiveWindowSendable#startLiveWindowMode()
	 */
	@Override
	public void startLiveWindowMode() 
	{
	}

	/* (non-Javadoc)
	 * @see edu.wpi.first.wpilibj.livewindow.LiveWindowSendable#stopLiveWindowMode()
	 */
	@Override
	public void stopLiveWindowMode() 
	{
	}

	/* (non-Javadoc)
	 * @see edu.wpi.first.wpilibj.PIDSource#pidGet()
	 */
	@Override
	public double pidGet() 
	{
		// TODO We likely want to return one of the axes based on a setup option.
		AllAxes var = getRotation();
		double result = Math.cbrt( var.XAxis * var.XAxis + var.YAxis * var.YAxis + var.ZAxis * var.ZAxis );
		return result;
		// return 0;
	}

	public static final byte ITG3200_ADDRESS_AD0_LOW      = 0x68; // address pin low (GND), default for SparkFun IMU Digital Combo board
	public static final byte  ITG3200_ADDRESS_AD0_HIGH    = 0x69; // address pin high (VCC), default for SparkFun ITG-3200 Breakout board
	public static final byte  ITG3200_DEFAULT_ADDRESS     = ITG3200_ADDRESS_AD0_LOW;
	
	public static final byte  ITG3200_RA_WHO_AM_I         = 0x00;
	public static final byte  ITG3200_RA_SMPLRT_DIV       = 0x15;
	public static final byte  ITG3200_RA_DLPF_FS          = 0x16;
	public static final byte  ITG3200_RA_INT_CFG          = 0x17;
	public static final byte  ITG3200_RA_INT_STATUS       = 0x1A;
	public static final byte  ITG3200_RA_TEMP_OUT_H       = 0x1B;
	public static final byte  ITG3200_RA_TEMP_OUT_L       = 0x1C;
	public static final byte  ITG3200_RA_GYRO_XOUT_H      = 0x1D;
	public static final byte  ITG3200_RA_GYRO_XOUT_L      = 0x1E;
	public static final byte  ITG3200_RA_GYRO_YOUT_H      = 0x1F;
	public static final byte  ITG3200_RA_GYRO_YOUT_L      = 0x20;
	public static final byte  ITG3200_RA_GYRO_ZOUT_H      = 0x21;
	public static final byte  ITG3200_RA_GYRO_ZOUT_L      = 0x22;
	public static final byte  ITG3200_RA_PWR_MGM          = 0x3E;
	
	public static final short  ITG3200_DEVID_BIT           = 6;
	public static final short  ITG3200_DEVID_LENGTH        = 6;
	
	public static final short  ITG3200_DF_FS_SEL_BIT       = 4;
	public static final short  ITG3200_DF_FS_SEL_LENGTH    = 2;
	public static final short  ITG3200_DF_DLPF_CFG_BIT     = 2;
	public static final short  ITG3200_DF_DLPF_CFG_LENGTH  = 3;
	
	public static final byte  ITG3200_FULLSCALE_2000      = 0x03;
	
	public static final byte  ITG3200_DLPF_BW_256         = 0x00;
	public static final byte  ITG3200_DLPF_BW_188         = 0x01;
	public static final byte  ITG3200_DLPF_BW_98          = 0x02;
	public static final byte  ITG3200_DLPF_BW_42          = 0x03;
	public static final byte  ITG3200_DLPF_BW_20          = 0x04;
	public static final byte  ITG3200_DLPF_BW_10          = 0x05;
	public static final byte  ITG3200_DLPF_BW_5           = 0x06;
	
	public static final byte  ITG3200_INTCFG_ACTL_BIT             = 7;
	public static final byte  ITG3200_INTCFG_OPEN_BIT             = 6;
	public static final byte  ITG3200_INTCFG_LATCH_INT_EN_BIT     = 5;
	public static final byte  ITG3200_INTCFG_INT_ANYRD_2CLEAR_BIT = 4;
	public static final byte  ITG3200_INTCFG_ITG_RDY_EN_BIT       = 2;
	public static final byte  ITG3200_INTCFG_RAW_RDY_EN_BIT       = 0;
	
	public static final byte  ITG3200_INTMODE_ACTIVEHIGH  = 0x00;
	public static final byte  ITG3200_INTMODE_ACTIVELOW   = 0x01;
	
	public static final byte  ITG3200_INTDRV_PUSHPULL     = 0x00;
	public static final byte  ITG3200_INTDRV_OPENDRAIN    = 0x01;
	
	public static final byte  ITG3200_INTLATCH_50USPULSE  = 0x00;
	public static final byte  ITG3200_INTLATCH_WAITCLEAR  = 0x01;
	
	public static final byte  ITG3200_INTCLEAR_STATUSREAD = 0x00;
	public static final byte  ITG3200_INTCLEAR_ANYREAD    = 0x01;
	
	public static final byte  ITG3200_INTSTAT_ITG_RDY_BIT         = 2;
	public static final byte  ITG3200_INTSTAT_RAW_DATA_READY_BIT  = 0;
	
	public static final byte  ITG3200_PWR_H_RESET_BIT     = 7;
	public static final byte  ITG3200_PWR_SLEEP_BIT       = 6;
	public static final byte  ITG3200_PWR_STBY_XG_BIT     = 5;
	public static final byte  ITG3200_PWR_STBY_YG_BIT     = 4;
	public static final byte  ITG3200_PWR_STBY_ZG_BIT     = 3;
	public static final byte  ITG3200_PWR_CLK_SEL_BIT     = 2;
	public static final byte  ITG3200_PWR_CLK_SEL_LENGTH  = 3;
	
	public static final byte  ITG3200_CLOCK_INTERNAL      = 0x00;
	public static final byte  ITG3200_CLOCK_PLL_XGYRO     = 0x01;
	public static final byte  ITG3200_CLOCK_PLL_YGYRO     = 0x02;
	public static final byte  ITG3200_CLOCK_PLL_ZGYRO     = 0x03;
	public static final byte  ITG3200_CLOCK_PLL_EXT32K    = 0x04;
	public static final byte  ITG3200_CLOCK_PLL_EXT19M    = 0x05;
}
