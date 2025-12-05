package com.pcl.lms.DB;

import com.pcl.lms.model.Programme;
import com.pcl.lms.model.Student;
import com.pcl.lms.model.Teacher;
import com.pcl.lms.model.User;
import com.pcl.lms.util.security.PasswordManager;

import java.util.ArrayList;

public class Database {
    public static ArrayList<User> userTable=new ArrayList<>();
    public static ArrayList<Student> studentTable=new ArrayList<>();
    public static ArrayList<Teacher> teacherTable=new ArrayList<>();
    public static ArrayList<Programme> programmeTable=new ArrayList<>();


    static {
        userTable.add(new User("Geethma Munasinghe","gmgee1175@gmail.com",24,new PasswordManager().encode("1234")));
    }

}
