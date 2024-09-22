package org.overture.ftc;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@SuppressWarnings (value="unchecked")
public class OpModeManager {
    public static class OpModeEntry {
        public OpModeEntry(String Name, String Group, Class<? extends LinearOpMode> Class) {
            this.Name = Name;
            this.Group = Group;
            this.Class = Class;
        }
        public String Name;
        public String Group;
        public Class<? extends LinearOpMode> Class;
    }
    private final ArrayList<OpModeEntry> teleopOpModes = new ArrayList<>();
    private final ArrayList<OpModeEntry> autoOpModes = new ArrayList<>();
    private static String PackageToScan = "org.firstinspires.ftc.teamcode";
    OpModeManager() {
        Set<Class<?>> teleopOpAnnotatedTypes = new Reflections(PackageToScan).getTypesAnnotatedWith(TeleOp.class);
        Set<Class<?>> autoOpAnnotatedTypes = new Reflections(PackageToScan).getTypesAnnotatedWith(Autonomous.class);

        for(Class<?> type : teleopOpAnnotatedTypes) {
            System.out.println(type.getCanonicalName());
            if(type.getSuperclass() == LinearOpMode.class){
                TeleOp annotation = type.getAnnotation(TeleOp.class);
                teleopOpModes.add(new OpModeEntry(annotation.name(), annotation.group(), (Class<? extends LinearOpMode>) type));
            }
        }

        for(Class<?> type : autoOpAnnotatedTypes) {
            System.out.println(type.getCanonicalName());
            if(type.getSuperclass() == LinearOpMode.class){
                Autonomous annotation = type.getAnnotation(Autonomous.class);
                teleopOpModes.add(new OpModeEntry(annotation.name(), annotation.group(), (Class<? extends LinearOpMode>) type));
            }
        }
    }
    public final ArrayList<OpModeEntry> GetTeleopOpModes() {
        return teleopOpModes;
    }

    public final ArrayList<OpModeEntry> GetAutoOpModes() {
        return autoOpModes;
    }
}
