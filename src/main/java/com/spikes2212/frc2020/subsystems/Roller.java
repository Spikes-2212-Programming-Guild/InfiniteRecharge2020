package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.ColorDetector;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.TalonSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Roller extends GenericSubsystem implements TalonSubsystem {

    public static final RootNamespace rollerNamespace = new RootNamespace("roller");
    public static final Supplier<Double> MIN_SPEED = rollerNamespace.addConstantDouble("min speed", -1);
    public static final Supplier<Double> MAX_SPEED = rollerNamespace.addConstantDouble("max speed", 1);

    private final double EIGHTHS_TO_PULSES = 45 * 4096 * 0.0;

    private static final List<Color> COLOR_ORDER = new ArrayList<>();

    private static DriverStation driverStation = DriverStation.getInstance();

    private static Color inFrontOf(Color color) {
        return COLOR_ORDER.get((COLOR_ORDER.indexOf(color) + 2) % 4);
    }

    public static Color getColorFromFMS() {
        String color = driverStation.getGameSpecificMessage();
        if (color.length() > 0) {
            switch (color.charAt(0)) {
                case 'B':
                    return ColorDetector.blueTarget;
                case 'G':
                    return ColorDetector.greenTarget;
                case 'R':
                    return ColorDetector.redTarget;
                case 'Y':
                    return ColorDetector.yellowTarget;
                default:
                    throw new IllegalArgumentException("the color from the FMS is incorrect, the color is: " + color);
            }
        } else
            return null;
    }

    private static Roller instance = new Roller();

    public static Roller getInstance() {
        return instance;
    }

    public final Namespace PID = rollerNamespace.addChild("PID");
    public final Supplier<Double> kP = PID.addConstantDouble("kP", 0.5);
    public final Supplier<Double> kI = PID.addConstantDouble("kI", 0);
    public final Supplier<Double> kD = PID.addConstantDouble("kD", 0);
    public final Supplier<Double> tolerance = PID.addConstantDouble("tolerance", 0);
    public final Supplier<Integer> timeout = PID.addConstantInt("timeout", 30);

    private WPI_TalonSRX motor;
    private ColorDetector detector;
    private DigitalInput limit;

    private Roller() {
        super(MIN_SPEED, MAX_SPEED);
        this.motor = new WPI_TalonSRX(RobotMap.CAN.ROLLER_MOTOR);
        this.detector = new ColorDetector(I2C.Port.kOnboard);
        this.limit = new DigitalInput(RobotMap.DIO.ROLLER_LIMIT);
        COLOR_ORDER.add(ColorDetector.blueTarget);
        COLOR_ORDER.add(ColorDetector.redTarget);
        COLOR_ORDER.add(ColorDetector.greenTarget);
        COLOR_ORDER.add(ColorDetector.yellowTarget);
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return limit.get() || speed == 0;
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    @Override
    public void periodic() {
        rollerNamespace.update();
    }

    @Override
    public void configureDashboard() {
        rollerNamespace.putData("roll with FMS",
                new MoveTalonSubsystem(this, getSetpoint(getColorFromFMS()), () -> 0.0));
    }

    private double getSetpoint(Color color) {
        if(color == null) return 0;
        int targetIndex = COLOR_ORDER.indexOf(inFrontOf(color));
        int currentIndex = COLOR_ORDER.indexOf(detector.getDetectedColor());
        if (targetIndex == -1 || currentIndex == -1) return 0;
        double offset = targetIndex - currentIndex;
        if (Math.abs(offset) == 3) offset *= -(1 / 3.0);
        return offset;
    }

    public boolean isPressed() {
        return limit.get();
    }

    @Override
    public void configureLoop() {
        motor.configFactoryDefault();
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, timeout.get());

        motor.configNominalOutputForward(0, timeout.get());
        motor.configNominalOutputReverse(0, timeout.get());
        motor.configPeakOutputForward(MAX_SPEED.get(), timeout.get());
        motor.configPeakOutputReverse(MIN_SPEED.get(), timeout.get());

        motor.configAllowableClosedloopError(0, 0, timeout.get());
        motor.config_kP(0, kP.get(), timeout.get());
        motor.config_kI(0, kI.get(), timeout.get());
        motor.config_kD(0, kD.get(), timeout.get());
    }

    @Override
    public void pidSet(double setpoint) {
        setpoint *= EIGHTHS_TO_PULSES;

        motor.configPeakOutputForward(MAX_SPEED.get(), timeout.get());
        motor.configPeakOutputReverse(MIN_SPEED.get(), timeout.get());

        motor.config_kP(0, kP.get(), timeout.get());
        motor.config_kI(0, kI.get(), timeout.get());
        motor.config_kD(0, kD.get(), timeout.get());

        motor.set(ControlMode.Position, setpoint);
    }

    @Override
    public void finish() {
        stop();
    }

    @Override
    public boolean onTarget(double setpoint) {
        setpoint *= EIGHTHS_TO_PULSES;

        double tolerance = this.tolerance.get() * EIGHTHS_TO_PULSES;
        int position = motor.getSelectedSensorPosition();

        return !canMove(motor.getMotorOutputPercent()) || Math.abs(setpoint - position) < tolerance;
    }
}
