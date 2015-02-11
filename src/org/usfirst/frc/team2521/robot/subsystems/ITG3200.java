package org.usfirst.frc.team2521.robot.subsystems;

	
	/* 
 * Copyright (c) 2015 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */


import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.BoundaryException;

import org.usfirst.frc.team2521.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj.I2C;

/**
 * Driver for the InvenSense ITG-3200 three axis digital gyroscope.
 * 
 * @author Ben Wolsieffer
 */
public class ITG3200 extends SensorBase {

    public static final double DEFAULT_CALIBRATION_TIME = 5.0;

    /**
     * The gyro sensitivity in LSB/radian/second. The value retrieved from the
     * gyro should be divided by this number.
     */
    public static final double GYRO_SENSITIVITY = 823.626830313;

    /**
     * The temperature sensor sensitivity in LSB/degree celsius. The value
     * retrieved from the temperature sensor should be divided by this number.
     */
    public static final double TEMPERATURE_SENSITIVITY = 280;
    /**
     * The ofset of the value retrieved from the temperature sensor.
     */
    public static final int TEMPERATURE_OFFSET = -13200;

    /**
     * The default address for the gyro. This is what the SparkFun breakout
     * uses.
     */
    public static final int ADDRESS = 0b1101000;
    /**
     * The alternative address that is used if AD0 is pulled high.
     */
    public static final int ALT_ADDRESS = ADDRESS | 1;

    /**
     * The address of the interrupt configuration register.
     */
    public static final int INTERRUPT_CONFIG_REGISTER = 23;
    /**
     * The address of the digital low pass filter (DLPF) and full scale
     * register.
     */
    public static final int DLPF_FULL_SCALE_REGISTER = 22;
    /**
     * The address of the sample rate divider register.
     */
    public static final int SAMPLE_RATE_REGISTER = 21;
    /**
     * The address of the interrupt status register.
     */
    public static final int INTERRUPT_STATUS_REGISTER = 26;
    /**
     * The address of the temperature sensor register.
     */
    public static final int TEMPERATURE_REGISTER = 27;
    /**
     * The address of the gyro reading register.
     */
    public static final int GYRO_REGISTER = 29;
    /**
     * The address of the power management register.
     */
    public static final int POWER_MANAGEMENT_REGISTER = 62;

    /**
     * Represents the different types of digital filters that the gyro can use.
     */
    public enum Filter {
        /**
         * 256Hz digital low pass filter. This sets the internal sample rate to
         * 8KHz.
         */
        DLPF_256Hz(0),
        /**
         * 188Hz digital low pass filter. This sets the internal sample rate to
         * 1KHz.
         */
        DLPF_188Hz(1),
        /**
         * 98Hz digital low pass filter. This sets the internal sample rate to
         * 1KHz.
         */
        DLPF_98Hz(2),
        /**
         * 42Hz digital low pass filter. This sets the internal sample rate to
         * 1KHz.
         */
        DLPF_42Hz(3),
        /**
         * 20Hz digital low pass filter. This sets the internal sample rate to
         * 1KHz.
         */
        DLPF_20Hz(4),
        /**
         * 10Hz digital low pass filter. This sets the internal sample rate to
         * 1KHz.
         */
        DLPF_10Hz(5),
        /**
         * 5Hz digital low pass filter. This sets the internal sample rate to
         * 1KHz.
         */
        DLPF_5Hz(6);

        private final int value;

        private Filter(int value) {
            this.value = value;
        }
    }

    /**
     * The clock source to use for the gyro.
     */
    public enum ClockSource {
        /**
         * Internal clock source. This is used at startup and is less accurate.
         */
        INTERNAL(0),
        /**
         * X axis gyro clock source.
         */
        X_AXIS(1),
        /**
         * Y axis gyro clock source.
         */
        Y_AXIS(2),
        /**
         * Z axis gyro clock source.
         */
        Z_AXIS(3),
        /**
         * External 32.768KHz clock source. This cannot be used on the SparkFun
         * breakout.
         */
        EXT_32_768KHZ(4),
        /**
         * External 19.2MHz clock source. This cannot be used on the SparkFun
         * breakout.
         */
        EXT_19_2MHZ(5);

        private final int value;

        private ClockSource(int value) {
            this.value = value;
        }
    }

    /**
     * The number samples that were taken during calibration.
     */
    private int numCalibrationSamples = 0;

    /**
     * Represents an axis of the gyro.
     */
    public class GyroAxis {

