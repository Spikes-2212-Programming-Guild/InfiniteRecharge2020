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
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import java.util.function.Supplier;

public class Roller extends GenericSubsystem implements TalonSubsystem {

    public static RootNamespace rollerNamespace = new RootNamespace("roller");
    public static Namespace PID = rollerNamespace.addChild("PID");

    public static Supplier<Double> MIN_SPEED = rollerNamespace.addConstantDouble("min speed", -1);
    public static Supplier<Double> MAX_SPEED = rollerNamespace.addConstantDouble("max speed", 1);
    public static Supplier<Double> kP = PID.addConstantDouble("kP", 0.5);
    public static Supplier<Double> kI = PID.addConstantDouble("kI", 0);
    public static Supplier<Double> kD = PID.addConstantDouble("kD", 0);
    public static Supplier<Double> tolerance = PID.addConstantDouble("tolerance", 0);
    public static Supplier<Integer> timeout = PID.addConstantInt("timeout", 30);

    private static final double EIGHTHS_TO_PULSES = 45 * 4096 * 0.0;
    private static final Color[] COLOR_ORDER = {Color.kRed, Color.kGreen, Color.kYellow, Color.kBlue};

    private WPI_TalonSRX motor;
    private ColorDetector detector;

    private static Roller instance;

    public static Roller getInstance() {
        if (instance == null) {
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.ROLLER_MOTOR);
            ColorDetector colorDetector = new ColorDetector(I2C.Port.kOnboard);
            instance = new Roller(MIN_SPEED, MAX_SPEED, motor, colorDetector);
        }
        return instance;
    }

    private Roller(Supplier<Double> minSpeed, Supplier<Double> maxSpeed, WPI_TalonSRX motor, ColorDetector detector) {
        super(minSpeed, maxSpeed);
        this.motor = motor;
        this.detector = detector;
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return true;
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
        rollerNamespace.putData("roll to yellow",
                new MoveTalonSubsystem(this, getSetpoint(Color.kYellow), () -> 0.0));
        rollerNamespace.putString("color", getMatchingConstant(detector.getColor())::name);
    }

    private com.spikes2212.frc2020.Color getMatchingConstant(Color color) {
        for(com.spikes2212.frc2020.Color value : com.spikes2212.frc2020.Color.values()) {
            if(value.getBlue() == color.blue && value.getRed() == color.red && value.getGreen() == color.green)
                return value;
        }
        return com.spikes2212.frc2020.Color.kWTF;
    }

    private int indexOf(Color[] array, Color value) {
        for (int i = 0; i < array.length; i++)
            if (array[i].equals(value))
                return i;
        return -1;
    }

    private Supplier<Double> getSetpoint(Color color) {
        int destIndex = indexOf(COLOR_ORDER, color);
        int currIndex = indexOf(COLOR_ORDER, detector.getColor());
        if(destIndex == -1 || currIndex == -1) return null;
        double offset = destIndex - currIndex;
        if (Math.abs(offset) == 3) offset *= -(1/3.0);
        double effectivelyFinalOffset = offset;
        return () -> effectivelyFinalOffset;
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

        double tolerance = Roller.tolerance.get() * EIGHTHS_TO_PULSES;
        int position = motor.getSelectedSensorPosition();

        return !canMove(motor.getMotorOutputPercent()) || Math.abs(setpoint - position) < tolerance;
    }
}
