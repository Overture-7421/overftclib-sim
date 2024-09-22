package com.qualcomm.robotcore.hardware;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class HardwareMap {
    private final NetworkTable robotTable;
    public HardwareMap(String robotName) {
        NetworkTableInstance ntInst = NetworkTableInstance.getDefault();
        robotTable = ntInst.getTable(robotName);
    }

    public NetworkTable getRobotTable() {
        return robotTable;
    }

    public NetworkTable getMotorTable() {
        return robotTable.getSubTable("motors");
    }
}