        /**
         * The rate of the gyro axis.
         */
        private double rate = 0;
        /**
         * The angle of the gyro axis.
         */
        private double angle = 0;
        /**
         * The calibrated center of the gyro axis.
         */
        private double center = 0;
        /**
         * Used internally to store the sum of the calibration samples.
         */
        private int calibrationSum = 0;

        /**
         * {@inheritDoc}
         */
        
        public double getAngle() {
            synchronized (ITG3200.this) {
                return angle;
            }
        }

        /**
         * {@inheritDoc}
         */
        
        public double getRate() {
            synchronized (ITG3200.this) {
                return rate;
            }
        }

        /**
         * {@inheritDoc}
         */
        
        public void reset() {
            synchronized (ITG3200.this) {
                angle = 0;
            }
        }
    }

    /**
     * The I2C object used to communicate.
     */
    private final I2C i2c;

    /**
     * The I2C address of the gyro.
     */
    private final int address;
    /**
     * The filter the gyro should use.
     */
    private Filter filter = Filter.DLPF_256Hz;
    /**
     * The internal sample rate of the gyro in hertz.
     */
    private int internalSampleRate;
    /**
     * The actual sample rate after the divider is applied.
     */
    private double sampleRate;
    /**
     * The sample rate divider. The internal sample rate is divided by this
     * value to get the real sample rate.
     */
    private int sampleRateDivider = 250;
    /**
     * The clock source for the gyro.
     */
    private ClockSource clockSource = ClockSource.X_AXIS;

    /**
     * Flag that is set true during calibration.
     */
    private boolean calibrate = false;

    /**
     * The x axis of the gyro.
     */
    private GyroAxis xGyro = new GyroAxis();
    /**
     * The y axis of the gyro.
     */
    private GyroAxis yGyro = new GyroAxis();
    /**
     * the x axis of the gyro.
     */
    private GyroAxis zGyro = new GyroAxis();

    /**
     * Creates a new ITG-3200 on the specified I2C port and digital interrupt
     * port.
     * 
     * @param port the I2C port the gyro is attached to
     * @param interrupt the interrupt port to use
     */
    public ITG3200(Port port) {
        this(port, false);
    }

    /**
     * Creates a new ITG-3200 on the specified I2C port and digital interrupt
     * port, possibly using the alternate address.
     * 
     * @param port the I2C port the gyro is attached to
     * @param interrupt the interrupt port to use
     * @param altAddress whether to use the alternate address
     */
    public ITG3200(Port port, boolean altAddress) {
        // Setup the address
        if (altAddress) {
            address = ALT_ADDRESS;
        } else {
            address = ADDRESS;
        }

        i2c = new I2C(port, address);

        // Register an interrupt handler
//        interrupt.requestInterrupts(new InterruptHandlerFunction<Object>() {
//
//            
//            public void interruptFired(int interruptAssertedMask, Object param) {
//                if (calibrate) {
//                    // If in calibration mode, run the calibration handler
//                    readCalibrationAxes();
//                } else {
//                    // Otherwise, read the axes normally
//                    readAxes();
//                }
//            }
//        });
//        // Listen for a falling edge
//        interrupt.setUpSourceEdge(false, true);

        // Write the power management register (ie. clock source)
        writePowerManagement();
        // Write the filter and full scale register
        writeDLPFFullScale();
        // Write sample rate divider
        writeSampleRateDivider();

        // Enable digital interrupt pin
        //interrupt.enableInterrupts();
        // Write interrupt config to gyro
        writeInterruptConfig();
        // Clear any existing interrupts in case the robot code was restarted
        // but the gyro was not
        clearInterrupts();

        // Calibrate the gyro
        calibrate(DEFAULT_CALIBRATION_TIME);
    }

    /**
     * Read the axes and add the values to the calibration sum.
     */
    private void readCalibrationAxes() {
        byte[] axes = new byte[6];
        i2c.read(GYRO_REGISTER, 6, axes);

        numCalibrationSamples++;
        xGyro.calibrationSum += (axes[0] << 8) | axes[1];
        yGyro.calibrationSum += (axes[2] << 8) | axes[3];
        zGyro.calibrationSum += (axes[4] << 8) | axes[5];
    }

    /**
     * Read the axes and compute the rate and angle.
     */
    private void readAxes() {
        byte[] axes = new byte[6];
        i2c.read(GYRO_REGISTER, 6, axes);

        synchronized (this) {
            xGyro.rate = (((axes[0] << 8) | axes[1]) - xGyro.center) / GYRO_SENSITIVITY;
            yGyro.rate = (((axes[2] << 8) | axes[3]) - yGyro.center) / GYRO_SENSITIVITY;
            zGyro.rate = (((axes[4] << 8) | axes[5]) - zGyro.center) / GYRO_SENSITIVITY;

            xGyro.angle += xGyro.rate / sampleRate;
            yGyro.angle += yGyro.rate / sampleRate;
            zGyro.angle += zGyro.rate / sampleRate;
        }
    }

