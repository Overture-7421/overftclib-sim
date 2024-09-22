package org.overture.ftc;

import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.ArrayList;
import java.util.List;
public final class SimMotorManager {
    private static NetworkTableInstance ntInst = NetworkTableInstance.getDefault();
    private static final List<OverDcMotor> registeredMotors = new ArrayList<>();

    public static void AddSimMotor(OverDcMotor dcMotor) {
        synchronized (registeredMotors) {
            registeredMotors.add(dcMotor);
        }
    }

    public static void UpdateSimMotors() {
        synchronized (registeredMotors) {
            for (OverDcMotor motor : registeredMotors) {
                motor.updateInternalSimState();
            }
            ntInst.flush();
        }
    }
}
