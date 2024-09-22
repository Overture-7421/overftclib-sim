package org.overture.ftc;

import com.qualcomm.robotcore.hardware.HardwareMap;
import edu.wpi.first.networktables.NetworkTableEntry;
import org.overture.ftc.overftclib.IOverDcMotor;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.math.controller.PIDController;

public class OverDcMotor implements IOverDcMotor {
    NetworkTable motorTable;
    NetworkTableEntry voltageEntry;
    NetworkTableEntry encoderPositionEntry;
    NetworkTableEntry encoderSpeedEntry;
    NetworkTableEntry softwareInvertedEntry;
    PIDController positionController = new PIDController(1.0, 0.0, 0.0);
    PIDController speedController = new PIDController(1.0, 0.0, 0.0);
    double encoderOffset = 0.0;
    RunMode currentRunMode = RunMode.RUN_WITHOUT_ENCODER;
    double encoderPPR = 7.0;
    public OverDcMotor(HardwareMap hardwareMap, String motorName) {
        motorTable = hardwareMap.getMotorTable().getSubTable(motorName);

        voltageEntry = motorTable.getEntry("voltage_applied");
        encoderPositionEntry = motorTable.getEntry("encoder_position");
        encoderSpeedEntry = motorTable.getEntry("encoder_speed");
        softwareInvertedEntry = motorTable.getEntry("software_inverted");

        SimMotorManager.AddSimMotor(this);
    }
    @Override
    public void setPower(double v) {
        voltageEntry.setNumber(v * 12);
    }

    @Override
    public double getPower() {
        return voltageEntry.getNumber(0.0).doubleValue() / 12.0;
    }

    @Override
    public void setDirection(Direction direction) {
        switch (direction){
            case FORWARD : {
                softwareInvertedEntry.setBoolean(false);
                break;
            }
            case REVERSE : {
                softwareInvertedEntry.setBoolean(true);
                break;
            }
        }
    }

    @Override
    public Direction getDirection() {
        return softwareInvertedEntry.getBoolean(false) ? Direction.FORWARD : Direction.REVERSE;
    }

    @Override
    public void setMode(RunMode runMode) {
        this.currentRunMode = runMode;
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {

    }

    @Override
    public void setTargetPosition(int i) {
        positionController.setSetpoint((double) i / encoderPPR);
    }

    @Override
    public int getTargetPosition() {
        return (int) (positionController.getSetpoint() * encoderPPR);
    }

    @Override
    public int getCurrentPosition() {
        return (int) (encoderPositionEntry.getNumber(0.0).doubleValue() * encoderPPR + encoderOffset);
    }

    public void updateInternalSimState(){
        switch (currentRunMode){
            case RUN_WITHOUT_ENCODER : {
                break;
            }
            case RUN_USING_ENCODER : {
                setPower(speedController.calculate(encoderSpeedEntry.getDouble(0.0)));
                break;
            }
            case RUN_TO_POSITION : {
                setPower(positionController.calculate(encoderPositionEntry.getDouble(0.0)));
                break;
            }
            case STOP_AND_RESET_ENCODER : {
                setPower(0.0);
                encoderOffset = -getCurrentPosition();
                break;
            }
        }
    }
}