    /**
     * Get the temperature of the gyro in degrees celcius.
     * 
     * @return the temperature
     */
    public double getTemperature() {
        byte[] temp = new byte[2];
        i2c.read(TEMPERATURE_REGISTER, 2, temp);

        return (((temp[0] << 8) | temp[1]) + TEMPERATURE_OFFSET) / TEMPERATURE_SENSITIVITY;
    }

    /**
     * Write the interrupt configuration flags.
     */
    private void writeInterruptConfig() {
        // 7: Logic level for INT output pin - 1=active low, 0=active high
        // 6: Drive type for INT output pin - 1=open drain, 0=push-pull
        // 5: Latch mode - 1=latch until interrupt is cleared, 0=50us pulse
        // 4: Latch clear method - 1=any register read, 0=status register read
        // only
        // 3: 0
        // 2: Enable interrupt when device is ready (PLL ready after changing
        // clock source)
        // 1: 0
        // 0: Enable interrupt when data is available

        i2c.write(INTERRUPT_CONFIG_REGISTER, 0b11110001);
    }

    /**
     * Clear the interrupt status register.
     */
    private void clearInterrupts() {
        // The gyro is configured to clear interrupts on any register read.
        byte[] buf = new byte[1];
        i2c.read(INTERRUPT_STATUS_REGISTER, 1, buf);
    }

    /**
     * Sets the filter type to use. This also may change the sample rate.
     * 
     * @see Filter
     * @param filter the filter type
     */
    public void setFilter(Filter filter) {
        this.filter = filter;
        writeDLPFFullScale();
        writeSampleRateDivider();
    }

    /**
     * Write the digital filter register and the full scale flag.
     */
    private void writeDLPFFullScale() {
        switch (filter) {
        case DLPF_256Hz:
            internalSampleRate = 8000;
        break;
        default:
            internalSampleRate = 1000;
        break;
        }

        int data = (3 << 3) | filter.value;
        i2c.write(DLPF_FULL_SCALE_REGISTER, data);
    }

    /**
     * set the sample rate divider. This divides the internal sample rate by the
     * specified value.
     * 
     * @param divider the sample rate divider
     */
    public void setSampleRateDivider(int divider) {
        BoundaryException.assertWithinBounds(divider, 1, 256);
        sampleRateDivider = divider;
        writeSampleRateDivider();
    }

    /**
     * Write the sample rate divider register.
     */
    private void writeSampleRateDivider() {
        sampleRate = ((double) internalSampleRate) / sampleRateDivider;
        i2c.write(SAMPLE_RATE_REGISTER, sampleRateDivider - 1);
    }

    /**
     * Set the clock source to use for the gyro.
     * 
     * @param source the clock source
     */
    public void setClockSource(ClockSource source) {
        clockSource = source;
        writePowerManagement();
    }

    /**
     * Calibrate the gyro for the specified amount of time. This should only be
     * called when the gyro is not moving. It is also done automatically upon
     * construction.
     * 
     * @param time the calibration time
     */
    public void calibrate(double time) {
        synchronized (this) {
            numCalibrationSamples = 0;
            xGyro.calibrationSum = 0;
            yGyro.calibrationSum = 0;
            zGyro.calibrationSum = 0;

            calibrate = true;
            Timer.delay(time);
            calibrate = false;

            xGyro.center = ((double) xGyro.calibrationSum) / numCalibrationSamples;
            yGyro.center = ((double) yGyro.calibrationSum) / numCalibrationSamples;
            zGyro.center = ((double) zGyro.calibrationSum) / numCalibrationSamples;
        }
    }

    /**
     * Write the power management (clock source) register.
     */
    private void writePowerManagement() {
        int data = clockSource.value;
        i2c.write(POWER_MANAGEMENT_REGISTER, data);
    }

    /**
     * Gets a {@link Gyro} object representing the x axis of the gyro.
     * 
     * @return the x axis
     */
    public GyroAxis getXGyro() {
        return xGyro;
    }
 
    /**
     * Gets a {@link Gyro} object representing the y axis of the gyro.
     * 
     * @return the y axis
     */
    public GyroAxis getYGyro() {
        return yGyro;
    }
 
    /**
     * Gets a {@link Gyro} object representing the z axis of the gyro.
     * 
     * @return the z axis
     */
    public GyroAxis getZGyro() {
        return zGyro;
    }

}
