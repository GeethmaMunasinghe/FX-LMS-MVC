package com.pcl.lms.DB;

import com.pcl.lms.model.User;
import com.pcl.lms.util.security.PasswordManager;

import java.util.ArrayList;

public class Database {
    public static ArrayList<User> userTable=new ArrayList<>();

    static {
        userTable.add(new User("Geethma Munasinghe","gmgee1175@gmail.com",24,new PasswordManager().encode("1234")));
    }

}
