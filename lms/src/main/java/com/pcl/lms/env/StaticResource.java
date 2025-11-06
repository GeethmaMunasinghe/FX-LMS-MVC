package com.pcl.lms.env;

public class StaticResource {
    private final static String VERSION="1.0.0";
    private final static String COMPANY="BrightLabs Innovations";

    public static String getVersion(){
        return VERSION;
    }
    public static String getCompany(){
        return COMPANY;
    }

}
