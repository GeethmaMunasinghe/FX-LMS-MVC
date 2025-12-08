package com.pcl.lms.DB;

import com.pcl.lms.model.*;
import com.pcl.lms.util.security.PasswordManager;

import java.util.ArrayList;

public class Database {
    public static ArrayList<User> userTable=new ArrayList<>();
    public static ArrayList<Student> studentTable=new ArrayList<>();
    public static ArrayList<Teacher> teacherTable=new ArrayList<>();
    public static ArrayList<Programme> programmeTable=new ArrayList<>();
    public static ArrayList<Intake> intakeTable=new ArrayList<>();

    static {
        userTable.add(new User("Geethma Munasinghe","gmgee1175@gmail.com",24,new PasswordManager()
                .encode("1234")));
        teacherTable.add(new Teacher("T-1","Vinu","0776543123","Matara"));
        teacherTable.add(new Teacher("T-2","Mashi","0112345678","Colombo"));
    }

}
