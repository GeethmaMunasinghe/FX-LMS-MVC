package com.pcl.lms.util.security;
import org.mindrot.jbcrypt.BCrypt;


public class PasswordManager {
    public String encode(String rowPassword){
        return BCrypt.hashpw(rowPassword,BCrypt.gensalt(10));
    }
    public boolean check(String rowPassword,String hashedPassword){
        return BCrypt.checkpw(rowPassword,hashedPassword);
    }
}
